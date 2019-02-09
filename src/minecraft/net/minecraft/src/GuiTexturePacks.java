// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.File;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, StringTranslate, GuiSmallButton, TexturePackList, 
//            GuiTexturePackSlot, GuiButton, RenderEngine, FontRenderer

public class GuiTexturePacks extends GuiScreen
{

    public GuiTexturePacks(GuiScreen guiscreen)
    {
        field_6454_o = -1;
        fileLocation = "";
        guiScreen = guiscreen;
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.add(new GuiSmallButton(5, width / 2 - 154, height - 48, stringtranslate.translateKey("texturePack.openFolder")));
        controlList.add(new GuiSmallButton(6, width / 2 + 4, height - 48, stringtranslate.translateKey("gui.done")));
        mc.texturePackList.updateAvaliableTexturePacks();
        fileLocation = (new File(Minecraft.getMinecraftDir(), "texturepacks")).getAbsolutePath();
        guiTexturePackSlot = new GuiTexturePackSlot(this);
        guiTexturePackSlot.registerScrollButtons(controlList, 7, 8);
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        if(guibutton.id == 5)
        {
            Sys.openURL((new StringBuilder()).append("file://").append(fileLocation).toString());
        } else
        if(guibutton.id == 6)
        {
            mc.renderEngine.refreshTextures();
            mc.displayGuiScreen(guiScreen);
        } else
        {
            guiTexturePackSlot.actionPerformed(guibutton);
        }
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
    }

    protected void mouseMovedOrUp(int i, int j, int k)
    {
        super.mouseMovedOrUp(i, j, k);
    }

    public void drawScreen(int i, int j, float f)
    {
        guiTexturePackSlot.drawScreen(i, j, f);
        if(field_6454_o <= 0)
        {
            mc.texturePackList.updateAvaliableTexturePacks();
            field_6454_o += 20;
        }
        StringTranslate stringtranslate = StringTranslate.getInstance();
        drawCenteredString(fontRenderer, stringtranslate.translateKey("texturePack.title"), width / 2, 16, 0xffffff);
        drawCenteredString(fontRenderer, stringtranslate.translateKey("texturePack.folderInfo"), width / 2 - 77, height - 26, 0x808080);
        super.drawScreen(i, j, f);
    }

    public void updateScreen()
    {
        super.updateScreen();
        field_6454_o--;
    }

    static Minecraft func_22124_a(GuiTexturePacks guitexturepacks)
    {
        return guitexturepacks.mc;
    }

    static Minecraft func_22126_b(GuiTexturePacks guitexturepacks)
    {
        return guitexturepacks.mc;
    }

    static Minecraft func_22119_c(GuiTexturePacks guitexturepacks)
    {
        return guitexturepacks.mc;
    }

    static Minecraft func_22122_d(GuiTexturePacks guitexturepacks)
    {
        return guitexturepacks.mc;
    }

    static Minecraft func_22117_e(GuiTexturePacks guitexturepacks)
    {
        return guitexturepacks.mc;
    }

    static Minecraft func_22118_f(GuiTexturePacks guitexturepacks)
    {
        return guitexturepacks.mc;
    }

    static Minecraft func_22116_g(GuiTexturePacks guitexturepacks)
    {
        return guitexturepacks.mc;
    }

    static Minecraft func_22121_h(GuiTexturePacks guitexturepacks)
    {
        return guitexturepacks.mc;
    }

    static Minecraft func_22123_i(GuiTexturePacks guitexturepacks)
    {
        return guitexturepacks.mc;
    }

    static FontRenderer func_22127_j(GuiTexturePacks guitexturepacks)
    {
        return guitexturepacks.fontRenderer;
    }

    static FontRenderer func_22120_k(GuiTexturePacks guitexturepacks)
    {
        return guitexturepacks.fontRenderer;
    }

    static FontRenderer func_22125_l(GuiTexturePacks guitexturepacks)
    {
        return guitexturepacks.fontRenderer;
    }

    protected GuiScreen guiScreen;
    private int field_6454_o;
    private String fileLocation;
    private GuiTexturePackSlot guiTexturePackSlot;
}
