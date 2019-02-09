// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Slot, EntityPlayer, ItemStack, Block, 
//            AchievementList, Item, IInventory

public class SlotCrafting extends Slot
{

    public SlotCrafting(EntityPlayer entityplayer, IInventory iinventory, IInventory iinventory1, int i, int j, int k)
    {
        super(iinventory1, i, j, k);
        field_25004_e = entityplayer;
        craftMatrix = iinventory;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

    public void onPickupFromSlot(ItemStack itemstack)
    {
        itemstack.func_28142_b(field_25004_e.worldObj, field_25004_e);
        if(itemstack.itemID == Block.workbench.blockID)
        {
            field_25004_e.addStat(AchievementList.field_25130_d, 1);
        } else
        if(itemstack.itemID == Item.pickaxeWood.shiftedIndex)
        {
            field_25004_e.addStat(AchievementList.field_27110_i, 1);
        } else
        if(itemstack.itemID == Block.stoneOvenIdle.blockID)
        {
            field_25004_e.addStat(AchievementList.field_27109_j, 1);
        } else
        if(itemstack.itemID == Item.hoeWood.shiftedIndex)
        {
            field_25004_e.addStat(AchievementList.field_27107_l, 1);
        } else
        if(itemstack.itemID == Item.bread.shiftedIndex)
        {
            field_25004_e.addStat(AchievementList.field_27106_m, 1);
        } else
        if(itemstack.itemID == Item.cake.shiftedIndex)
        {
            field_25004_e.addStat(AchievementList.field_27105_n, 1);
        } else
        if(itemstack.itemID == Item.pickaxeStone.shiftedIndex)
        {
            field_25004_e.addStat(AchievementList.field_27104_o, 1);
        } else
        if(itemstack.itemID == Item.swordWood.shiftedIndex)
        {
            field_25004_e.addStat(AchievementList.field_27101_r, 1);
        }
        for(int i = 0; i < craftMatrix.getSizeInventory(); i++)
        {
            ItemStack itemstack1 = craftMatrix.getStackInSlot(i);
            if(itemstack1 == null)
            {
                continue;
            }
            craftMatrix.decrStackSize(i, 1);
            if(itemstack1.getItem().hasContainerItem())
            {
                craftMatrix.setInventorySlotContents(i, new ItemStack(itemstack1.getItem().getContainerItem()));
            }
        }

    }

    private final IInventory craftMatrix;
    private EntityPlayer field_25004_e;
}
