// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.awt.Canvas;
import net.minecraft.client.MinecraftApplet;

public class CanvasMinecraftApplet extends Canvas
{

    public CanvasMinecraftApplet(MinecraftApplet minecraftapplet)
    {
//        super();
        mcApplet = minecraftapplet;
    }

    public synchronized void addNotify()
    {
        super.addNotify();
        mcApplet.startMainThread();
    }

    public synchronized void removeNotify()
    {
        mcApplet.shutdown();
        super.removeNotify();
    }

    final MinecraftApplet mcApplet; /* synthetic field */
}
