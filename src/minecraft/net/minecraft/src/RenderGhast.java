// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            RenderLiving, ModelGhast, EntityGhast, EntityLiving

public class RenderGhast extends RenderLiving
{

    public RenderGhast()
    {
        super(new ModelGhast(), 0.5F);
    }

    protected void func_4014_a(EntityGhast entityghast, float f)
    {
        EntityGhast entityghast1 = entityghast;
        float f1 = ((float)entityghast1.prevAttackCounter + (float)(entityghast1.attackCounter - entityghast1.prevAttackCounter) * f) / 20F;
        if(f1 < 0.0F)
        {
            f1 = 0.0F;
        }
        f1 = 1.0F / (f1 * f1 * f1 * f1 * f1 * 2.0F + 1.0F);
        float f2 = (8F + f1) / 2.0F;
        float f3 = (8F + 1.0F / f1) / 2.0F;
        GL11.glScalef(f3, f2, f3);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void preRenderCallback(EntityLiving entityliving, float f)
    {
        func_4014_a((EntityGhast)entityliving, f);
    }
}
