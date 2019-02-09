// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            Render, ModelMinecart, EntityMinecart, Vec3D, 
//            MathHelper, RenderBlocks, Block, ModelBase, 
//            Entity

public class RenderMinecart extends Render
{

    public RenderMinecart()
    {
        shadowSize = 0.5F;
        modelMinecart = new ModelMinecart();
    }

    public void func_152_a(EntityMinecart entityminecart, double d, double d1, double d2, 
            float f, float f1)
    {
        GL11.glPushMatrix();
        double d3 = entityminecart.lastTickPosX + (entityminecart.posX - entityminecart.lastTickPosX) * (double)f1;
        double d4 = entityminecart.lastTickPosY + (entityminecart.posY - entityminecart.lastTickPosY) * (double)f1;
        double d5 = entityminecart.lastTickPosZ + (entityminecart.posZ - entityminecart.lastTickPosZ) * (double)f1;
        double d6 = 0.30000001192092896D;
        Vec3D vec3d = entityminecart.func_514_g(d3, d4, d5);
        float f2 = entityminecart.prevRotationPitch + (entityminecart.rotationPitch - entityminecart.prevRotationPitch) * f1;
        if(vec3d != null)
        {
            Vec3D vec3d1 = entityminecart.func_515_a(d3, d4, d5, d6);
            Vec3D vec3d2 = entityminecart.func_515_a(d3, d4, d5, -d6);
            if(vec3d1 == null)
            {
                vec3d1 = vec3d;
            }
            if(vec3d2 == null)
            {
                vec3d2 = vec3d;
            }
            d += vec3d.xCoord - d3;
            d1 += (vec3d1.yCoord + vec3d2.yCoord) / 2D - d4;
            d2 += vec3d.zCoord - d5;
            Vec3D vec3d3 = vec3d2.addVector(-vec3d1.xCoord, -vec3d1.yCoord, -vec3d1.zCoord);
            if(vec3d3.lengthVector() != 0.0D)
            {
                vec3d3 = vec3d3.normalize();
                f = (float)((Math.atan2(vec3d3.zCoord, vec3d3.xCoord) * 180D) / 3.1415926535897931D);
                f2 = (float)(Math.atan(vec3d3.yCoord) * 73D);
            }
        }
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef(180F - f, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-f2, 0.0F, 0.0F, 1.0F);
        float f3 = (float)entityminecart.minecartTimeSinceHit - f1;
        float f4 = (float)entityminecart.minecartCurrentDamage - f1;
        if(f4 < 0.0F)
        {
            f4 = 0.0F;
        }
        if(f3 > 0.0F)
        {
            GL11.glRotatef(((MathHelper.sin(f3) * f3 * f4) / 10F) * (float)entityminecart.minecartRockDirection, 1.0F, 0.0F, 0.0F);
        }
        if(entityminecart.minecartType != 0)
        {
            loadTexture("/terrain.png");
            float f5 = 0.75F;
            GL11.glScalef(f5, f5, f5);
            GL11.glTranslatef(0.0F, 0.3125F, 0.0F);
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
            if(entityminecart.minecartType == 1)
            {
                (new RenderBlocks()).renderBlockOnInventory(Block.chest, 0, entityminecart.getEntityBrightness(f1));
            } else
            if(entityminecart.minecartType == 2)
            {
                (new RenderBlocks()).renderBlockOnInventory(Block.stoneOvenIdle, 0, entityminecart.getEntityBrightness(f1));
            }
            GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.3125F, 0.0F);
            GL11.glScalef(1.0F / f5, 1.0F / f5, 1.0F / f5);
        }
        loadTexture("/item/cart.png");
        GL11.glScalef(-1F, -1F, 1.0F);
        modelMinecart.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        func_152_a((EntityMinecart)entity, d, d1, d2, f, f1);
    }

    protected ModelBase modelMinecart;
}
