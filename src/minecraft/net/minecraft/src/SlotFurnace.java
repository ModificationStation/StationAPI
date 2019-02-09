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
        thePlayer = entityplayer;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

    public void onPickupFromSlot(ItemStack itemstack)
    {
        itemstack.onCrafting(thePlayer.worldObj, thePlayer);
        if(itemstack.itemID == Item.ingotIron.shiftedIndex)
        {
            thePlayer.addStat(AchievementList.acquireIron, 1);
        }
        if(itemstack.itemID == Item.fishCooked.shiftedIndex)
        {
            thePlayer.addStat(AchievementList.cookFish, 1);
        }
        super.onPickupFromSlot(itemstack);
    }

    private EntityPlayer thePlayer;
}
