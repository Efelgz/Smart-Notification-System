package com.smartnotif.core;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J@\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00030\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H&R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\u00a8\u0006\u0012"}, d2 = {"Lcom/smartnotif/core/FilterStrategy;", "", "modeName", "", "getModeName", "()Ljava/lang/String;", "filter", "Lcom/smartnotif/core/FilterResult;", "sender", "content", "source", "Lcom/smartnotif/core/NotificationSource;", "keywords", "", "threshold", "", "isGroup", "", "app_debug"})
public abstract interface FilterStrategy {
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getModeName();
    
    /**
     * Returns importance score 0..1 and sets entity fields.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract com.smartnotif.core.FilterResult filter(@org.jetbrains.annotations.NotNull()
    java.lang.String sender, @org.jetbrains.annotations.NotNull()
    java.lang.String content, @org.jetbrains.annotations.NotNull()
    com.smartnotif.core.NotificationSource source, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> keywords, float threshold, boolean isGroup);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}