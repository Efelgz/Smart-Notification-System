from __future__ import annotations

from datetime import datetime
from threading import Lock
from typing import Dict, List

from database import DEFAULT_KEYWORDS, NotificationDatabase
from ml_filter import MLFilter


class SmartNotificationService:
    def __init__(self, db_path: str = "smart_notifications.db"):
        self._lock = Lock()
        self._db = NotificationDatabase(db_path)
        self._filter = MLFilter(initial_keywords=DEFAULT_KEYWORDS, threshold=1.2)

        now_iso = datetime.utcnow().isoformat(timespec="seconds")
        self._db.seed_keywords(DEFAULT_KEYWORDS, now_iso)
        db_weights = self._db.load_keyword_weights()
        if db_weights:
            self._filter.set_keyword_weights(db_weights)

    def receive_notification(self, payload: Dict) -> Dict:
        sender = str(payload.get("sender", "Unknown")).strip() or "Unknown"
        content = str(payload.get("content", "")).strip()
        source = str(payload.get("source", "App")).strip() or "App"
        subject = payload.get("subject")
        platform = str(payload.get("platform") or source).strip() or source

        if not content:
            raise ValueError("content is required")

        muted_apps = {item.lower() for item in self._db.list_muted_apps()}
        if platform.lower() in muted_apps:
            return {
                "stored": False,
                "reason": f"{platform} is muted",
                "platform": platform,
            }

        is_critical, score, matched_keywords = self._filter.analyze_content_details(content)
        received_at = datetime.utcnow().isoformat(timespec="seconds")

        notification_id = self._db.insert_notification(
            sender=sender,
            content=content,
            source=source,
            subject=str(subject).strip() if subject else None,
            platform=platform,
            received_at=received_at,
            score=score,
            is_critical=is_critical,
        )

        created = self._db.get_notification(notification_id)
        created["matched_keywords"] = matched_keywords
        created["stored"] = True
        return self._serialize_notification(created)

    def list_notifications(self, limit: int = 100) -> List[Dict]:
        rows = self._db.list_notifications(limit=limit)
        return [self._serialize_notification(item) for item in rows]

    def list_critical_notifications(self, limit: int = 50) -> List[Dict]:
        rows = self._db.list_critical_notifications(limit=limit)
        return [self._serialize_notification(item) for item in rows]

    def add_muted_app(self, app_name: str) -> List[str]:
        if not app_name.strip():
            raise ValueError("app_name is required")
        self._db.add_muted_app(app_name)
        return self._db.list_muted_apps()

    def remove_muted_app(self, app_name: str) -> List[str]:
        self._db.remove_muted_app(app_name)
        return self._db.list_muted_apps()

    def list_muted_apps(self) -> List[str]:
        return self._db.list_muted_apps()

    def provide_feedback(self, notification_id: int, is_important: bool) -> Dict:
        with self._lock:
            row = self._db.get_notification(notification_id)
            if not row:
                raise ValueError("notification not found")

            adjusted_keywords = self._filter.update_model(
                row["content"],
                is_important=is_important,
                learning_rate=0.2,
            )
            weights = self._filter.get_keyword_weights()
            now_iso = datetime.utcnow().isoformat(timespec="seconds")

            for keyword in adjusted_keywords:
                self._db.upsert_keyword_weight(
                    keyword=keyword,
                    weight=weights[keyword],
                    positive_delta=1 if is_important else 0,
                    negative_delta=0 if is_important else 1,
                    now_iso=now_iso,
                )

            self._db.set_feedback(notification_id, is_important)
            updated_row = self._db.get_notification(notification_id)

        result = self._serialize_notification(updated_row)
        result["adjusted_keywords"] = adjusted_keywords
        result["model_updated"] = True
        return result

    def get_dashboard(self) -> Dict:
        return {
            "stats": self._db.get_stats(),
            "critical_feed": self.list_critical_notifications(limit=30),
            "latest_notifications": self.list_notifications(limit=30),
            "muted_apps": self.list_muted_apps(),
            "keyword_weights": self._filter.top_keywords(limit=15),
        }

    @staticmethod
    def _serialize_notification(raw: Dict) -> Dict:
        return {
            "id": int(raw["id"]),
            "sender": raw["sender"],
            "content": raw["content"],
            "source": raw["source"],
            "subject": raw.get("subject"),
            "platform": raw.get("platform"),
            "received_at": raw["received_at"],
            "score": float(raw["score"]),
            "is_critical": bool(raw["is_critical"]),
            "feedback": None if raw.get("feedback") is None else bool(raw["feedback"]),
            "matched_keywords": raw.get("matched_keywords", []),
            "stored": raw.get("stored", True),
        }
