package net.modificationstation.stationloader.client.proxy;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ScaledResolution;
import net.modificationstation.stationloader.events.client.gui.guiscreen.DrawScreen;

/**
 * EntityRenderer proxy that is used to hook into "updateCameraAndRender" function
 * Original EntityRenderer is replaced with this one on MCPostInitializationEvent by StationLoader's mod
 * 
 * Will be replaced with ASM patch later.
 * 
 * @author mine_diver
 *
 */
public class EntityRenderer extends net.minecraft.src.EntityRenderer{

    public EntityRenderer(Minecraft minecraft) {
        super(minecraft);
        mc = minecraft;
        field_28133_I = 0;
    }
    @Override
    public void updateCameraAndRender(float f)
    {
        boolean skipRenderWorld = mc.skipRenderWorld;
        mc.skipRenderWorld = true;
        super.updateCameraAndRender(f);
        mc.skipRenderWorld = skipRenderWorld;
        field_28135_a = mc.gameSettings.anaglyph;
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        int k = (Mouse.getX() * i) / mc.displayWidth;
        int i1 = j - (Mouse.getY() * j) / mc.displayHeight - 1;
        char c = '\310';
        if(mc.gameSettings.limitFramerate == 1)
        {
            c = 'x';
        }
        if(mc.gameSettings.limitFramerate == 2)
        {
            c = '(';
        }
        if(mc.theWorld != null)
        {
            if(mc.gameSettings.limitFramerate == 0)
            {
                renderWorld(f, 0L);
            } else
            {
                renderWorld(f, field_28133_I + (long)(0x3b9aca00 / c));
            }
            if(mc.gameSettings.limitFramerate == 2)
            {
                long l1 = ((field_28133_I + (long)(0x3b9aca00 / c)) - System.nanoTime()) / 0xf4240L;
                if(l1 > 0L && l1 < 500L)
                {
                    try
                    {
                        Thread.sleep(l1);
                    }
                    catch(InterruptedException interruptedexception)
                    {
                        interruptedexception.printStackTrace();
                    }
                }
            }
            field_28133_I = System.nanoTime();
            if(!mc.gameSettings.hideGUI || mc.currentScreen != null)
            {
                mc.ingameGUI.renderGameOverlay(f, mc.currentScreen != null, k, i1);
            }
        } else
        {
            GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
            GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
            GL11.glLoadIdentity();
            func_905_b();
            if(mc.gameSettings.limitFramerate == 2)
            {
                long l2 = ((field_28133_I + (long)(0x3b9aca00 / c)) - System.nanoTime()) / 0xf4240L;
                if(l2 < 0L)
                {
                    l2 += 10L;
                }
                if(l2 > 0L && l2 < 500L)
                {
                    try
                    {
                        Thread.sleep(l2);
                    }
                    catch(InterruptedException interruptedexception1)
                    {
                        interruptedexception1.printStackTrace();
                    }
                }
            }
            field_28133_I = System.nanoTime();
        }
        if(mc.currentScreen != null)
        {
            GL11.glClear(256);
            if (DrawScreen.EVENT.getInvoker().drawScreen(mc.currentScreen, k, i1, f, mc.currentScreen.getClass().getSimpleName()))
                mc.currentScreen.drawScreen(k, i1, f);
            if(mc.currentScreen != null && mc.currentScreen.field_25091_h != null)
            {
                mc.currentScreen.field_25091_h.func_25087_a(f);
            }
        }
    }
    private Minecraft mc;
    private long field_28133_I;
}
