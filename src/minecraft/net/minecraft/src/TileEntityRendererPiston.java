// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            TileEntitySpecialRenderer, Block, TileEntityPiston, Tessellator, 
//            RenderHelper, RenderBlocks, BlockPistonBase, BlockPistonExtension, 
//            World, TileEntity

public class TileEntityRendererPiston extends TileEntitySpecialRenderer
{

    public TileEntityRendererPiston()
    {
    }

    public void func_31070_a(TileEntityPiston tileentitypiston, double d, double d1, double d2, 
            float f)
    {
        Block block = Block.blocksList[tileentitypiston.getStoredBlockID()];
        if(block != null && tileentitypiston.func_31008_a(f) < 1.0F)
        {
            Tessellator tessellator = Tessellator.instance;
            bindTextureByName("/terrain.png");
            RenderHelper.disableStandardItemLighting();
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glDisable(2884 /*GL_CULL_FACE*/);
            if(Minecraft.isAmbientOcclusionEnabled())
            {
                GL11.glShadeModel(7425 /*GL_SMOOTH*/);
            } else
            {
                GL11.glShadeModel(7424 /*GL_FLAT*/);
            }
            tessellator.startDrawingQuads();
            tessellator.setTranslationD(((float)d - (float)tileentitypiston.xCoord) + tileentitypiston.func_31017_b(f), ((float)d1 - (float)tileentitypiston.yCoord) + tileentitypiston.func_31014_c(f), ((float)d2 - (float)tileentitypiston.zCoord) + tileentitypiston.func_31013_d(f));
            tessellator.setColorOpaque(1, 1, 1);
            if(block == Block.pistonExtension && tileentitypiston.func_31008_a(f) < 0.5F)
            {
                field_31071_b.func_31079_a(block, tileentitypiston.xCoord, tileentitypiston.yCoord, tileentitypiston.zCoord, false);
            } else
            if(tileentitypiston.func_31012_k() && !tileentitypiston.func_31015_b())
            {
                Block.pistonExtension.func_31052_a_(((BlockPistonBase)block).func_31040_i());
                field_31071_b.func_31079_a(Block.pistonExtension, tileentitypiston.xCoord, tileentitypiston.yCoord, tileentitypiston.zCoord, tileentitypiston.func_31008_a(f) < 0.5F);
                Block.pistonExtension.func_31051_a();
                tessellator.setTranslationD((float)d - (float)tileentitypiston.xCoord, (float)d1 - (float)tileentitypiston.yCoord, (float)d2 - (float)tileentitypiston.zCoord);
                field_31071_b.func_31078_d(block, tileentitypiston.xCoord, tileentitypiston.yCoord, tileentitypiston.zCoord);
            } else
            {
                field_31071_b.func_31075_a(block, tileentitypiston.xCoord, tileentitypiston.yCoord, tileentitypiston.zCoord);
            }
            tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }

    public void func_31069_a(World world)
    {
        field_31071_b = new RenderBlocks(world);
    }

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, 
            float f)
    {
        func_31070_a((TileEntityPiston)tileentity, d, d1, d2, f);
    }

    private RenderBlocks field_31071_b;
}
