from models import EmailNotification, SocialMediaNotification
from manager import NotificationManager

def main():
    manager = NotificationManager()
    manager.add_muted_app("Instagram")

    notif1 = EmailNotification(1, "salih.hoca@university.edu", "Yarınki sunum için deadline öne çekildi, acil dönüş yapın.", "Proje Sunumu")
    notif2 = SocialMediaNotification(2, "Ahmet", "Akşam yemeğe gidelim mi?", "WhatsApp")
    notif3 = SocialMediaNotification(3, "MemeSayfasi", "Bu kedi videosuna inanamayacaksın!", "Instagram")
    notif4 = EmailNotification(4, "Steam", "İstek listenizdeki oyun indirime girdi.", "Haftasonu İndirimi")

    manager.receive_notification(notif1) 
    manager.receive_notification(notif2) 
    manager.receive_notification(notif3) 
    manager.receive_notification(notif4) 

    manager.get_dashboard_feed()

if __name__ == "__main__":
    main()