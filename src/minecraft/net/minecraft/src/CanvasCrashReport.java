// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.awt.Canvas;
import java.awt.Dimension;

class CanvasCrashReport extends Canvas
{

    public CanvasCrashReport(int i)
    {
        setPreferredSize(new Dimension(i, i));
        setMinimumSize(new Dimension(i, i));
    }
}
