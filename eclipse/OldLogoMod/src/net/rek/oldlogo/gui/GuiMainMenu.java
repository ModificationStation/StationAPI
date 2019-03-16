// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.rek.oldlogo.gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMultiplayer;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSelectWorld;
import net.minecraft.src.GuiTexturePacks;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.Tessellator;
import net.modificationstation.stationloader.events.client.gui.guiscreen.DrawScreen;
import net.rek.oldlogo.gui.render.RenderBlocksLogoFunc;
import net.rek.oldlogo.gui.util.LogoConfig;
import net.rek.oldlogo.gui.util.LogoEffectRandomizer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, mod_Test1a, LogoEffectRandomizer, StringTranslate, 
//            GuiButton, GuiOptions, GuiSelectWorld, GuiMultiplayer, 
//            GuiTexturePacks, Tessellator, RenderEngine, MathHelper, 
//            FontRenderer, ScaledResolution, RenderBlocksLogoFunc

public class GuiMainMenu extends net.minecraft.src.GuiMainMenu
{

    public GuiMainMenu()
    {
        minecraftLogo = LogoConfig.getLogo();
        updateCounter = 0.0F;
        splashText = "missingno";
        try
        {
            ArrayList arraylist = new ArrayList();
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader((net.minecraft.src.GuiMainMenu.class).getResourceAsStream("/title/splashes.txt"), Charset.forName("UTF-8")));
            String s = "";
            do
            {
                String s1;
                if((s1 = bufferedreader.readLine()) == null)
                {
                    break;
                }
                s1 = s1.trim();
                if(s1.length() > 0)
                {
                    arraylist.add(s1);
                }
            } while(true);
            splashText = (String)arraylist.get(rand.nextInt(arraylist.size()));
        }
        catch(Exception exception) { }
    }

    public void updateScreen()
    {
        updateCounter++;
        if(logoEffects != null)
        {
            for(int i = 0; i < logoEffects.length; i++)
            {
                for(int j = 0; j < logoEffects[i].length; j++)
                {
                    logoEffects[i][j].func_875_a();
                }

            }

        }
    }

    protected void keyTyped(char c, int i)
    {
    }

    public void initGui()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if(calendar.get(2) + 1 == 11 && calendar.get(5) == 9)
        {
            splashText = "Happy birthday, ez!";
        } else
        if(calendar.get(2) + 1 == 6 && calendar.get(5) == 1)
        {
            splashText = "Happy birthday, Notch!";
        } else
        if(calendar.get(2) + 1 == 12 && calendar.get(5) == 24)
        {
            splashText = "Merry X-mas!";
        } else
        if(calendar.get(2) + 1 == 1 && calendar.get(5) == 1)
        {
            splashText = "Happy new year!";
        }
        StringTranslate stringtranslate = StringTranslate.getInstance();
        int i = height / 4 + 48;
        controlList.add(new GuiButton(1, width / 2 - 100, i, stringtranslate.translateKey("menu.singleplayer")));
        controlList.add(multiplayerButton = new GuiButton(2, width / 2 - 100, i + 24, stringtranslate.translateKey("menu.multiplayer")));
        controlList.add(new GuiButton(3, width / 2 - 100, i + 48, stringtranslate.translateKey("menu.mods")));
        if(mc.hideQuitButton)
        {
            controlList.add(new GuiButton(0, width / 2 - 100, i + 72, stringtranslate.translateKey("menu.options")));
        } else
        {
            controlList.add(new GuiButton(0, width / 2 - 100, i + 72 + 12, 98, 20, stringtranslate.translateKey("menu.options")));
            controlList.add(new GuiButton(4, width / 2 + 2, i + 72 + 12, 98, 20, stringtranslate.translateKey("menu.quit")));
        }
        if(mc.session == null)
        {
            multiplayerButton.enabled = false;
        }
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 0)
        {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        }
        if(guibutton.id == 1)
        {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if(guibutton.id == 2)
        {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if(guibutton.id == 3)
        {
            mc.displayGuiScreen(new GuiTexturePacks(this));
        }
        if(guibutton.id == 4)
        {
            mc.shutdown();
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
        Tessellator tessellator = Tessellator.instance;
        if (LogoConfig.enabled()) {
        	drawLogo(f);
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/gui/logo.png"));
        }
        else 
        {
        	char c = '\u0112';
            int k = width / 2 - c / 2;
            byte byte0 = 30;
        	GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/title/mclogo.png"));
        	drawTexturedModalRect(k + 0, byte0 + 0, 0, 0, 155, 44);
            drawTexturedModalRect(k + 155, byte0 + 0, 0, 45, 155, 44);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.setColorOpaque_I(0xffffff);
        GL11.glPushMatrix();
        GL11.glTranslatef(width / 2 + 90, 70F, 0.0F);
        GL11.glRotatef(-20F, 0.0F, 0.0F, 1.0F);
        float f1 = 1.8F - MathHelper.abs(MathHelper.sin(((float)(System.currentTimeMillis() % 1000L) / 1000F) * 3.141593F * 2.0F) * 0.1F);
        f1 = (f1 * 100F) / (float)(fontRenderer.getStringWidth(splashText) + 32);
        GL11.glScalef(f1, f1, f1);
        drawCenteredString(fontRenderer, splashText, 0, -8, 0xffff00);
        GL11.glPopMatrix();
        drawString(fontRenderer, "Minecraft Beta 1.7.3", 2, 2, 0x505050);
        String s = "Copyright Mojang AB. Do not distribute.";
        drawString(fontRenderer, s, width - fontRenderer.getStringWidth(s) - 2, height - 10, 0xffffff);
        /** v StationLoader v*/
        if (DrawScreen.EVENT.getInvoker().drawScreen(this, i, j, f, "GuiScreen"))
        /** ^ StationLoader ^*/
        {
            for(int k = 0; k < controlList.size(); k++)
            {
                GuiButton guibutton = (GuiButton)controlList.get(k);
                guibutton.drawButton(mc, i, j);
            }
        }
    }

    private void drawLogo(float f)
    {
        if(logoEffects == null)
        {
            logoEffects = new LogoEffectRandomizer[LogoConfig.getLogoWidth()][minecraftLogo.length];
            for(int i = 0; i < logoEffects.length; i++)
            {
                for(int j = 0; j < logoEffects[i].length; j++)
                {
                    logoEffects[i][j] = new LogoEffectRandomizer(this, i, j);
                }

            }

        }
        GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int k = 120 * scaledresolution.scaleFactor;
        GLU.gluPerspective(70F, (float)mc.displayWidth / (float)k, 0.05F, 100F);
        GL11.glViewport(0, mc.displayHeight - k, mc.displayWidth, k);
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glDisable(2884 /*GL_CULL_FACE*/);
        GL11.glCullFace(1029 /*GL_BACK*/);
        GL11.glDepthMask(true);
        RenderBlocksLogoFunc renderblockslogofunc = new RenderBlocksLogoFunc();
        for(int l = 0; l < 3; l++)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.4F, 0.6F, -13F);
            if(l == 0)
            {
                GL11.glClear(256);
                GL11.glTranslatef(0.0F, -0.4F, 0.0F);
                GL11.glScalef(0.98F, 1.0F, 1.0F);
                GL11.glEnable(3042 /*GL_BLEND*/);
                GL11.glBlendFunc(770, 771);
            }
            if(l == 1)
            {
                GL11.glDisable(3042 /*GL_BLEND*/);
                GL11.glClear(256);
            }
            if(l == 2)
            {
                GL11.glEnable(3042 /*GL_BLEND*/);
                GL11.glBlendFunc(768, 1);
            }
            GL11.glScalef(1.0F, -1F, 1.0F);
            GL11.glRotatef(15F, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(0.89F, 1.0F, 0.4F);
            GL11.glTranslatef((float)(-LogoConfig.getLogoWidth()) * 0.5F, (float)(-minecraftLogo.length) * 0.5F, 0.0F);
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/terrain.png"));
            if(l == 0)
            {
                GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/title/black.png"));
            }
            for(int i1 = 0; i1 < minecraftLogo.length; i1++)
            {
                for(int j1 = 0; j1 < minecraftLogo[i1].length(); j1++)
                {
                    char c = minecraftLogo[i1].charAt(j1);
                    if(c == ' ')
                    {
                        continue;
                    }
                    GL11.glPushMatrix();
                    LogoEffectRandomizer logoeffectrandomizer = logoEffects[j1][i1];
                    float f1 = (float)(logoeffectrandomizer.field_1311_b + (logoeffectrandomizer.field_1312_a - logoeffectrandomizer.field_1311_b) * (double)f);
                    float f2 = 1.0F;
                    float f3 = 1.0F;
                    float f4 = 0.0F;
                    if(l == 0)
                    {
                        f2 = f1 * 0.04F + 1.0F;
                        f3 = 1.0F / f2;
                        f1 = 0.0F;
                    }
                    GL11.glTranslatef(j1, i1, f1);
                    GL11.glScalef(f2, f2, f2);
                    GL11.glRotatef(f4, 0.0F, 1.0F, 0.0F);
                    if (LogoConfig.getBlock(i1, j1) != null) 
                    {
                    	renderblockslogofunc.func_1238_a(LogoConfig.getBlock(i1, j1), LogoConfig.getMetaData(i1, j1), f3);
                    }
                    else 
                    {
                    	renderblockslogofunc.func_1238_a(Block.stone, 0, f3);
                    }
                    GL11.glPopMatrix();
                }

            }

            GL11.glPopMatrix();
        }

        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        GL11.glPopMatrix();
        GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
        GL11.glEnable(2884 /*GL_CULL_FACE*/);
    }

    public static Random getRand()
    {
        return rand;
    }

    private static final Random rand = new Random();
    String minecraftLogo[];
    private LogoEffectRandomizer logoEffects[][];
    private float updateCounter;
    private String splashText;
    private GuiButton multiplayerButton;

}
