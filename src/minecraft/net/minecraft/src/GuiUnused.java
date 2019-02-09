// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            GuiScreen

public class GuiUnused extends GuiScreen
{

    public void initGui()
    {
    }

    public void drawScreen(int i, int j, float f)
    {
        drawGradientRect(0, 0, width, height, 0xff402020, 0xff501010);
        drawCenteredString(fontRenderer, message1, width / 2, 90, 0xffffff);
        drawCenteredString(fontRenderer, message2, width / 2, 110, 0xffffff);
        super.drawScreen(i, j, f);
    }

    protected void keyTyped(char c, int i)
    {
    }

    private String message1;
    private String message2;
}
