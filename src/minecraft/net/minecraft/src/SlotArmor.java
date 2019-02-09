// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Slot, ItemStack, ItemArmor, Item, 
//            Block, ContainerPlayer, IInventory

class SlotArmor extends Slot
{

    SlotArmor(ContainerPlayer containerplayer, IInventory iinventory, int i, int j, int k, int l)
    {
        super(iinventory, i, j, k);
        inventory = containerplayer;
        armorType = l;
    }

    public int getSlotStackLimit()
    {
        return 1;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        if(itemstack.getItem() instanceof ItemArmor)
        {
            return ((ItemArmor)itemstack.getItem()).armorType == armorType;
        }
        if(itemstack.getItem().shiftedIndex == Block.pumpkin.blockID)
        {
            return armorType == 0;
        } else
        {
            return false;
        }
    }

    final int armorType; /* synthetic field */
    final ContainerPlayer inventory; /* synthetic field */
}
