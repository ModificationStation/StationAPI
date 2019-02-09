// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            RenderLiving, EntitySlime, ModelBase, EntityLiving

public class RenderSlime extends RenderLiving
{

    public RenderSlime(ModelBase modelbase, ModelBase modelbase1, float f)
    {
        super(modelbase, f);
        scaleAmount = modelbase1;
    }

    protected boolean renderSlimePassModel(EntitySlime entityslime, int i, float f)
    {
        if(i == 0)
        {
            setRenderPassModel(scaleAmount);
            GL11.glEnable(2977 /*GL_NORMALIZE*/);
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glBlendFunc(770, 771);
            return true;
        }
        if(i == 1)
        {
            GL11.glDisable(3042 /*GL_BLEND*/);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        return false;
    }

    protected void scaleSlime(EntitySlime entityslime, float f)
    {
        int i = entityslime.getSlimeSize();
        float f1 = (entityslime.field_767_b + (entityslime.field_768_a - entityslime.field_767_b) * f) / ((float)i * 0.5F + 1.0F);
        float f2 = 1.0F / (f1 + 1.0F);
        float f3 = i;
        GL11.glScalef(f2 * f3, (1.0F / f2) * f3, f2 * f3);
    }

    protected void preRenderCallback(EntityLiving entityliving, float f)
    {
        scaleSlime((EntitySlime)entityliving, f);
    }

    protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f)
    {
        return renderSlimePassModel((EntitySlime)entityliving, i, f);
    }

    private ModelBase scaleAmount;
}
