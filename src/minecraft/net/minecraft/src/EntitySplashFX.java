// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            EntityRainFX, World

public class EntitySplashFX extends EntityRainFX
{

    public EntitySplashFX(World world, double d, double d1, double d2, 
            double d3, double d4, double d5)
    {
        super(world, d, d1, d2);
        particleGravity = 0.04F;
        particleTextureIndex++;
        if(d4 == 0.0D && (d3 != 0.0D || d5 != 0.0D))
        {
            motionX = d3;
            motionY = d4 + 0.10000000000000001D;
            motionZ = d5;
        }
    }
}
