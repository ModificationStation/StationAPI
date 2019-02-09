// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.isom;

import java.applet.Applet;
import java.awt.BorderLayout;
import net.minecraft.src.CanvasIsomPreview;

public class IsomPreviewApplet extends Applet
{

    public IsomPreviewApplet()
    {
        a = new CanvasIsomPreview();
        setLayout(new BorderLayout());
        add(a, "Center");
    }

    public void start()
    {
        a.func_1272_b();
    }

    public void stop()
    {
        a.exit();
    }

    private CanvasIsomPreview a;
}
