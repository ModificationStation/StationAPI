// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Vec3D

public class PositionTextureVertex
{

    public PositionTextureVertex(float f, float f1, float f2, float f3, float f4)
    {
        this(Vec3D.createVectorHelper(f, f1, f2), f3, f4);
    }

    public PositionTextureVertex setTexturePosition(float f, float f1)
    {
        return new PositionTextureVertex(this, f, f1);
    }

    public PositionTextureVertex(PositionTextureVertex positiontexturevertex, float f, float f1)
    {
        vector3D = positiontexturevertex.vector3D;
        texturePositionX = f;
        texturePositionY = f1;
    }

    public PositionTextureVertex(Vec3D vec3d, float f, float f1)
    {
        vector3D = vec3d;
        texturePositionX = f;
        texturePositionY = f1;
    }

    public Vec3D vector3D;
    public float texturePositionX;
    public float texturePositionY;
}
