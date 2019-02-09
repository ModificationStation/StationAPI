// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            Render, RenderBlocks, Block, EntityFallingSand, 
//            MathHelper, Entity

public class RenderFallingSand extends Render
{

    public RenderFallingSand()
    {
        field_197_d = new RenderBlocks();
        shadowSize = 0.5F;
    }

    public void doRenderFallingSand(EntityFallingSand entityfallingsand, double d, double d1, double d2, 
            float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        loadTexture("/terrain.png");
        Block block = Block.blocksList[entityfallingsand.blockID];
        World world = entityfallingsand.getWorld();
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        field_197_d.renderBlockFallingSand(block, world, MathHelper.floor_double(entityfallingsand.posX), MathHelper.floor_double(entityfallingsand.posY), MathHelper.floor_double(entityfallingsand.posZ));
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glPopMatrix();
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        doRenderFallingSand((EntityFallingSand)entity, d, d1, d2, f, f1);
    }

    private RenderBlocks field_197_d;
}
