// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            ModelQuadruped, ModelRenderer

public class ModelSheep1 extends ModelQuadruped
{

    public ModelSheep1()
    {
        super(12, 0.0F);
        head = new ModelRenderer(0, 0);
        head.addBox(-3F, -4F, -4F, 6, 6, 6, 0.6F);
        head.setRotationPoint(0.0F, 6F, -8F);
        body = new ModelRenderer(28, 8);
        body.addBox(-4F, -10F, -7F, 8, 16, 6, 1.75F);
        body.setRotationPoint(0.0F, 5F, 2.0F);
        float f = 0.5F;
        leg1 = new ModelRenderer(0, 16);
        leg1.addBox(-2F, 0.0F, -2F, 4, 6, 4, f);
        leg1.setRotationPoint(-3F, 12F, 7F);
        leg2 = new ModelRenderer(0, 16);
        leg2.addBox(-2F, 0.0F, -2F, 4, 6, 4, f);
        leg2.setRotationPoint(3F, 12F, 7F);
        leg3 = new ModelRenderer(0, 16);
        leg3.addBox(-2F, 0.0F, -2F, 4, 6, 4, f);
        leg3.setRotationPoint(-3F, 12F, -5F);
        leg4 = new ModelRenderer(0, 16);
        leg4.addBox(-2F, 0.0F, -2F, 4, 6, 4, f);
        leg4.setRotationPoint(3F, 12F, -5F);
    }
}
