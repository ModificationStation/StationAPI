// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            ModelBase, ModelRenderer

public class ModelSquid extends ModelBase
{

    public ModelSquid()
    {
        squidTentacles = new ModelRenderer[8];
        byte byte0 = -16;
        squidBody = new ModelRenderer(0, 0);
        squidBody.addBox(-6F, -8F, -6F, 12, 16, 12);
        squidBody.rotationPointY += 24 + byte0;
        for(int i = 0; i < squidTentacles.length; i++)
        {
            squidTentacles[i] = new ModelRenderer(48, 0);
            double d = ((double)i * 3.1415926535897931D * 2D) / (double)squidTentacles.length;
            float f = (float)Math.cos(d) * 5F;
            float f1 = (float)Math.sin(d) * 5F;
            squidTentacles[i].addBox(-1F, 0.0F, -1F, 2, 18, 2);
            squidTentacles[i].rotationPointX = f;
            squidTentacles[i].rotationPointZ = f1;
            squidTentacles[i].rotationPointY = 31 + byte0;
            d = ((double)i * 3.1415926535897931D * -2D) / (double)squidTentacles.length + 1.5707963267948966D;
            squidTentacles[i].rotateAngleY = (float)d;
        }

    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        for(int i = 0; i < squidTentacles.length; i++)
        {
            squidTentacles[i].rotateAngleX = f2;
        }

    }

    public void render(float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        squidBody.render(f5);
        for(int i = 0; i < squidTentacles.length; i++)
        {
            squidTentacles[i].render(f5);
        }

    }

    ModelRenderer squidBody;
    ModelRenderer squidTentacles[];
}
