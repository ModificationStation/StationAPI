// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            Render, Tessellator, RenderManager, EntityFish, 
//            EntityPlayer, MathHelper, Vec3D, GameSettings, 
//            Entity

public class RenderFish extends Render
{

    public RenderFish()
    {
    }

    public void func_4011_a(EntityFish entityfish, double d, double d1, double d2, 
            float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        int i = 1;
        byte byte0 = 2;
        loadTexture("/particles.png");
        Tessellator tessellator = Tessellator.instance;
        float f2 = (float)(i * 8 + 0) / 128F;
        float f3 = (float)(i * 8 + 8) / 128F;
        float f4 = (float)(byte0 * 8 + 0) / 128F;
        float f5 = (float)(byte0 * 8 + 8) / 128F;
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.5F;
        GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV(0.0F - f7, 0.0F - f8, 0.0D, f2, f5);
        tessellator.addVertexWithUV(f6 - f7, 0.0F - f8, 0.0D, f3, f5);
        tessellator.addVertexWithUV(f6 - f7, 1.0F - f8, 0.0D, f3, f4);
        tessellator.addVertexWithUV(0.0F - f7, 1.0F - f8, 0.0D, f2, f4);
        tessellator.draw();
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glPopMatrix();
        if(entityfish.angler != null)
        {
            float f9 = ((entityfish.angler.prevRotationYaw + (entityfish.angler.rotationYaw - entityfish.angler.prevRotationYaw) * f1) * 3.141593F) / 180F;
            double d3 = MathHelper.sin(f9);
            double d5 = MathHelper.cos(f9);
            float f11 = entityfish.angler.getSwingProgress(f1);
            float f12 = MathHelper.sin(MathHelper.sqrt_float(f11) * 3.141593F);
            Vec3D vec3d = Vec3D.createVector(-0.5D, 0.029999999999999999D, 0.80000000000000004D);
            vec3d.rotateAroundX((-(entityfish.angler.prevRotationPitch + (entityfish.angler.rotationPitch - entityfish.angler.prevRotationPitch) * f1) * 3.141593F) / 180F);
            vec3d.rotateAroundY((-(entityfish.angler.prevRotationYaw + (entityfish.angler.rotationYaw - entityfish.angler.prevRotationYaw) * f1) * 3.141593F) / 180F);
            vec3d.rotateAroundY(f12 * 0.5F);
            vec3d.rotateAroundX(-f12 * 0.7F);
            double d7 = entityfish.angler.prevPosX + (entityfish.angler.posX - entityfish.angler.prevPosX) * (double)f1 + vec3d.xCoord;
            double d8 = entityfish.angler.prevPosY + (entityfish.angler.posY - entityfish.angler.prevPosY) * (double)f1 + vec3d.yCoord;
            double d9 = entityfish.angler.prevPosZ + (entityfish.angler.posZ - entityfish.angler.prevPosZ) * (double)f1 + vec3d.zCoord;
            if(renderManager.options.thirdPersonView)
            {
                float f10 = ((entityfish.angler.prevRenderYawOffset + (entityfish.angler.renderYawOffset - entityfish.angler.prevRenderYawOffset) * f1) * 3.141593F) / 180F;
                double d4 = MathHelper.sin(f10);
                double d6 = MathHelper.cos(f10);
                d7 = (entityfish.angler.prevPosX + (entityfish.angler.posX - entityfish.angler.prevPosX) * (double)f1) - d6 * 0.34999999999999998D - d4 * 0.84999999999999998D;
                d8 = (entityfish.angler.prevPosY + (entityfish.angler.posY - entityfish.angler.prevPosY) * (double)f1) - 0.45000000000000001D;
                d9 = ((entityfish.angler.prevPosZ + (entityfish.angler.posZ - entityfish.angler.prevPosZ) * (double)f1) - d4 * 0.34999999999999998D) + d6 * 0.84999999999999998D;
            }
            double d10 = entityfish.prevPosX + (entityfish.posX - entityfish.prevPosX) * (double)f1;
            double d11 = entityfish.prevPosY + (entityfish.posY - entityfish.prevPosY) * (double)f1 + 0.25D;
            double d12 = entityfish.prevPosZ + (entityfish.posZ - entityfish.prevPosZ) * (double)f1;
            double d13 = (float)(d7 - d10);
            double d14 = (float)(d8 - d11);
            double d15 = (float)(d9 - d12);
            GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
            GL11.glDisable(2896 /*GL_LIGHTING*/);
            tessellator.startDrawing(3);
            tessellator.setColorOpaque_I(0);
            int j = 16;
            for(int k = 0; k <= j; k++)
            {
                float f13 = (float)k / (float)j;
                tessellator.addVertex(d + d13 * (double)f13, d1 + d14 * (double)(f13 * f13 + f13) * 0.5D + 0.25D, d2 + d15 * (double)f13);
            }

            tessellator.draw();
            GL11.glEnable(2896 /*GL_LIGHTING*/);
            GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        }
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        func_4011_a((EntityFish)entity, d, d1, d2, f, f1);
    }
}
