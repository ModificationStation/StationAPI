// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiTextField, StringTranslate, GuiButton, 
//            ChatAllowedCharacters, MathHelper, ISaveFormat, PlayerControllerSP

public class GuiCreateWorld extends GuiScreen
{

    public GuiCreateWorld(GuiScreen guiscreen)
    {
        field_22131_a = guiscreen;
    }

    public void updateScreen()
    {
        textboxWorldName.updateCursorCounter();
        textboxSeed.updateCursorCounter();
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, stringtranslate.translateKey("selectWorld.create")));
        controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, stringtranslate.translateKey("gui.cancel")));
        textboxWorldName = new GuiTextField(this, fontRenderer, width / 2 - 100, 60, 200, 20, stringtranslate.translateKey("selectWorld.newWorld"));
        textboxWorldName.isFocused = true;
        textboxWorldName.setMaxStringLength(32);
        textboxSeed = new GuiTextField(this, fontRenderer, width / 2 - 100, 116, 200, 20, "");
        func_22129_j();
    }

    private void func_22129_j()
    {
        folderName = textboxWorldName.getText().trim();
        char ac[] = ChatAllowedCharacters.allowedCharactersArray;
        int i = ac.length;
        for(int j = 0; j < i; j++)
        {
            char c = ac[j];
            folderName = folderName.replace(c, '_');
        }

        if(MathHelper.stringNullOrLengthZero(folderName))
        {
            folderName = "World";
        }
        folderName = generateUnusedFolderName(mc.getSaveLoader(), folderName);
    }

    public static String generateUnusedFolderName(ISaveFormat isaveformat, String s)
    {
        for(; isaveformat.func_22173_b(s) != null; s = (new StringBuilder()).append(s).append("-").toString()) { }
        return s;
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
            mc.displayGuiScreen(field_22131_a);
        } else
        if(guibutton.id == 0)
        {
            mc.displayGuiScreen(null);
            if(createClicked)
            {
                return;
            }
            createClicked = true;
            long l = (new Random()).nextLong();
            String s = textboxSeed.getText();
            if(!MathHelper.stringNullOrLengthZero(s))
            {
                try
                {
                    long l1 = Long.parseLong(s);
                    if(l1 != 0L)
                    {
                        l = l1;
                    }
                }
                catch(NumberFormatException numberformatexception)
                {
                    l = s.hashCode();
                }
            }
            mc.playerController = new PlayerControllerSP(mc);
            mc.startWorld(folderName, textboxWorldName.getText(), l);
            mc.displayGuiScreen(null);
        }
    }

    protected void keyTyped(char c, int i)
    {
        if(textboxWorldName.isFocused)
        {
            textboxWorldName.textboxKeyTyped(c, i);
        } else
        {
            textboxSeed.textboxKeyTyped(c, i);
        }
        if(c == '\r')
        {
            actionPerformed((GuiButton)controlList.get(0));
        }
        ((GuiButton)controlList.get(0)).enabled = textboxWorldName.getText().length() > 0;
        func_22129_j();
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        textboxWorldName.mouseClicked(i, j, k);
        textboxSeed.mouseClicked(i, j, k);
    }

    public void drawScreen(int i, int j, float f)
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        drawDefaultBackground();
        drawCenteredString(fontRenderer, stringtranslate.translateKey("selectWorld.create"), width / 2, (height / 4 - 60) + 20, 0xffffff);
        drawString(fontRenderer, stringtranslate.translateKey("selectWorld.enterName"), width / 2 - 100, 47, 0xa0a0a0);
        drawString(fontRenderer, (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.resultFolder")).append(" ").append(folderName).toString(), width / 2 - 100, 85, 0xa0a0a0);
        drawString(fontRenderer, stringtranslate.translateKey("selectWorld.enterSeed"), width / 2 - 100, 104, 0xa0a0a0);
        drawString(fontRenderer, stringtranslate.translateKey("selectWorld.seedInfo"), width / 2 - 100, 140, 0xa0a0a0);
        textboxWorldName.drawTextBox();
        textboxSeed.drawTextBox();
        super.drawScreen(i, j, f);
    }

    public void selectNextField()
    {
        if(textboxWorldName.isFocused)
        {
            textboxWorldName.setFocused(false);
            textboxSeed.setFocused(true);
        } else
        {
            textboxWorldName.setFocused(true);
            textboxSeed.setFocused(false);
        }
    }

    private GuiScreen field_22131_a;
    private GuiTextField textboxWorldName;
    private GuiTextField textboxSeed;
    private String folderName;
    private boolean createClicked;
}
