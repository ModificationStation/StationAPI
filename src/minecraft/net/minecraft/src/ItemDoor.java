// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Item, Material, Block, EntityPlayer, 
//            MathHelper, World, ItemStack

public class ItemDoor extends Item
{

    public ItemDoor(int i, Material material)
    {
        super(i);
        doorMaterial = material;
        maxStackSize = 1;
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        if(l != 1)
        {
            return false;
        }
        j++;
        Block block;
        if(doorMaterial == Material.wood)
        {
            block = Block.doorWood;
        } else
        {
            block = Block.doorSteel;
        }
        if(!block.canPlaceBlockAt(world, i, j, k))
        {
            return false;
        }
        int i1 = MathHelper.floor_double((double)(((entityplayer.rotationYaw + 180F) * 4F) / 360F) - 0.5D) & 3;
        byte byte0 = 0;
        byte byte1 = 0;
        if(i1 == 0)
        {
            byte1 = 1;
        }
        if(i1 == 1)
        {
            byte0 = -1;
        }
        if(i1 == 2)
        {
            byte1 = -1;
        }
        if(i1 == 3)
        {
            byte0 = 1;
        }
        int j1 = (world.isBlockNormalCube(i - byte0, j, k - byte1) ? 1 : 0) + (world.isBlockNormalCube(i - byte0, j + 1, k - byte1) ? 1 : 0);
        int k1 = (world.isBlockNormalCube(i + byte0, j, k + byte1) ? 1 : 0) + (world.isBlockNormalCube(i + byte0, j + 1, k + byte1) ? 1 : 0);
        boolean flag = world.getBlockId(i - byte0, j, k - byte1) == block.blockID || world.getBlockId(i - byte0, j + 1, k - byte1) == block.blockID;
        boolean flag1 = world.getBlockId(i + byte0, j, k + byte1) == block.blockID || world.getBlockId(i + byte0, j + 1, k + byte1) == block.blockID;
        boolean flag2 = false;
        if(flag && !flag1)
        {
            flag2 = true;
        } else
        if(k1 > j1)
        {
            flag2 = true;
        }
        if(flag2)
        {
            i1 = i1 - 1 & 3;
            i1 += 4;
        }
        world.editingBlocks = true;
        world.setBlockAndMetadataWithNotify(i, j, k, block.blockID, i1);
        world.setBlockAndMetadataWithNotify(i, j + 1, k, block.blockID, i1 + 8);
        world.editingBlocks = false;
        world.notifyBlocksOfNeighborChange(i, j, k, block.blockID);
        world.notifyBlocksOfNeighborChange(i, j + 1, k, block.blockID);
        itemstack.stackSize--;
        return true;
    }

    private Material doorMaterial;
}
