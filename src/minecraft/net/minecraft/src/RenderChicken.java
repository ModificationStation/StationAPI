// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            RenderLiving, EntityChicken, MathHelper, ModelBase, 
//            EntityLiving, Entity

public class RenderChicken extends RenderLiving
{

    public RenderChicken(ModelBase modelbase, float f)
    {
        super(modelbase, f);
    }

    public void renderChicken(EntityChicken entitychicken, double d, double d1, double d2, 
            float f, float f1)
    {
        super.doRenderLiving(entitychicken, d, d1, d2, f, f1);
    }

    protected float getWingRotation(EntityChicken entitychicken, float f)
    {
        float f1 = entitychicken.field_756_e + (entitychicken.field_752_b - entitychicken.field_756_e) * f;
        float f2 = entitychicken.field_757_d + (entitychicken.destPos - entitychicken.field_757_d) * f;
        return (MathHelper.sin(f1) + 1.0F) * f2;
    }

    protected float func_170_d(EntityLiving entityliving, float f)
    {
        return getWingRotation((EntityChicken)entityliving, f);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, 
            float f, float f1)
    {
        renderChicken((EntityChicken)entityliving, d, d1, d2, f, f1);
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        renderChicken((EntityChicken)entity, d, d1, d2, f, f1);
    }
}
