// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, GuiMainMenu

public class GuiConflictWarning extends GuiScreen
{

    public GuiConflictWarning()
    {
        updateCounter = 0;
    }

    public void updateScreen()
    {
        updateCounter++;
    }

    public void initGui()
    {
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, "Back to title screen"));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        if(guibutton.id == 0)
        {
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Level save conflict", width / 2, (height / 4 - 60) + 20, 0xffffff);
        drawString(fontRenderer, "Minecraft detected a conflict in the level save data.", width / 2 - 140, (height / 4 - 60) + 60 + 0, 0xa0a0a0);
        drawString(fontRenderer, "This could be caused by two copies of the game", width / 2 - 140, (height / 4 - 60) + 60 + 18, 0xa0a0a0);
        drawString(fontRenderer, "accessing the same level.", width / 2 - 140, (height / 4 - 60) + 60 + 27, 0xa0a0a0);
        drawString(fontRenderer, "To prevent level corruption, the current game has quit.", width / 2 - 140, (height / 4 - 60) + 60 + 45, 0xa0a0a0);
        super.drawScreen(i, j, f);
    }

    private int updateCounter;
}
