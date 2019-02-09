// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            EntityFX, World, Tessellator

public class EntityPortalFX extends EntityFX
{

    public EntityPortalFX(World world, double d, double d1, double d2, 
            double d3, double d4, double d5)
    {
        super(world, d, d1, d2, d3, d4, d5);
        motionX = d3;
        motionY = d4;
        motionZ = d5;
        field_4086_p = posX = d;
        field_4085_q = posY = d1;
        field_4084_r = posZ = d2;
        float f = rand.nextFloat() * 0.6F + 0.4F;
        field_4083_a = particleScale = rand.nextFloat() * 0.2F + 0.5F;
        particleRed = particleGreen = particleBlue = 1.0F * f;
        particleGreen *= 0.3F;
        particleRed *= 0.9F;
        particleMaxAge = (int)(Math.random() * 10D) + 40;
        noClip = true;
        particleTextureIndex = (int)(Math.random() * 8D);
    }

    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = ((float)particleAge + f) / (float)particleMaxAge;
        f6 = 1.0F - f6;
        f6 *= f6;
        f6 = 1.0F - f6;
        particleScale = field_4083_a * f6;
        super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
    }

    public float getEntityBrightness(float f)
    {
        float f1 = super.getEntityBrightness(f);
        float f2 = (float)particleAge / (float)particleMaxAge;
        f2 *= f2;
        f2 *= f2;
        return f1 * (1.0F - f2) + f2;
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        float f = (float)particleAge / (float)particleMaxAge;
        float f1 = f;
        f = -f + f * f * 2.0F;
        f = 1.0F - f;
        posX = field_4086_p + motionX * (double)f;
        posY = field_4085_q + motionY * (double)f + (double)(1.0F - f1);
        posZ = field_4084_r + motionZ * (double)f;
        if(particleAge++ >= particleMaxAge)
        {
            setEntityDead();
        }
    }

    private float field_4083_a;
    private double field_4086_p;
    private double field_4085_q;
    private double field_4084_r;
}
