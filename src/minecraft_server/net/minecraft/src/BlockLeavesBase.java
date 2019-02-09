// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Block, Material

public class BlockLeavesBase extends Block
{

    protected BlockLeavesBase(int i, int j, Material material, boolean flag)
    {
        super(i, j, material);
        graphicsLevel = flag;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    protected boolean graphicsLevel;
}
