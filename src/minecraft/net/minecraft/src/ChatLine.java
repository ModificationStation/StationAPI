// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


public class ChatLine
{

    public ChatLine(String s)
    {
        message = s;
        updateCounter = 0;
    }

    public String message;
    public int updateCounter;
}
