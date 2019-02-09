// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;

// Referenced classes of package net.minecraft.src:
//            PanelCrashReport, UnexpectedThrowable

public final class MinecraftImpl extends Minecraft
{

    public MinecraftImpl(Component component, Canvas canvas, MinecraftApplet minecraftapplet, int i, int j, boolean flag, Frame frame)
    {
        super(component, canvas, minecraftapplet, i, j, flag);
        mcFrame = frame;
    }

    public void displayUnexpectedThrowable(UnexpectedThrowable unexpectedthrowable)
    {
        mcFrame.removeAll();
        mcFrame.add(new PanelCrashReport(unexpectedthrowable), "Center");
        mcFrame.validate();
    }

    final Frame mcFrame; /* synthetic field */
}
