// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//            World, Block, ItemStack, EntityPlayer, 
//            InventoryPlayer, EntityPlayerSP, WorldProvider, Container, 
//            Entity

public class PlayerController
{

    public PlayerController(Minecraft minecraft)
    {
        field_1064_b = false;
        mc = minecraft;
    }

    public void func_717_a(World world)
    {
    }

    public void clickBlock(int i, int j, int k, int l)
    {
        mc.theWorld.onBlockHit(mc.thePlayer, i, j, k, l);
        sendBlockRemoved(i, j, k, l);
    }

    public boolean sendBlockRemoved(int i, int j, int k, int l)
    {
        World world = mc.theWorld;
        Block block = Block.blocksList[world.getBlockId(i, j, k)];
        world.func_28106_e(2001, i, j, k, block.blockID + world.getBlockMetadata(i, j, k) * 256);
        int i1 = world.getBlockMetadata(i, j, k);
        boolean flag = world.setBlockWithNotify(i, j, k, 0);
        if(block != null && flag)
        {
            block.onBlockDestroyedByPlayer(world, i, j, k, i1);
        }
        return flag;
    }

    public void sendBlockRemoving(int i, int j, int k, int l)
    {
    }

    public void resetBlockRemoving()
    {
    }

    public void setPartialTime(float f)
    {
    }

    public float getBlockReachDistance()
    {
        return 5F;
    }

    public boolean sendUseItem(EntityPlayer entityplayer, World world, ItemStack itemstack)
    {
        int i = itemstack.stackSize;
        ItemStack itemstack1 = itemstack.useItemRightClick(world, entityplayer);
        if(itemstack1 != itemstack || itemstack1 != null && itemstack1.stackSize != i)
        {
            entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = itemstack1;
            if(itemstack1.stackSize == 0)
            {
                entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
            }
            return true;
        } else
        {
            return false;
        }
    }

    public void flipPlayer(EntityPlayer entityplayer)
    {
    }

    public void updateController()
    {
    }

    public boolean shouldDrawHUD()
    {
        return true;
    }

    public void func_6473_b(EntityPlayer entityplayer)
    {
    }

    public boolean sendPlaceBlock(EntityPlayer entityplayer, World world, ItemStack itemstack, int i, int j, int k, int l)
    {
        int i1 = world.getBlockId(i, j, k);
        if(i1 > 0 && Block.blocksList[i1].blockActivated(world, i, j, k, entityplayer))
        {
            return true;
        }
        if(itemstack == null)
        {
            return false;
        } else
        {
            return itemstack.useItem(entityplayer, world, i, j, k, l);
        }
    }

    public EntityPlayer createPlayer(World world)
    {
        return new EntityPlayerSP(mc, world, mc.session, world.worldProvider.worldType);
    }

    public void interactWithEntity(EntityPlayer entityplayer, Entity entity)
    {
        entityplayer.useCurrentItemOnEntity(entity);
    }

    public void attackEntity(EntityPlayer entityplayer, Entity entity)
    {
        entityplayer.attackTargetEntityWithCurrentItem(entity);
    }

    public ItemStack func_27174_a(int i, int j, int k, boolean flag, EntityPlayer entityplayer)
    {
        return entityplayer.craftingInventory.func_27280_a(j, k, flag, entityplayer);
    }

    public void func_20086_a(int i, EntityPlayer entityplayer)
    {
        entityplayer.craftingInventory.onCraftGuiClosed(entityplayer);
        entityplayer.craftingInventory = entityplayer.inventorySlots;
    }

    protected final Minecraft mc;
    public boolean field_1064_b;
}
