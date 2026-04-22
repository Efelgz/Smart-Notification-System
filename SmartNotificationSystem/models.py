from abc import ABC, abstractmethod
from datetime import datetime

class Notification(ABC):
    def __init__(self, notif_id: int, sender: str, content: str):
        self._notif_id = notif_id          
        self._sender = sender
        self._content = content
        self._timestamp = datetime.now()
        self._is_critical = False          
        self._is_processed = False

    @property
    def content(self):
        return self._content

    @property
    def is_critical(self):
        return self._is_critical

    def set_critical_status(self, status: bool):
        self._is_critical = status

    @abstractmethod
    def display(self):
        pass

class EmailNotification(Notification):
    def __init__(self, notif_id: int, sender: str, content: str, subject: str):
        super().__init__(notif_id, sender, content)
        self.__subject = subject  
    
    def display(self):
        status = "🔴 KRİTİK" if self._is_critical else "⚪ Gürültü"
        return f"[{status}] E-Posta | Kimden: {self._sender} | Konu: {self.__subject}"

class SocialMediaNotification(Notification):
    def __init__(self, notif_id: int, sender: str, content: str, platform: str):
        super().__init__(notif_id, sender, content)
        self.__platform = platform
        
    def display(self):
        status = "🔴 KRİTİK" if self._is_critical else "⚪ Gürültü"
        return f"[{status}] {self.__platform} | Kimden: {self._sender} | İçerik: {self._content[:20]}..."