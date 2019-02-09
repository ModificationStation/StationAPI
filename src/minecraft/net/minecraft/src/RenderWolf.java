// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            RenderLiving, EntityWolf, ModelBase, EntityLiving, 
//            Entity

public class RenderWolf extends RenderLiving
{

    public RenderWolf(ModelBase modelbase, float f)
    {
        super(modelbase, f);
    }

    public void renderWolf(EntityWolf entitywolf, double d, double d1, double d2, 
            float f, float f1)
    {
        super.doRenderLiving(entitywolf, d, d1, d2, f, f1);
    }

    protected float func_25004_a(EntityWolf entitywolf, float f)
    {
        return entitywolf.setTailRotation();
    }

    protected void func_25006_b(EntityWolf entitywolf, float f)
    {
    }

    protected void preRenderCallback(EntityLiving entityliving, float f)
    {
        func_25006_b((EntityWolf)entityliving, f);
    }

    protected float func_170_d(EntityLiving entityliving, float f)
    {
        return func_25004_a((EntityWolf)entityliving, f);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, 
            float f, float f1)
    {
        renderWolf((EntityWolf)entityliving, d, d1, d2, f, f1);
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        renderWolf((EntityWolf)entity, d, d1, d2, f, f1);
    }
}
