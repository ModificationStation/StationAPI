// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Block, World, Material, IBlockAccess, 
//            RailLogic, AxisAlignedBB, Vec3D, MovingObjectPosition

public class BlockRail extends Block
{

    public static final boolean isRailBlockAt(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j, k);
        return l == Block.rail.blockID || l == Block.railPowered.blockID || l == Block.railDetector.blockID;
    }

    public static final boolean isRailBlock(int i)
    {
        return i == Block.rail.blockID || i == Block.railPowered.blockID || i == Block.railDetector.blockID;
    }

    protected BlockRail(int i, int j, boolean flag)
    {
        super(i, j, Material.circuits);
        isPowered = flag;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    public boolean getIsPowered()
    {
        return isPowered;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3D vec3d, Vec3D vec3d1)
    {
        setBlockBoundsBasedOnState(world, i, j, k);
        return super.collisionRayTrace(world, i, j, k, vec3d, vec3d1);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int l = iblockaccess.getBlockMetadata(i, j, k);
        if(l >= 2 && l <= 5)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        } else
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        }
    }

    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if(isPowered)
        {
            if(blockID == Block.railPowered.blockID && (j & 8) == 0)
            {
                return blockIndexInTexture - 16;
            }
        } else
        if(j >= 6)
        {
            return blockIndexInTexture - 16;
        }
        return blockIndexInTexture;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return 9;
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return world.isBlockNormalCube(i, j - 1, k);
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        if(!world.multiplayerWorld)
        {
            func_4031_h(world, i, j, k, true);
        }
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if(world.multiplayerWorld)
        {
            return;
        }
        int i1 = world.getBlockMetadata(i, j, k);
        int j1 = i1;
        if(isPowered)
        {
            j1 &= 7;
        }
        boolean flag = false;
        if(!world.isBlockNormalCube(i, j - 1, k))
        {
            flag = true;
        }
        if(j1 == 2 && !world.isBlockNormalCube(i + 1, j, k))
        {
            flag = true;
        }
        if(j1 == 3 && !world.isBlockNormalCube(i - 1, j, k))
        {
            flag = true;
        }
        if(j1 == 4 && !world.isBlockNormalCube(i, j, k - 1))
        {
            flag = true;
        }
        if(j1 == 5 && !world.isBlockNormalCube(i, j, k + 1))
        {
            flag = true;
        }
        if(flag)
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k));
            world.setBlockWithNotify(i, j, k, 0);
        } else
        if(blockID == Block.railPowered.blockID)
        {
            boolean flag1 = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k);
            flag1 = flag1 || func_27044_a(world, i, j, k, i1, true, 0) || func_27044_a(world, i, j, k, i1, false, 0);
            boolean flag2 = false;
            if(flag1 && (i1 & 8) == 0)
            {
                world.setBlockMetadataWithNotify(i, j, k, j1 | 8);
                flag2 = true;
            } else
            if(!flag1 && (i1 & 8) != 0)
            {
                world.setBlockMetadataWithNotify(i, j, k, j1);
                flag2 = true;
            }
            if(flag2)
            {
                world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
                if(j1 == 2 || j1 == 3 || j1 == 4 || j1 == 5)
                {
                    world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
                }
            }
        } else
        if(l > 0 && Block.blocksList[l].canProvidePower() && !isPowered && RailLogic.getNAdjacentTracks(new RailLogic(this, world, i, j, k)) == 3)
        {
            func_4031_h(world, i, j, k, false);
        }
    }

    private void func_4031_h(World world, int i, int j, int k, boolean flag)
    {
        if(world.multiplayerWorld)
        {
            return;
        } else
        {
            (new RailLogic(this, world, i, j, k)).func_792_a(world.isBlockIndirectlyGettingPowered(i, j, k), flag);
            return;
        }
    }

    private boolean func_27044_a(World world, int i, int j, int k, int l, boolean flag, int i1)
    {
        if(i1 >= 8)
        {
            return false;
        }
        int j1 = l & 7;
        boolean flag1 = true;
        switch(j1)
        {
        case 0: // '\0'
            if(flag)
            {
                k++;
            } else
            {
                k--;
            }
            break;

        case 1: // '\001'
            if(flag)
            {
                i--;
            } else
            {
                i++;
            }
            break;

        case 2: // '\002'
            if(flag)
            {
                i--;
            } else
            {
                i++;
                j++;
                flag1 = false;
            }
            j1 = 1;
            break;

        case 3: // '\003'
            if(flag)
            {
                i--;
                j++;
                flag1 = false;
            } else
            {
                i++;
            }
            j1 = 1;
            break;

        case 4: // '\004'
            if(flag)
            {
                k++;
            } else
            {
                k--;
                j++;
                flag1 = false;
            }
            j1 = 0;
            break;

        case 5: // '\005'
            if(flag)
            {
                k++;
                j++;
                flag1 = false;
            } else
            {
                k--;
            }
            j1 = 0;
            break;
        }
        if(func_27043_a(world, i, j, k, flag, i1, j1))
        {
            return true;
        }
        return flag1 && func_27043_a(world, i, j - 1, k, flag, i1, j1);
    }

    private boolean func_27043_a(World world, int i, int j, int k, boolean flag, int l, int i1)
    {
        int j1 = world.getBlockId(i, j, k);
        if(j1 == Block.railPowered.blockID)
        {
            int k1 = world.getBlockMetadata(i, j, k);
            int l1 = k1 & 7;
            if(i1 == 1 && (l1 == 0 || l1 == 4 || l1 == 5))
            {
                return false;
            }
            if(i1 == 0 && (l1 == 1 || l1 == 2 || l1 == 3))
            {
                return false;
            }
            if((k1 & 8) != 0)
            {
                if(world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k))
                {
                    return true;
                } else
                {
                    return func_27044_a(world, i, j, k, k1, flag, l + 1);
                }
            }
        }
        return false;
    }

    public int getMobilityFlag()
    {
        return 0;
    }

    static boolean isPoweredBlockRail(BlockRail blockrail)
    {
        return blockrail.isPowered;
    }

    private final boolean isPowered;
}
