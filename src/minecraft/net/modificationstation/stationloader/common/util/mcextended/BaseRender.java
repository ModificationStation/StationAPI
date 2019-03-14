// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.modificationstation.stationloader.common.util.mcextended;

import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderItem;

// Referenced classes of package net.minecraft.src:
//            RenderItem, FontRenderer, RenderEngine, ItemStack, 
//            EntityItem, ItemRenderer, EntityRenderer

public abstract class BaseRender
{

    public BaseRender()
    {
    }

    public void renderItemOverlayIntoGUI(RenderItem renderitem, FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j)
    {
    }

    public void renderItemOnGround(RenderItem renderitem, EntityItem entityitem, double d, double d1, float f, 
            float f1)
    {
    }

    public void renderItemIntoGUI(RenderItem renderitem, FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j)
    {
    }

    public void renderItemInHand(ItemRenderer itemrenderer, ItemStack itemstack)
    {
    }

    public boolean renderPlayerOverlay(ItemRenderer itemrenderer, float f)
    {
        return false;
    }

    public boolean updateFogColor(EntityRenderer entityrenderer, float f)
    {
        return false;
    }

    public boolean setupFog(EntityRenderer entityrenderer, int i, float f)
    {
        return false;
    }
}
