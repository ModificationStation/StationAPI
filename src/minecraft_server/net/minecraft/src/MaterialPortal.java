// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Material, MapColor

public class MaterialPortal extends Material
{

    public MaterialPortal(MapColor mapcolor)
    {
        super(mapcolor);
    }

    public boolean isSolid()
    {
        return false;
    }

    public boolean getCanBlockGrass()
    {
        return false;
    }

    public boolean getIsSolid()
    {
        return false;
    }
}
