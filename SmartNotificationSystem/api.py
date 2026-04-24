from __future__ import annotations

from pathlib import Path
from typing import Optional

from fastapi import FastAPI, HTTPException
from fastapi.responses import FileResponse
from fastapi.staticfiles import StaticFiles
from pydantic import BaseModel, Field

from notification_service import SmartNotificationService

BASE_DIR = Path(__file__).resolve().parent
STATIC_DIR = BASE_DIR / "static"
DB_PATH = BASE_DIR / "smart_notifications.db"

app = FastAPI(
    title="Smart Notification System API",
    version="1.0.0",
    description="Mobile-first notification ingestion, filtering, storage, and model feedback API.",
)

service = SmartNotificationService(db_path=str(DB_PATH))
app.mount("/static", StaticFiles(directory=STATIC_DIR), name="static")


class NotificationIn(BaseModel):
    sender: str = Field(min_length=1, max_length=120)
    content: str = Field(min_length=1, max_length=2000)
    source: str = Field(default="App", min_length=1, max_length=80)
    subject: Optional[str] = Field(default=None, max_length=200)
    platform: Optional[str] = Field(default=None, max_length=80)


class MutedAppIn(BaseModel):
    app_name: str = Field(min_length=1, max_length=80)


class FeedbackIn(BaseModel):
    notification_id: int = Field(ge=1)
    is_important: bool


@app.get("/")
def home() -> FileResponse:
    return FileResponse(STATIC_DIR / "index.html")


@app.get("/api/health")
def health() -> dict:
    return {"status": "ok"}


@app.get("/api/dashboard")
def dashboard() -> dict:
    return service.get_dashboard()


@app.post("/api/notifications")
def create_notification(payload: NotificationIn) -> dict:
    try:
        return service.receive_notification(payload.model_dump())
    except ValueError as exc:
        raise HTTPException(status_code=400, detail=str(exc)) from exc


@app.get("/api/notifications")
def list_notifications(limit: int = 100) -> dict:
    safe_limit = max(1, min(limit, 200))
    return {"items": service.list_notifications(limit=safe_limit)}


@app.post("/api/muted-apps")
def mute_app(payload: MutedAppIn) -> dict:
    try:
        muted = service.add_muted_app(payload.app_name)
        return {"muted_apps": muted}
    except ValueError as exc:
        raise HTTPException(status_code=400, detail=str(exc)) from exc


@app.delete("/api/muted-apps/{app_name}")
def unmute_app(app_name: str) -> dict:
    muted = service.remove_muted_app(app_name)
    return {"muted_apps": muted}


@app.get("/api/muted-apps")
def list_muted_apps() -> dict:
    return {"muted_apps": service.list_muted_apps()}


@app.post("/api/feedback")
def submit_feedback(payload: FeedbackIn) -> dict:
    try:
        return service.provide_feedback(
            notification_id=payload.notification_id,
            is_important=payload.is_important,
        )
    except ValueError as exc:
        raise HTTPException(status_code=404, detail=str(exc)) from exc


@app.get("/api/model")
def model_snapshot() -> dict:
    dashboard = service.get_dashboard()
    return {
        "keyword_weights": dashboard["keyword_weights"],
        "stats": dashboard["stats"],
    }
