// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Block, Material, World, IBlockAccess, 
//            Vec3D, AxisAlignedBB, Entity

public abstract class BlockFluid extends Block
{

    protected BlockFluid(int i, Material material)
    {
        super(i, (material != Material.lava ? 12 : 14) * 16 + 13, material);
        float f = 0.0F;
        float f1 = 0.0F;
        setBlockBounds(0.0F + f1, 0.0F + f, 0.0F + f1, 1.0F + f1, 1.0F + f, 1.0F + f1);
        setTickOnLoad(true);
    }

    public static float setFluidHeight(int i)
    {
        if(i >= 8)
        {
            i = 0;
        }
        float f = (float)(i + 1) / 9F;
        return f;
    }

    public int getBlockTextureFromSide(int i)
    {
        if(i == 0 || i == 1)
        {
            return blockIndexInTexture;
        } else
        {
            return blockIndexInTexture + 1;
        }
    }

    protected int func_301_g(World world, int i, int j, int k)
    {
        if(world.getBlockMaterial(i, j, k) != blockMaterial)
        {
            return -1;
        } else
        {
            return world.getBlockMetadata(i, j, k);
        }
    }

    protected int func_303_b(IBlockAccess iblockaccess, int i, int j, int k)
    {
        if(iblockaccess.getBlockMaterial(i, j, k) != blockMaterial)
        {
            return -1;
        }
        int l = iblockaccess.getBlockMetadata(i, j, k);
        if(l >= 8)
        {
            l = 0;
        }
        return l;
    }

    public boolean isACube()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean canCollideCheck(int i, boolean flag)
    {
        return flag && i == 0;
    }

    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        Material material = iblockaccess.getBlockMaterial(i, j, k);
        if(material == blockMaterial)
        {
            return false;
        }
        if(material == Material.ice)
        {
            return false;
        }
        if(l == 1)
        {
            return true;
        } else
        {
            return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }

    public int idDropped(int i, Random random)
    {
        return 0;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    private Vec3D func_298_c(IBlockAccess iblockaccess, int i, int j, int k)
    {
        Vec3D vec3d = Vec3D.createVector(0.0D, 0.0D, 0.0D);
        int l = func_303_b(iblockaccess, i, j, k);
        for(int i1 = 0; i1 < 4; i1++)
        {
            int j1 = i;
            int k1 = j;
            int l1 = k;
            if(i1 == 0)
            {
                j1--;
            }
            if(i1 == 1)
            {
                l1--;
            }
            if(i1 == 2)
            {
                j1++;
            }
            if(i1 == 3)
            {
                l1++;
            }
            int i2 = func_303_b(iblockaccess, j1, k1, l1);
            if(i2 < 0)
            {
                if(iblockaccess.getBlockMaterial(j1, k1, l1).getIsSolid())
                {
                    continue;
                }
                i2 = func_303_b(iblockaccess, j1, k1 - 1, l1);
                if(i2 >= 0)
                {
                    int j2 = i2 - (l - 8);
                    vec3d = vec3d.addVector((j1 - i) * j2, (k1 - j) * j2, (l1 - k) * j2);
                }
                continue;
            }
            if(i2 >= 0)
            {
                int k2 = i2 - l;
                vec3d = vec3d.addVector((j1 - i) * k2, (k1 - j) * k2, (l1 - k) * k2);
            }
        }

        if(iblockaccess.getBlockMetadata(i, j, k) >= 8)
        {
            boolean flag = false;
            if(flag || shouldSideBeRendered(iblockaccess, i, j, k - 1, 2))
            {
                flag = true;
            }
            if(flag || shouldSideBeRendered(iblockaccess, i, j, k + 1, 3))
            {
                flag = true;
            }
            if(flag || shouldSideBeRendered(iblockaccess, i - 1, j, k, 4))
            {
                flag = true;
            }
            if(flag || shouldSideBeRendered(iblockaccess, i + 1, j, k, 5))
            {
                flag = true;
            }
            if(flag || shouldSideBeRendered(iblockaccess, i, j + 1, k - 1, 2))
            {
                flag = true;
            }
            if(flag || shouldSideBeRendered(iblockaccess, i, j + 1, k + 1, 3))
            {
                flag = true;
            }
            if(flag || shouldSideBeRendered(iblockaccess, i - 1, j + 1, k, 4))
            {
                flag = true;
            }
            if(flag || shouldSideBeRendered(iblockaccess, i + 1, j + 1, k, 5))
            {
                flag = true;
            }
            if(flag)
            {
                vec3d = vec3d.normalize().addVector(0.0D, -6D, 0.0D);
            }
        }
        vec3d = vec3d.normalize();
        return vec3d;
    }

    public void velocityToAddToEntity(World world, int i, int j, int k, Entity entity, Vec3D vec3d)
    {
        Vec3D vec3d1 = func_298_c(world, i, j, k);
        vec3d.xCoord += vec3d1.xCoord;
        vec3d.yCoord += vec3d1.yCoord;
        vec3d.zCoord += vec3d1.zCoord;
    }

    public int tickRate()
    {
        if(blockMaterial == Material.water)
        {
            return 5;
        }
        return blockMaterial != Material.lava ? 0 : 30;
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        super.updateTick(world, i, j, k, random);
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        checkForHarden(world, i, j, k);
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        checkForHarden(world, i, j, k);
    }

    private void checkForHarden(World world, int i, int j, int k)
    {
        if(world.getBlockId(i, j, k) != blockID)
        {
            return;
        }
        if(blockMaterial == Material.lava)
        {
            boolean flag = false;
            if(flag || world.getBlockMaterial(i, j, k - 1) == Material.water)
            {
                flag = true;
            }
            if(flag || world.getBlockMaterial(i, j, k + 1) == Material.water)
            {
                flag = true;
            }
            if(flag || world.getBlockMaterial(i - 1, j, k) == Material.water)
            {
                flag = true;
            }
            if(flag || world.getBlockMaterial(i + 1, j, k) == Material.water)
            {
                flag = true;
            }
            if(flag || world.getBlockMaterial(i, j + 1, k) == Material.water)
            {
                flag = true;
            }
            if(flag)
            {
                int l = world.getBlockMetadata(i, j, k);
                if(l == 0)
                {
                    world.setBlockWithNotify(i, j, k, Block.obsidian.blockID);
                } else
                if(l <= 4)
                {
                    world.setBlockWithNotify(i, j, k, Block.cobblestone.blockID);
                }
                func_300_h(world, i, j, k);
            }
        }
    }

    protected void func_300_h(World world, int i, int j, int k)
    {
        world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        for(int l = 0; l < 8; l++)
        {
            world.spawnParticle("largesmoke", (double)i + Math.random(), (double)j + 1.2D, (double)k + Math.random(), 0.0D, 0.0D, 0.0D);
        }

    }
}
