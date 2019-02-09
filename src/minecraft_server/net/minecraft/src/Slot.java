// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            IInventory, ItemStack

public class Slot
{

    public Slot(IInventory iinventory, int i, int j, int k)
    {
        inventory = iinventory;
        slotIndex = i;
        xDisplayPosition = j;
        yDisplayPosition = k;
    }

    public void onPickupFromSlot(ItemStack itemstack)
    {
        onSlotChanged();
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return true;
    }

    public ItemStack getStack()
    {
        return inventory.getStackInSlot(slotIndex);
    }

    public boolean func_27006_b()
    {
        return getStack() != null;
    }

    public void putStack(ItemStack itemstack)
    {
        inventory.setInventorySlotContents(slotIndex, itemstack);
        onSlotChanged();
    }

    public void onSlotChanged()
    {
        inventory.onInventoryChanged();
    }

    public int getSlotStackLimit()
    {
        return inventory.getInventoryStackLimit();
    }

    public ItemStack decrStackSize(int i)
    {
        return inventory.decrStackSize(slotIndex, i);
    }

    public boolean isHere(IInventory iinventory, int i)
    {
        return iinventory == inventory && i == slotIndex;
    }

    private final int slotIndex;
    private final IInventory inventory;
    public int id;
    public int xDisplayPosition;
    public int yDisplayPosition;
}
