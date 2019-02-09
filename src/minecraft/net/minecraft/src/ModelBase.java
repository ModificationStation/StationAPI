// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            EntityLiving

public abstract class ModelBase
{

    public ModelBase()
    {
        isRiding = false;
    }

    public void render(float f, float f1, float f2, float f3, float f4, float f5)
    {
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
    }

    public void setLivingAnimations(EntityLiving entityliving, float f, float f1, float f2)
    {
    }

    public float onGround;
    public boolean isRiding;
}
