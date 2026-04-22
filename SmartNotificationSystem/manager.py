from models import Notification
from ml_filter import MLFilter

class NotificationManager:
    def __init__(self):
        self.__notification_db = []  
        self.__ml_filter = MLFilter() 
        self.__muted_apps = []

    def add_muted_app(self, app_name: str):
        self.__muted_apps.append(app_name)

    def receive_notification(self, notification: Notification):
        if getattr(notification, f"_{notification.__class__.__name__}__platform", "") in self.__muted_apps:
            return

        is_critical = self.__ml_filter.analyze_content(notification.content)
        notification.set_critical_status(is_critical)
        self.__notification_db.append(notification)

    def get_dashboard_feed(self):
        print("\n--- KULLANICI EKRANI (SMART DASHBOARD) ---")
        critical_notifs = [n for n in self.__notification_db if n.is_critical]
        
        if not critical_notifs:
            print("Zihnin tamamen rahat! Hiç kritik bildirim yok.")
        else:
            for notif in critical_notifs:
                print(notif.display())
        print("------------------------------------------")