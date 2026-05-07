package com.smartnotif.core;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\b\u0086\u0081\u0002\u0018\u0000 \u000e2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u000eB\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007j\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\r\u00a8\u0006\u000f"}, d2 = {"Lcom/smartnotif/core/NotificationSource;", "", "displayName", "", "emoji", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V", "getDisplayName", "()Ljava/lang/String;", "getEmoji", "SMS", "EMAIL", "WHATSAPP", "INSTAGRAM", "OTHER", "Companion", "app_debug"})
public enum NotificationSource {
    /*public static final*/ SMS /* = new SMS(null, null) */,
    /*public static final*/ EMAIL /* = new EMAIL(null, null) */,
    /*public static final*/ WHATSAPP /* = new WHATSAPP(null, null) */,
    /*public static final*/ INSTAGRAM /* = new INSTAGRAM(null, null) */,
    /*public static final*/ OTHER /* = new OTHER(null, null) */;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String emoji = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.smartnotif.core.NotificationSource.Companion Companion = null;
    
    NotificationSource(java.lang.String displayName, java.lang.String emoji) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDisplayName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmoji() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.smartnotif.core.NotificationSource> getEntries() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/smartnotif/core/NotificationSource$Companion;", "", "()V", "fromPackage", "Lcom/smartnotif/core/NotificationSource;", "pkg", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.smartnotif.core.NotificationSource fromPackage(@org.jetbrains.annotations.NotNull()
        java.lang.String pkg) {
            return null;
        }
    }
}