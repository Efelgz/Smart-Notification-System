package com.smartnotif.core;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J>\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00060\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u0006X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\u0015"}, d2 = {"Lcom/smartnotif/core/FocusStrategy;", "Lcom/smartnotif/core/FilterStrategy;", "aiEngine", "Lcom/smartnotif/nlp/AIEngine;", "(Lcom/smartnotif/nlp/AIEngine;)V", "modeName", "", "getModeName", "()Ljava/lang/String;", "filter", "Lcom/smartnotif/core/FilterResult;", "sender", "content", "source", "Lcom/smartnotif/core/NotificationSource;", "keywords", "", "threshold", "", "isGroup", "", "app_debug"})
public final class FocusStrategy implements com.smartnotif.core.FilterStrategy {
    @org.jetbrains.annotations.NotNull()
    private final com.smartnotif.nlp.AIEngine aiEngine = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String modeName = "Focus Mode";
    
    public FocusStrategy(@org.jetbrains.annotations.NotNull()
    com.smartnotif.nlp.AIEngine aiEngine) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String getModeName() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.smartnotif.core.FilterResult filter(@org.jetbrains.annotations.NotNull()
    java.lang.String sender, @org.jetbrains.annotations.NotNull()
    java.lang.String content, @org.jetbrains.annotations.NotNull()
    com.smartnotif.core.NotificationSource source, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> keywords, float threshold, boolean isGroup) {
        return null;
    }
    
    public FocusStrategy() {
        super();
    }
}