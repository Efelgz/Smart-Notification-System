package com.smartnotif.core;

/**
 * SmartNotificationListenerService
 * ─────────────────────────────────
 * Hooks into the Android OS notification stream (Observer Pattern).
 * Every time an app posts a notification, onNotificationPosted() fires.
 *
 * Requires the user to grant "Notification Access" in system settings.
 * The app prompts them to do this on first launch.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\r\u001a\u00020\fH\u0016J\u0010\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0018\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/smartnotif/core/SmartNotificationListenerService;", "Landroid/service/notification/NotificationListenerService;", "()V", "db", "Lcom/smartnotif/core/NotificationDatabase;", "filterEngine", "Lcom/smartnotif/core/FilterEngine;", "prefs", "Lcom/smartnotif/core/UserPreferencesRepository;", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "onCreate", "", "onDestroy", "onNotificationPosted", "sbn", "Landroid/service/notification/StatusBarNotification;", "triggerAlert", "title", "", "content", "app_debug"})
public final class SmartNotificationListenerService extends android.service.notification.NotificationListenerService {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.NotNull()
    private final com.smartnotif.core.FilterEngine filterEngine = null;
    private com.smartnotif.core.NotificationDatabase db;
    private com.smartnotif.core.UserPreferencesRepository prefs;
    
    public SmartNotificationListenerService() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    public void onNotificationPosted(@org.jetbrains.annotations.NotNull()
    android.service.notification.StatusBarNotification sbn) {
    }
    
    private final void triggerAlert(java.lang.String title, java.lang.String content) {
    }
}