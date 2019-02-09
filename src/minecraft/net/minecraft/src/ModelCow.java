// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            ModelQuadruped, ModelRenderer

public class ModelCow extends ModelQuadruped
{

    public ModelCow()
    {
        super(12, 0.0F);
        head = new ModelRenderer(0, 0);
        head.addBox(-4F, -4F, -6F, 8, 8, 6, 0.0F);
        head.setRotationPoint(0.0F, 4F, -8F);
        horn1 = new ModelRenderer(22, 0);
        horn1.addBox(-4F, -5F, -4F, 1, 3, 1, 0.0F);
        horn1.setRotationPoint(0.0F, 3F, -7F);
        horn2 = new ModelRenderer(22, 0);
        horn2.addBox(3F, -5F, -4F, 1, 3, 1, 0.0F);
        horn2.setRotationPoint(0.0F, 3F, -7F);
        udders = new ModelRenderer(52, 0);
        udders.addBox(-2F, -3F, 0.0F, 4, 6, 2, 0.0F);
        udders.setRotationPoint(0.0F, 14F, 6F);
        udders.rotateAngleX = 1.570796F;
        body = new ModelRenderer(18, 4);
        body.addBox(-6F, -10F, -7F, 12, 18, 10, 0.0F);
        body.setRotationPoint(0.0F, 5F, 2.0F);
        leg1.rotationPointX--;
        leg2.rotationPointX++;
        leg1.rotationPointZ += 0.0F;
        leg2.rotationPointZ += 0.0F;
        leg3.rotationPointX--;
        leg4.rotationPointX++;
        leg3.rotationPointZ--;
        leg4.rotationPointZ--;
    }

    public void render(float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(f, f1, f2, f3, f4, f5);
        horn1.render(f5);
        horn2.render(f5);
        udders.render(f5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
        horn1.rotateAngleY = head.rotateAngleY;
        horn1.rotateAngleX = head.rotateAngleX;
        horn2.rotateAngleY = head.rotateAngleY;
        horn2.rotateAngleX = head.rotateAngleX;
    }

    ModelRenderer udders;
    ModelRenderer horn1;
    ModelRenderer horn2;
}
