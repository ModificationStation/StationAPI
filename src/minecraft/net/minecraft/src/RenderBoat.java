// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            Render, ModelBoat, EntityBoat, MathHelper, 
//            ModelBase, Entity

public class RenderBoat extends Render
{

    public RenderBoat()
    {
        shadowSize = 0.5F;
        modelBoat = new ModelBoat();
    }

    public void func_157_a(EntityBoat entityboat, double d, double d1, double d2, 
            float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef(180F - f, 0.0F, 1.0F, 0.0F);
        float f2 = (float)entityboat.boatTimeSinceHit - f1;
        float f3 = (float)entityboat.boatCurrentDamage - f1;
        if(f3 < 0.0F)
        {
            f3 = 0.0F;
        }
        if(f2 > 0.0F)
        {
            GL11.glRotatef(((MathHelper.sin(f2) * f2 * f3) / 10F) * (float)entityboat.boatRockDirection, 1.0F, 0.0F, 0.0F);
        }
        loadTexture("/terrain.png");
        float f4 = 0.75F;
        GL11.glScalef(f4, f4, f4);
        GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
        loadTexture("/item/boat.png");
        GL11.glScalef(-1F, -1F, 1.0F);
        modelBoat.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        func_157_a((EntityBoat)entity, d, d1, d2, f, f1);
    }

    protected ModelBase modelBoat;
}
