// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiTextField, StringTranslate, GuiButton, 
//            ISaveFormat, WorldInfo

public class GuiRenameWorld extends GuiScreen
{

    public GuiRenameWorld(GuiScreen guiscreen, String s)
    {
        field_22112_a = guiscreen;
        field_22113_i = s;
    }

    public void updateScreen()
    {
        field_22114_h.updateCursorCounter();
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, stringtranslate.translateKey("selectWorld.renameButton")));
        controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, stringtranslate.translateKey("gui.cancel")));
        ISaveFormat isaveformat = mc.getSaveLoader();
        WorldInfo worldinfo = isaveformat.func_22173_b(field_22113_i);
        String s = worldinfo.getWorldName();
        field_22114_h = new GuiTextField(this, fontRenderer, width / 2 - 100, 60, 200, 20, s);
        field_22114_h.isFocused = true;
        field_22114_h.setMaxStringLength(32);
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        if(guibutton.id == 1)
        {
            mc.displayGuiScreen(field_22112_a);
        } else
        if(guibutton.id == 0)
        {
            ISaveFormat isaveformat = mc.getSaveLoader();
            isaveformat.func_22170_a(field_22113_i, field_22114_h.getText().trim());
            mc.displayGuiScreen(field_22112_a);
        }
    }

    protected void keyTyped(char c, int i)
    {
        field_22114_h.textboxKeyTyped(c, i);
        ((GuiButton)controlList.get(0)).enabled = field_22114_h.getText().trim().length() > 0;
        if(c == '\r')
        {
            actionPerformed((GuiButton)controlList.get(0));
        }
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        field_22114_h.mouseClicked(i, j, k);
    }

    public void drawScreen(int i, int j, float f)
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        drawDefaultBackground();
        drawCenteredString(fontRenderer, stringtranslate.translateKey("selectWorld.renameTitle"), width / 2, (height / 4 - 60) + 20, 0xffffff);
        drawString(fontRenderer, stringtranslate.translateKey("selectWorld.enterName"), width / 2 - 100, 47, 0xa0a0a0);
        field_22114_h.drawTextBox();
        super.drawScreen(i, j, f);
    }

    private GuiScreen field_22112_a;
    private GuiTextField field_22114_h;
    private final String field_22113_i;
}
