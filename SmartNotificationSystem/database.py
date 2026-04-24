from __future__ import annotations

import sqlite3
from contextlib import contextmanager
from pathlib import Path
from typing import Dict, Iterable, Iterator, List, Optional

DEFAULT_KEYWORDS = [
    "acil",
    "toplanti",
    "hata",
    "fatura",
    "deadline",
    "sunum",
    "urgent",
    "exam",
    "meeting",
    "error",
    "invoice",
]


class NotificationDatabase:
    def __init__(self, db_path: str | Path):
        self._db_path = Path(db_path)
        self._db_path.parent.mkdir(parents=True, exist_ok=True)
        self._initialize()

    @contextmanager
    def connection(self) -> Iterator[sqlite3.Connection]:
        conn = sqlite3.connect(self._db_path)
        conn.row_factory = sqlite3.Row
        try:
            yield conn
            conn.commit()
        finally:
            conn.close()

    def _initialize(self) -> None:
        with self.connection() as conn:
            conn.executescript(
                """
                CREATE TABLE IF NOT EXISTS notifications (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    sender TEXT NOT NULL,
                    content TEXT NOT NULL,
                    source TEXT NOT NULL,
                    subject TEXT,
                    platform TEXT,
                    received_at TEXT NOT NULL,
                    score REAL NOT NULL,
                    is_critical INTEGER NOT NULL,
                    feedback INTEGER
                );

                CREATE TABLE IF NOT EXISTS muted_apps (
                    app_name TEXT PRIMARY KEY
                );

                CREATE TABLE IF NOT EXISTS keyword_weights (
                    keyword TEXT PRIMARY KEY,
                    weight REAL NOT NULL,
                    positive_hits INTEGER NOT NULL DEFAULT 0,
                    negative_hits INTEGER NOT NULL DEFAULT 0,
                    updated_at TEXT NOT NULL
                );
                """
            )

    def insert_notification(
        self,
        *,
        sender: str,
        content: str,
        source: str,
        subject: Optional[str],
        platform: Optional[str],
        received_at: str,
        score: float,
        is_critical: bool,
    ) -> int:
        with self.connection() as conn:
            cursor = conn.execute(
                """
                INSERT INTO notifications (
                    sender, content, source, subject, platform, received_at, score, is_critical
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """,
                (
                    sender,
                    content,
                    source,
                    subject,
                    platform,
                    received_at,
                    score,
                    int(is_critical),
                ),
            )
            return int(cursor.lastrowid)

    def get_notification(self, notification_id: int) -> Optional[Dict]:
        with self.connection() as conn:
            row = conn.execute(
                "SELECT * FROM notifications WHERE id = ?",
                (notification_id,),
            ).fetchone()
            return dict(row) if row else None

    def list_notifications(self, limit: int = 100) -> List[Dict]:
        with self.connection() as conn:
            rows = conn.execute(
                "SELECT * FROM notifications ORDER BY id DESC LIMIT ?",
                (limit,),
            ).fetchall()
            return [dict(row) for row in rows]

    def list_critical_notifications(self, limit: int = 50) -> List[Dict]:
        with self.connection() as conn:
            rows = conn.execute(
                """
                SELECT * FROM notifications
                WHERE is_critical = 1
                ORDER BY id DESC
                LIMIT ?
                """,
                (limit,),
            ).fetchall()
            return [dict(row) for row in rows]

    def set_feedback(self, notification_id: int, is_important: bool) -> None:
        with self.connection() as conn:
            conn.execute(
                "UPDATE notifications SET feedback = ? WHERE id = ?",
                (int(is_important), notification_id),
            )

    def add_muted_app(self, app_name: str) -> None:
        with self.connection() as conn:
            conn.execute(
                "INSERT OR IGNORE INTO muted_apps (app_name) VALUES (?)",
                (app_name.strip(),),
            )

    def remove_muted_app(self, app_name: str) -> None:
        with self.connection() as conn:
            conn.execute(
                "DELETE FROM muted_apps WHERE app_name = ?",
                (app_name.strip(),),
            )

    def list_muted_apps(self) -> List[str]:
        with self.connection() as conn:
            rows = conn.execute(
                "SELECT app_name FROM muted_apps ORDER BY app_name ASC"
            ).fetchall()
            return [str(row["app_name"]) for row in rows]

    def load_keyword_weights(self) -> Dict[str, float]:
        with self.connection() as conn:
            rows = conn.execute(
                "SELECT keyword, weight FROM keyword_weights"
            ).fetchall()
            return {str(row["keyword"]): float(row["weight"]) for row in rows}

    def seed_keywords(self, keywords: Iterable[str], now_iso: str) -> None:
        with self.connection() as conn:
            for keyword in keywords:
                clean = keyword.strip().lower()
                if not clean:
                    continue
                conn.execute(
                    """
                    INSERT OR IGNORE INTO keyword_weights (keyword, weight, updated_at)
                    VALUES (?, ?, ?)
                    """,
                    (clean, 1.0, now_iso),
                )

    def upsert_keyword_weight(
        self,
        *,
        keyword: str,
        weight: float,
        positive_delta: int,
        negative_delta: int,
        now_iso: str,
    ) -> None:
        with self.connection() as conn:
            conn.execute(
                """
                INSERT INTO keyword_weights (keyword, weight, positive_hits, negative_hits, updated_at)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT(keyword) DO UPDATE SET
                    weight = excluded.weight,
                    positive_hits = positive_hits + excluded.positive_hits,
                    negative_hits = negative_hits + excluded.negative_hits,
                    updated_at = excluded.updated_at
                """,
                (keyword, weight, positive_delta, negative_delta, now_iso),
            )

    def get_stats(self) -> Dict[str, int]:
        with self.connection() as conn:
            total = conn.execute("SELECT COUNT(*) AS c FROM notifications").fetchone()["c"]
            critical = conn.execute(
                "SELECT COUNT(*) AS c FROM notifications WHERE is_critical = 1"
            ).fetchone()["c"]
            feedback = conn.execute(
                "SELECT COUNT(*) AS c FROM notifications WHERE feedback IS NOT NULL"
            ).fetchone()["c"]
            return {
                "total_notifications": int(total),
                "critical_notifications": int(critical),
                "feedback_count": int(feedback),
            }
