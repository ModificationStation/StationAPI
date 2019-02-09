// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            WorldServer, Block, EntityPlayer, ItemStack, 
//            EntityPlayerMP, Packet53BlockChange, NetServerHandler, InventoryPlayer, 
//            World

public class ItemInWorldManager
{

    public ItemInWorldManager(WorldServer worldserver)
    {
        field_672_d = 0.0F;
        thisWorld = worldserver;
    }

    public void func_328_a()
    {
        field_22051_j++;
        if(field_22050_k)
        {
            int i = field_22051_j - field_22046_o;
            int j = thisWorld.getBlockId(field_22049_l, field_22048_m, field_22047_n);
            if(j != 0)
            {
                Block block = Block.blocksList[j];
                float f = block.blockStrength(thisPlayer) * (float)(i + 1);
                if(f >= 1.0F)
                {
                    field_22050_k = false;
                    func_325_c(field_22049_l, field_22048_m, field_22047_n);
                }
            } else
            {
                field_22050_k = false;
            }
        }
    }

    public void func_324_a(int i, int j, int k, int l)
    {
        thisWorld.func_28096_a(null, i, j, k, l);
        field_22055_d = field_22051_j;
        int i1 = thisWorld.getBlockId(i, j, k);
        if(i1 > 0)
        {
            Block.blocksList[i1].onBlockClicked(thisWorld, i, j, k, thisPlayer);
        }
        if(i1 > 0 && Block.blocksList[i1].blockStrength(thisPlayer) >= 1.0F)
        {
            func_325_c(i, j, k);
        } else
        {
            field_22054_g = i;
            field_22053_h = j;
            field_22052_i = k;
        }
    }

    public void func_22045_b(int i, int j, int k)
    {
        if(i == field_22054_g && j == field_22053_h && k == field_22052_i)
        {
            int l = field_22051_j - field_22055_d;
            int i1 = thisWorld.getBlockId(i, j, k);
            if(i1 != 0)
            {
                Block block = Block.blocksList[i1];
                float f = block.blockStrength(thisPlayer) * (float)(l + 1);
                if(f >= 0.7F)
                {
                    func_325_c(i, j, k);
                } else
                if(!field_22050_k)
                {
                    field_22050_k = true;
                    field_22049_l = i;
                    field_22048_m = j;
                    field_22047_n = k;
                    field_22046_o = field_22055_d;
                }
            }
        }
        field_672_d = 0.0F;
    }

    public boolean removeBlock(int i, int j, int k)
    {
        Block block = Block.blocksList[thisWorld.getBlockId(i, j, k)];
        int l = thisWorld.getBlockMetadata(i, j, k);
        boolean flag = thisWorld.setBlockWithNotify(i, j, k, 0);
        if(block != null && flag)
        {
            block.onBlockDestroyedByPlayer(thisWorld, i, j, k, l);
        }
        return flag;
    }

    public boolean func_325_c(int i, int j, int k)
    {
        int l = thisWorld.getBlockId(i, j, k);
        int i1 = thisWorld.getBlockMetadata(i, j, k);
        thisWorld.func_28101_a(thisPlayer, 2001, i, j, k, l + thisWorld.getBlockMetadata(i, j, k) * 256);
        boolean flag = removeBlock(i, j, k);
        ItemStack itemstack = thisPlayer.getCurrentEquippedItem();
        if(itemstack != null)
        {
            itemstack.func_25124_a(l, i, j, k, thisPlayer);
            if(itemstack.stackSize == 0)
            {
                itemstack.func_577_a(thisPlayer);
                thisPlayer.destroyCurrentEquippedItem();
            }
        }
        if(flag && thisPlayer.canHarvestBlock(Block.blocksList[l]))
        {
            Block.blocksList[l].harvestBlock(thisWorld, thisPlayer, i, j, k, i1);
            ((EntityPlayerMP)thisPlayer).playerNetServerHandler.sendPacket(new Packet53BlockChange(i, j, k, thisWorld));
        }
        return flag;
    }

    public boolean func_6154_a(EntityPlayer entityplayer, World world, ItemStack itemstack)
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

    public boolean activeBlockOrUseItem(EntityPlayer entityplayer, World world, ItemStack itemstack, int i, int j, int k, int l)
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

    private WorldServer thisWorld;
    public EntityPlayer thisPlayer;
    private float field_672_d;
    private int field_22055_d;
    private int field_22054_g;
    private int field_22053_h;
    private int field_22052_i;
    private int field_22051_j;
    private boolean field_22050_k;
    private int field_22049_l;
    private int field_22048_m;
    private int field_22047_n;
    private int field_22046_o;
}
