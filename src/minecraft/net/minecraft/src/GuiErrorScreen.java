// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton

public class GuiErrorScreen extends GuiScreen
{

    public GuiErrorScreen()
    {
        field_28098_a = 0;
    }

    public void updateScreen()
    {
        field_28098_a++;
    }

    public void initGui()
    {
    }

    protected void actionPerformed(GuiButton guibutton)
    {
    }

    protected void keyTyped(char c, int i)
    {
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Out of memory!", width / 2, (height / 4 - 60) + 20, 0xffffff);
        drawString(fontRenderer, "Minecraft has run out of memory.", width / 2 - 140, (height / 4 - 60) + 60 + 0, 0xa0a0a0);
        drawString(fontRenderer, "This could be caused by a bug in the game or by the", width / 2 - 140, (height / 4 - 60) + 60 + 18, 0xa0a0a0);
        drawString(fontRenderer, "Java Virtual Machine not being allocated enough", width / 2 - 140, (height / 4 - 60) + 60 + 27, 0xa0a0a0);
        drawString(fontRenderer, "memory. If you are playing in a web browser, try", width / 2 - 140, (height / 4 - 60) + 60 + 36, 0xa0a0a0);
        drawString(fontRenderer, "downloading the game and playing it offline.", width / 2 - 140, (height / 4 - 60) + 60 + 45, 0xa0a0a0);
        drawString(fontRenderer, "To prevent level corruption, the current game has quit.", width / 2 - 140, (height / 4 - 60) + 60 + 63, 0xa0a0a0);
        drawString(fontRenderer, "Please restart the game.", width / 2 - 140, (height / 4 - 60) + 60 + 81, 0xa0a0a0);
        super.drawScreen(i, j, f);
    }

    private int field_28098_a;
}
