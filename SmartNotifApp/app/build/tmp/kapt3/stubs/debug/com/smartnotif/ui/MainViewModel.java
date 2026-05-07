package com.smartnotif.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cJ\u001e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020!J\u0006\u0010#\u001a\u00020\u001aJ\u000e\u0010$\u001a\u00020\u001a2\u0006\u0010%\u001a\u00020&J\u000e\u0010\'\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cJ\u000e\u0010(\u001a\u00020\u001a2\u0006\u0010)\u001a\u00020*J\u0016\u0010+\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020!J\u000e\u0010/\u001a\u00020\u001a2\u0006\u0010.\u001a\u00020!R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000bR\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u000b\u00a8\u00060"}, d2 = {"Lcom/smartnotif/ui/MainViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "aiEngine", "Lcom/smartnotif/nlp/AIEngine;", "dashboardStats", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/smartnotif/ui/DashboardStats;", "getDashboardStats", "()Lkotlinx/coroutines/flow/StateFlow;", "db", "Lcom/smartnotif/core/NotificationDatabase;", "filterEngine", "Lcom/smartnotif/core/FilterEngine;", "notificationsFlow", "", "Lcom/smartnotif/core/NotificationEntity;", "getNotificationsFlow", "prefs", "Lcom/smartnotif/core/UserPreferencesRepository;", "prefsFlow", "Lcom/smartnotif/core/UserPreferencesRepository$Prefs;", "getPrefsFlow", "addKeyword", "Lkotlinx/coroutines/Job;", "kw", "", "analyzeText", "Lcom/smartnotif/nlp/AIEngine$ScoreBreakdown;", "text", "isShortcode", "", "isGroup", "clearHistory", "deleteNotification", "id", "", "removeKeyword", "setThreshold", "value", "", "toggleChannel", "source", "Lcom/smartnotif/core/NotificationSource;", "on", "toggleFocusMode", "app_debug"})
public final class MainViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.smartnotif.core.NotificationDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.smartnotif.core.UserPreferencesRepository prefs = null;
    @org.jetbrains.annotations.NotNull()
    private final com.smartnotif.core.FilterEngine filterEngine = null;
    @org.jetbrains.annotations.NotNull()
    private final com.smartnotif.nlp.AIEngine aiEngine = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.smartnotif.core.UserPreferencesRepository.Prefs> prefsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.smartnotif.core.NotificationEntity>> notificationsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.smartnotif.ui.DashboardStats> dashboardStats = null;
    
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.smartnotif.core.UserPreferencesRepository.Prefs> getPrefsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.smartnotif.core.NotificationEntity>> getNotificationsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.smartnotif.ui.DashboardStats> getDashboardStats() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job toggleFocusMode(boolean on) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job toggleChannel(@org.jetbrains.annotations.NotNull()
    com.smartnotif.core.NotificationSource source, boolean on) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job setThreshold(float value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job addKeyword(@org.jetbrains.annotations.NotNull()
    java.lang.String kw) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job removeKeyword(@org.jetbrains.annotations.NotNull()
    java.lang.String kw) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job clearHistory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job deleteNotification(long id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smartnotif.nlp.AIEngine.ScoreBreakdown analyzeText(@org.jetbrains.annotations.NotNull()
    java.lang.String text, boolean isShortcode, boolean isGroup) {
        return null;
    }
}