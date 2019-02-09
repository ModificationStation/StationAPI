// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            TileEntityRenderer, RenderEngine, TileEntity, World, 
//            FontRenderer

public abstract class TileEntitySpecialRenderer
{

    public TileEntitySpecialRenderer()
    {
    }

    public abstract void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, 
            float f);

    protected void bindTextureByName(String s)
    {
        RenderEngine renderengine = tileEntityRenderer.renderEngine;
        renderengine.bindTexture(renderengine.getTexture(s));
    }

    public void setTileEntityRenderer(TileEntityRenderer tileentityrenderer)
    {
        tileEntityRenderer = tileentityrenderer;
    }

    public void func_31069_a(World world)
    {
    }

    public FontRenderer getFontRenderer()
    {
        return tileEntityRenderer.getFontRenderer();
    }

    protected TileEntityRenderer tileEntityRenderer;
}
