// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


public class UnexpectedThrowable
{

    public UnexpectedThrowable(String s, Throwable throwable)
    {
        description = s;
        exception = throwable;
    }

    public final String description;
    public final Throwable exception;
}
