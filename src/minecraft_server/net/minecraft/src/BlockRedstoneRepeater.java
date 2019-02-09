// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Block, Material, World, IBlockAccess, 
//            EntityLiving, MathHelper, Item, EntityPlayer

public class BlockRedstoneRepeater extends Block
{

    protected BlockRedstoneRepeater(int i, boolean flag)
    {
        super(i, 6, Material.circuits);
        field_22015_c = flag;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    public boolean isACube()
    {
        return false;
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        if(!world.isBlockNormalCube(i, j - 1, k))
        {
            return false;
        } else
        {
            return super.canPlaceBlockAt(world, i, j, k);
        }
    }

    public boolean canBlockStay(World world, int i, int j, int k)
    {
        if(!world.isBlockNormalCube(i, j - 1, k))
        {
            return false;
        } else
        {
            return super.canBlockStay(world, i, j, k);
        }
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        int l = world.getBlockMetadata(i, j, k);
        boolean flag = func_22012_g(world, i, j, k, l);
        if(field_22015_c && !flag)
        {
            world.setBlockAndMetadataWithNotify(i, j, k, Block.redstoneRepeaterIdle.blockID, l);
        } else
        if(!field_22015_c)
        {
            world.setBlockAndMetadataWithNotify(i, j, k, Block.redstoneRepeaterActive.blockID, l);
            if(!flag)
            {
                int i1 = (l & 0xc) >> 2;
                world.scheduleUpdateTick(i, j, k, Block.redstoneRepeaterActive.blockID, field_22013_b[i1] * 2);
            }
        }
    }

    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if(i == 0)
        {
            return !field_22015_c ? 115 : 99;
        }
        if(i == 1)
        {
            return !field_22015_c ? 131 : 147;
        } else
        {
            return 5;
        }
    }

    public int getBlockTextureFromSide(int i)
    {
        return getBlockTextureFromSideAndMetadata(i, 0);
    }

    public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l)
    {
        return isPoweringTo(world, i, j, k, l);
    }

    public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        if(!field_22015_c)
        {
            return false;
        }
        int i1 = iblockaccess.getBlockMetadata(i, j, k) & 3;
        if(i1 == 0 && l == 3)
        {
            return true;
        }
        if(i1 == 1 && l == 4)
        {
            return true;
        }
        if(i1 == 2 && l == 2)
        {
            return true;
        }
        return i1 == 3 && l == 5;
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if(!canBlockStay(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k));
            world.setBlockWithNotify(i, j, k, 0);
            return;
        }
        int i1 = world.getBlockMetadata(i, j, k);
        boolean flag = func_22012_g(world, i, j, k, i1);
        int j1 = (i1 & 0xc) >> 2;
        if(field_22015_c && !flag)
        {
            world.scheduleUpdateTick(i, j, k, blockID, field_22013_b[j1] * 2);
        } else
        if(!field_22015_c && flag)
        {
            world.scheduleUpdateTick(i, j, k, blockID, field_22013_b[j1] * 2);
        }
    }

    private boolean func_22012_g(World world, int i, int j, int k, int l)
    {
        int i1 = l & 3;
        switch(i1)
        {
        case 0: // '\0'
            return world.isBlockIndirectlyProvidingPowerTo(i, j, k + 1, 3) || world.getBlockId(i, j, k + 1) == Block.redstoneWire.blockID && world.getBlockMetadata(i, j, k + 1) > 0;

        case 2: // '\002'
            return world.isBlockIndirectlyProvidingPowerTo(i, j, k - 1, 2) || world.getBlockId(i, j, k - 1) == Block.redstoneWire.blockID && world.getBlockMetadata(i, j, k - 1) > 0;

        case 3: // '\003'
            return world.isBlockIndirectlyProvidingPowerTo(i + 1, j, k, 5) || world.getBlockId(i + 1, j, k) == Block.redstoneWire.blockID && world.getBlockMetadata(i + 1, j, k) > 0;

        case 1: // '\001'
            return world.isBlockIndirectlyProvidingPowerTo(i - 1, j, k, 4) || world.getBlockId(i - 1, j, k) == Block.redstoneWire.blockID && world.getBlockMetadata(i - 1, j, k) > 0;
        }
        return false;
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        int l = world.getBlockMetadata(i, j, k);
        int i1 = (l & 0xc) >> 2;
        i1 = i1 + 1 << 2 & 0xc;
        world.setBlockMetadataWithNotify(i, j, k, i1 | l & 3);
        return true;
    }

    public boolean canProvidePower()
    {
        return false;
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
        int l = ((MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;
        world.setBlockMetadataWithNotify(i, j, k, l);
        boolean flag = func_22012_g(world, i, j, k, l);
        if(flag)
        {
            world.scheduleUpdateTick(i, j, k, blockID, 1);
        }
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
        world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
        world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
        world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
        world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
        world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public int idDropped(int i, Random random)
    {
        return Item.redstoneRepeater.shiftedIndex;
    }

    public static final double field_22014_a[] = {
        -0.0625D, 0.0625D, 0.1875D, 0.3125D
    };
    private static final int field_22013_b[] = {
        1, 2, 3, 4
    };
    private final boolean field_22015_c;

}
