// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            BlockContainer, Material, Block, World, 
//            TileEntityFurnace, EntityPlayer, TileEntity, EntityLiving, 
//            MathHelper, IInventory, ItemStack, EntityItem

public class BlockFurnace extends BlockContainer
{

    protected BlockFurnace(int i, boolean flag)
    {
        super(i, Material.rock);
        field_28033_a = new Random();
        isActive = flag;
        blockIndexInTexture = 45;
    }

    public int idDropped(int i, Random random)
    {
        return Block.stoneOvenIdle.blockID;
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
        setDefaultDirection(world, i, j, k);
    }

    private void setDefaultDirection(World world, int i, int j, int k)
    {
        if(world.singleplayerWorld)
        {
            return;
        }
        int l = world.getBlockId(i, j, k - 1);
        int i1 = world.getBlockId(i, j, k + 1);
        int j1 = world.getBlockId(i - 1, j, k);
        int k1 = world.getBlockId(i + 1, j, k);
        byte byte0 = 3;
        if(Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1])
        {
            byte0 = 3;
        }
        if(Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l])
        {
            byte0 = 2;
        }
        if(Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1])
        {
            byte0 = 5;
        }
        if(Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1])
        {
            byte0 = 4;
        }
        world.setBlockMetadataWithNotify(i, j, k, byte0);
    }

    public int getBlockTextureFromSide(int i)
    {
        if(i == 1)
        {
            return blockIndexInTexture + 17;
        }
        if(i == 0)
        {
            return blockIndexInTexture + 17;
        }
        if(i == 3)
        {
            return blockIndexInTexture - 1;
        } else
        {
            return blockIndexInTexture;
        }
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.singleplayerWorld)
        {
            return true;
        } else
        {
            TileEntityFurnace tileentityfurnace = (TileEntityFurnace)world.getBlockTileEntity(i, j, k);
            entityplayer.displayGUIFurnace(tileentityfurnace);
            return true;
        }
    }

    public static void updateFurnaceBlockState(boolean flag, World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        field_28034_c = true;
        if(flag)
        {
            world.setBlockWithNotify(i, j, k, Block.stoneOvenActive.blockID);
        } else
        {
            world.setBlockWithNotify(i, j, k, Block.stoneOvenIdle.blockID);
        }
        field_28034_c = false;
        world.setBlockMetadataWithNotify(i, j, k, l);
        tileentity.validate();
        world.setBlockTileEntity(i, j, k, tileentity);
    }

    protected TileEntity getBlockEntity()
    {
        return new TileEntityFurnace();
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if(l == 0)
        {
            world.setBlockMetadataWithNotify(i, j, k, 2);
        }
        if(l == 1)
        {
            world.setBlockMetadataWithNotify(i, j, k, 5);
        }
        if(l == 2)
        {
            world.setBlockMetadataWithNotify(i, j, k, 3);
        }
        if(l == 3)
        {
            world.setBlockMetadataWithNotify(i, j, k, 4);
        }
    }

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        if(!field_28034_c)
        {
            TileEntityFurnace tileentityfurnace = (TileEntityFurnace)world.getBlockTileEntity(i, j, k);
label0:
            for(int l = 0; l < tileentityfurnace.getSizeInventory(); l++)
            {
                ItemStack itemstack = tileentityfurnace.getStackInSlot(l);
                if(itemstack == null)
                {
                    continue;
                }
                float f = field_28033_a.nextFloat() * 0.8F + 0.1F;
                float f1 = field_28033_a.nextFloat() * 0.8F + 0.1F;
                float f2 = field_28033_a.nextFloat() * 0.8F + 0.1F;
                do
                {
                    if(itemstack.stackSize <= 0)
                    {
                        continue label0;
                    }
                    int i1 = field_28033_a.nextInt(21) + 10;
                    if(i1 > itemstack.stackSize)
                    {
                        i1 = itemstack.stackSize;
                    }
                    itemstack.stackSize -= i1;
                    EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                    float f3 = 0.05F;
                    entityitem.motionX = (float)field_28033_a.nextGaussian() * f3;
                    entityitem.motionY = (float)field_28033_a.nextGaussian() * f3 + 0.2F;
                    entityitem.motionZ = (float)field_28033_a.nextGaussian() * f3;
                    world.entityJoinedWorld(entityitem);
                } while(true);
            }

        }
        super.onBlockRemoval(world, i, j, k);
    }

    private Random field_28033_a;
    private final boolean isActive;
    private static boolean field_28034_c = false;

}
