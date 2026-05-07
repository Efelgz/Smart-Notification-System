package com.smartnotif.core;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002JF\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u00122\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0010J\u0006\u0010\u0016\u001a\u00020\u0017J\u0006\u0010\u0018\u001a\u00020\u0017R\u0011\u0010\u0003\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/smartnotif/core/FilterEngine;", "", "()V", "currentMode", "", "getCurrentMode", "()Ljava/lang/String;", "strategy", "Lcom/smartnotif/core/FilterStrategy;", "process", "Lcom/smartnotif/core/FilterResult;", "sender", "content", "source", "Lcom/smartnotif/core/NotificationSource;", "channelOn", "", "keywords", "", "threshold", "", "isGroup", "setFocusMode", "", "setNormalMode", "app_debug"})
public final class FilterEngine {
    @org.jetbrains.annotations.NotNull()
    private com.smartnotif.core.FilterStrategy strategy;
    
    public FilterEngine() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentMode() {
        return null;
    }
    
    public final void setNormalMode() {
    }
    
    public final void setFocusMode() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smartnotif.core.FilterResult process(@org.jetbrains.annotations.NotNull()
    java.lang.String sender, @org.jetbrains.annotations.NotNull()
    java.lang.String content, @org.jetbrains.annotations.NotNull()
    com.smartnotif.core.NotificationSource source, boolean channelOn, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> keywords, float threshold, boolean isGroup) {
        return null;
    }
}