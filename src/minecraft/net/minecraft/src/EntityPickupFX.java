// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            EntityFX, Entity, MathHelper, World, 
//            RenderManager, Tessellator

public class EntityPickupFX extends EntityFX
{

    public EntityPickupFX(World world, Entity entity, Entity entity1, float f)
    {
        super(world, entity.posX, entity.posY, entity.posZ, entity.motionX, entity.motionY, entity.motionZ);
        field_678_p = 0;
        field_677_q = 0;
        field_675_a = entity;
        field_679_o = entity1;
        field_677_q = 3;
        field_676_r = f;
    }

    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = ((float)field_678_p + f) / (float)field_677_q;
        f6 *= f6;
        double d = field_675_a.posX;
        double d1 = field_675_a.posY;
        double d2 = field_675_a.posZ;
        double d3 = field_679_o.lastTickPosX + (field_679_o.posX - field_679_o.lastTickPosX) * (double)f;
        double d4 = field_679_o.lastTickPosY + (field_679_o.posY - field_679_o.lastTickPosY) * (double)f + (double)field_676_r;
        double d5 = field_679_o.lastTickPosZ + (field_679_o.posZ - field_679_o.lastTickPosZ) * (double)f;
        double d6 = d + (d3 - d) * (double)f6;
        double d7 = d1 + (d4 - d1) * (double)f6;
        double d8 = d2 + (d5 - d2) * (double)f6;
        int i = MathHelper.floor_double(d6);
        int j = MathHelper.floor_double(d7 + (double)(yOffset / 2.0F));
        int k = MathHelper.floor_double(d8);
        float f7 = worldObj.getLightBrightness(i, j, k);
        d6 -= interpPosX;
        d7 -= interpPosY;
        d8 -= interpPosZ;
        GL11.glColor4f(f7, f7, f7, 1.0F);
        RenderManager.instance.renderEntityWithPosYaw(field_675_a, (float)d6, (float)d7, (float)d8, field_675_a.rotationYaw, f);
    }

    public void onUpdate()
    {
        field_678_p++;
        if(field_678_p == field_677_q)
        {
            setEntityDead();
        }
    }

    public int getFXLayer()
    {
        return 3;
    }

    private Entity field_675_a;
    private Entity field_679_o;
    private int field_678_p;
    private int field_677_q;
    private float field_676_r;
}
