// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            ModelBase, ModelRenderer, EntityWolf, MathHelper, 
//            EntityLiving

public class ModelWolf extends ModelBase
{

    public ModelWolf()
    {
        float f = 0.0F;
        float f1 = 13.5F;
        wolfHeadMain = new ModelRenderer(0, 0);
        wolfHeadMain.addBox(-3F, -3F, -2F, 6, 6, 4, f);
        wolfHeadMain.setRotationPoint(-1F, f1, -7F);
        wolfBody = new ModelRenderer(18, 14);
        wolfBody.addBox(-4F, -2F, -3F, 6, 9, 6, f);
        wolfBody.setRotationPoint(0.0F, 14F, 2.0F);
        wolfMane = new ModelRenderer(21, 0);
        wolfMane.addBox(-4F, -3F, -3F, 8, 6, 7, f);
        wolfMane.setRotationPoint(-1F, 14F, 2.0F);
        wolfLeg1 = new ModelRenderer(0, 18);
        wolfLeg1.addBox(-1F, 0.0F, -1F, 2, 8, 2, f);
        wolfLeg1.setRotationPoint(-2.5F, 16F, 7F);
        wolfLeg2 = new ModelRenderer(0, 18);
        wolfLeg2.addBox(-1F, 0.0F, -1F, 2, 8, 2, f);
        wolfLeg2.setRotationPoint(0.5F, 16F, 7F);
        wolfLeg3 = new ModelRenderer(0, 18);
        wolfLeg3.addBox(-1F, 0.0F, -1F, 2, 8, 2, f);
        wolfLeg3.setRotationPoint(-2.5F, 16F, -4F);
        wolfLeg4 = new ModelRenderer(0, 18);
        wolfLeg4.addBox(-1F, 0.0F, -1F, 2, 8, 2, f);
        wolfLeg4.setRotationPoint(0.5F, 16F, -4F);
        wolfTail = new ModelRenderer(9, 18);
        wolfTail.addBox(-1F, 0.0F, -1F, 2, 8, 2, f);
        wolfTail.setRotationPoint(-1F, 12F, 8F);
        wolfRightEar = new ModelRenderer(16, 14);
        wolfRightEar.addBox(-3F, -5F, 0.0F, 2, 2, 1, f);
        wolfRightEar.setRotationPoint(-1F, f1, -7F);
        wolfLeftEar = new ModelRenderer(16, 14);
        wolfLeftEar.addBox(1.0F, -5F, 0.0F, 2, 2, 1, f);
        wolfLeftEar.setRotationPoint(-1F, f1, -7F);
        wolfSnout = new ModelRenderer(0, 10);
        wolfSnout.addBox(-2F, 0.0F, -5F, 3, 3, 4, f);
        wolfSnout.setRotationPoint(-0.5F, f1, -7F);
    }

    public void render(float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5);
        wolfHeadMain.renderWithRotation(f5);
        wolfBody.render(f5);
        wolfLeg1.render(f5);
        wolfLeg2.render(f5);
        wolfLeg3.render(f5);
        wolfLeg4.render(f5);
        wolfRightEar.renderWithRotation(f5);
        wolfLeftEar.renderWithRotation(f5);
        wolfSnout.renderWithRotation(f5);
        wolfTail.renderWithRotation(f5);
        wolfMane.render(f5);
    }

    public void setLivingAnimations(EntityLiving entityliving, float f, float f1, float f2)
    {
        EntityWolf entitywolf = (EntityWolf)entityliving;
        if(entitywolf.isWolfAngry())
        {
            wolfTail.rotateAngleY = 0.0F;
        } else
        {
            wolfTail.rotateAngleY = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        }
        if(entitywolf.isWolfSitting())
        {
            wolfMane.setRotationPoint(-1F, 16F, -3F);
            wolfMane.rotateAngleX = 1.256637F;
            wolfMane.rotateAngleY = 0.0F;
            wolfBody.setRotationPoint(0.0F, 18F, 0.0F);
            wolfBody.rotateAngleX = 0.7853982F;
            wolfTail.setRotationPoint(-1F, 21F, 6F);
            wolfLeg1.setRotationPoint(-2.5F, 22F, 2.0F);
            wolfLeg1.rotateAngleX = 4.712389F;
            wolfLeg2.setRotationPoint(0.5F, 22F, 2.0F);
            wolfLeg2.rotateAngleX = 4.712389F;
            wolfLeg3.rotateAngleX = 5.811947F;
            wolfLeg3.setRotationPoint(-2.49F, 17F, -4F);
            wolfLeg4.rotateAngleX = 5.811947F;
            wolfLeg4.setRotationPoint(0.51F, 17F, -4F);
        } else
        {
            wolfBody.setRotationPoint(0.0F, 14F, 2.0F);
            wolfBody.rotateAngleX = 1.570796F;
            wolfMane.setRotationPoint(-1F, 14F, -3F);
            wolfMane.rotateAngleX = wolfBody.rotateAngleX;
            wolfTail.setRotationPoint(-1F, 12F, 8F);
            wolfLeg1.setRotationPoint(-2.5F, 16F, 7F);
            wolfLeg2.setRotationPoint(0.5F, 16F, 7F);
            wolfLeg3.setRotationPoint(-2.5F, 16F, -4F);
            wolfLeg4.setRotationPoint(0.5F, 16F, -4F);
            wolfLeg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
            wolfLeg2.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
            wolfLeg3.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
            wolfLeg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        }
        float f3 = entitywolf.getInterestedAngle(f2) + entitywolf.getShakeAngle(f2, 0.0F);
        wolfHeadMain.rotateAngleZ = f3;
        wolfRightEar.rotateAngleZ = f3;
        wolfLeftEar.rotateAngleZ = f3;
        wolfSnout.rotateAngleZ = f3;
        wolfMane.rotateAngleZ = entitywolf.getShakeAngle(f2, -0.08F);
        wolfBody.rotateAngleZ = entitywolf.getShakeAngle(f2, -0.16F);
        wolfTail.rotateAngleZ = entitywolf.getShakeAngle(f2, -0.2F);
        if(entitywolf.getWolfShaking())
        {
            float f4 = entitywolf.getEntityBrightness(f2) * entitywolf.getShadingWhileShaking(f2);
            GL11.glColor3f(f4, f4, f4);
        }
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
        wolfHeadMain.rotateAngleX = f4 / 57.29578F;
        wolfHeadMain.rotateAngleY = f3 / 57.29578F;
        wolfRightEar.rotateAngleY = wolfHeadMain.rotateAngleY;
        wolfRightEar.rotateAngleX = wolfHeadMain.rotateAngleX;
        wolfLeftEar.rotateAngleY = wolfHeadMain.rotateAngleY;
        wolfLeftEar.rotateAngleX = wolfHeadMain.rotateAngleX;
        wolfSnout.rotateAngleY = wolfHeadMain.rotateAngleY;
        wolfSnout.rotateAngleX = wolfHeadMain.rotateAngleX;
        wolfTail.rotateAngleX = f2;
    }

    public ModelRenderer wolfHeadMain;
    public ModelRenderer wolfBody;
    public ModelRenderer wolfLeg1;
    public ModelRenderer wolfLeg2;
    public ModelRenderer wolfLeg3;
    public ModelRenderer wolfLeg4;
    ModelRenderer wolfRightEar;
    ModelRenderer wolfLeftEar;
    ModelRenderer wolfSnout;
    ModelRenderer wolfTail;
    ModelRenderer wolfMane;
}
