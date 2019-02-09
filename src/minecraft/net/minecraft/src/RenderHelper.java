// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            Vec3D, GLAllocation

public class RenderHelper
{

    public RenderHelper()
    {
    }

    public static void disableStandardItemLighting()
    {
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glDisable(16384 /*GL_LIGHT0*/);
        GL11.glDisable(16385 /*GL_LIGHT1*/);
        GL11.glDisable(2903 /*GL_COLOR_MATERIAL*/);
    }

    public static void enableStandardItemLighting()
    {
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(16384 /*GL_LIGHT0*/);
        GL11.glEnable(16385 /*GL_LIGHT1*/);
        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
        GL11.glColorMaterial(1032 /*GL_FRONT_AND_BACK*/, 5634 /*GL_AMBIENT_AND_DIFFUSE*/);
        float f = 0.4F;
        float f1 = 0.6F;
        float f2 = 0.0F;
        Vec3D vec3d = Vec3D.createVector(0.20000000298023224D, 1.0D, -0.69999998807907104D).normalize();
        GL11.glLight(16384 /*GL_LIGHT0*/, 4611 /*GL_POSITION*/, func_1157_a(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 0.0D));
        GL11.glLight(16384 /*GL_LIGHT0*/, 4609 /*GL_DIFFUSE*/, func_1156_a(f1, f1, f1, 1.0F));
        GL11.glLight(16384 /*GL_LIGHT0*/, 4608 /*GL_AMBIENT*/, func_1156_a(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glLight(16384 /*GL_LIGHT0*/, 4610 /*GL_SPECULAR*/, func_1156_a(f2, f2, f2, 1.0F));
        vec3d = Vec3D.createVector(-0.20000000298023224D, 1.0D, 0.69999998807907104D).normalize();
        GL11.glLight(16385 /*GL_LIGHT1*/, 4611 /*GL_POSITION*/, func_1157_a(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 0.0D));
        GL11.glLight(16385 /*GL_LIGHT1*/, 4609 /*GL_DIFFUSE*/, func_1156_a(f1, f1, f1, 1.0F));
        GL11.glLight(16385 /*GL_LIGHT1*/, 4608 /*GL_AMBIENT*/, func_1156_a(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glLight(16385 /*GL_LIGHT1*/, 4610 /*GL_SPECULAR*/, func_1156_a(f2, f2, f2, 1.0F));
        GL11.glShadeModel(7424 /*GL_FLAT*/);
        GL11.glLightModel(2899 /*GL_LIGHT_MODEL_AMBIENT*/, func_1156_a(f, f, f, 1.0F));
    }

    private static FloatBuffer func_1157_a(double d, double d1, double d2, double d3)
    {
        return func_1156_a((float)d, (float)d1, (float)d2, (float)d3);
    }

    private static FloatBuffer func_1156_a(float f, float f1, float f2, float f3)
    {
        field_1695_a.clear();
        field_1695_a.put(f).put(f1).put(f2).put(f3);
        field_1695_a.flip();
        return field_1695_a;
    }

    private static FloatBuffer field_1695_a = GLAllocation.createDirectFloatBuffer(16);

}
