// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Slot, EntityPlayer, ItemStack, Item, 
//            AchievementList, IInventory

public class SlotFurnace extends Slot
{

    public SlotFurnace(EntityPlayer entityplayer, IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        field_27007_d = entityplayer;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

    public void onPickupFromSlot(ItemStack itemstack)
    {
        itemstack.func_28142_b(field_27007_d.worldObj, field_27007_d);
        if(itemstack.itemID == Item.ingotIron.shiftedIndex)
        {
            field_27007_d.addStat(AchievementList.field_27108_k, 1);
        }
        if(itemstack.itemID == Item.fishCooked.shiftedIndex)
        {
            field_27007_d.addStat(AchievementList.field_27103_p, 1);
        }
        super.onPickupFromSlot(itemstack);
    }

    private EntityPlayer field_27007_d;
}
