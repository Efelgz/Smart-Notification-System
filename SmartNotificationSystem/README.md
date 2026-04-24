# Smart Notification System - Mobile App

This project now includes a phone-ready web application with:

- Modern mobile-first interface
- FastAPI backend
- SQLite database persistence
- Notification processing with scoring
- Feedback-based model updates

## Run the app

1. Open terminal in the project root.
2. Install dependencies:

```powershell
cd "C:\Users\efelg\Desktop\software design patterns proje\SmartNotificationSystem"
"c:/Users/efelg/Desktop/software design patterns proje/.venv/Scripts/python.exe" -m pip install -r requirements.txt
```

3. Start server:

```powershell
"c:/Users/efelg/Desktop/software design patterns proje/.venv/Scripts/python.exe" run_server.py
```

4. Open in browser:

- Local desktop: `http://127.0.0.1:8000`
- Same Wi-Fi phone: `http://<your-computer-ip>:8000`

To find your local IP on Windows:

```powershell
ipconfig
```

Use the IPv4 address of your active adapter.

## API summary

- `GET /api/health`
- `GET /api/dashboard`
- `POST /api/notifications`
- `GET /api/notifications`
- `GET /api/muted-apps`
- `POST /api/muted-apps`
- `DELETE /api/muted-apps/{app_name}`
- `POST /api/feedback`
- `GET /api/model`

## Database

SQLite database file is created automatically at:

- `SmartNotificationSystem/smart_notifications.db`

Tables:

- `notifications`
- `muted_apps`
- `keyword_weights`
