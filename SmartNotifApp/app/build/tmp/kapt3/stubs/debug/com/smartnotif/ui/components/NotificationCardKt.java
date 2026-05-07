package com.smartnotif.ui.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000D\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a,\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0010\b\u0002\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\fH\u0007\u001a\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000fH\u0007\u001a\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013\u001a\u0013\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\u0016\u001a\u0013\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\u0016\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"fmt", "Ljava/text/SimpleDateFormat;", "NotificationCard", "", "entity", "Lcom/smartnotif/core/NotificationEntity;", "onDelete", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "SourceBadge", "source", "Lcom/smartnotif/core/NotificationSource;", "StateBadge", "important", "", "formatTime", "", "ms", "", "sourceColor", "Landroidx/compose/ui/graphics/Color;", "(Lcom/smartnotif/core/NotificationSource;)J", "sourceColorLight", "app_debug"})
public final class NotificationCardKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.text.SimpleDateFormat fmt = null;
    
    public static final long sourceColor(@org.jetbrains.annotations.NotNull()
    com.smartnotif.core.NotificationSource source) {
        return 0L;
    }
    
    public static final long sourceColorLight(@org.jetbrains.annotations.NotNull()
    com.smartnotif.core.NotificationSource source) {
        return 0L;
    }
    
    @androidx.compose.runtime.Composable()
    public static final void NotificationCard(@org.jetbrains.annotations.NotNull()
    com.smartnotif.core.NotificationEntity entity, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDelete, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SourceBadge(@org.jetbrains.annotations.NotNull()
    com.smartnotif.core.NotificationSource source) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void StateBadge(boolean important) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String formatTime(long ms) {
        return null;
    }
}