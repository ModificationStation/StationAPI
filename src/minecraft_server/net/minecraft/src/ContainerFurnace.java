// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;

// Referenced classes of package net.minecraft.src:
//            Container, Slot, SlotFurnace, InventoryPlayer, 
//            TileEntityFurnace, ICrafting, ItemStack, EntityPlayer

public class ContainerFurnace extends Container
{

    public ContainerFurnace(InventoryPlayer inventoryplayer, TileEntityFurnace tileentityfurnace)
    {
        lastCookTime = 0;
        lastBurnTime = 0;
        lastItemBurnTime = 0;
        furnace = tileentityfurnace;
        addSlot(new Slot(tileentityfurnace, 0, 56, 17));
        addSlot(new Slot(tileentityfurnace, 1, 56, 53));
        addSlot(new SlotFurnace(inventoryplayer.player, tileentityfurnace, 2, 116, 35));
        for(int i = 0; i < 3; i++)
        {
            for(int k = 0; k < 9; k++)
            {
                addSlot(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }

        }

        for(int j = 0; j < 9; j++)
        {
            addSlot(new Slot(inventoryplayer, j, 8 + j * 18, 142));
        }

    }

    public void onCraftGuiOpened(ICrafting icrafting)
    {
        super.onCraftGuiOpened(icrafting);
        icrafting.updateCraftingInventoryInfo(this, 0, furnace.furnaceCookTime);
        icrafting.updateCraftingInventoryInfo(this, 1, furnace.furnaceBurnTime);
        icrafting.updateCraftingInventoryInfo(this, 2, furnace.currentItemBurnTime);
    }

    public void updateCraftingMatrix()
    {
        super.updateCraftingMatrix();
        for(int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting)crafters.get(i);
            if(lastCookTime != furnace.furnaceCookTime)
            {
                icrafting.updateCraftingInventoryInfo(this, 0, furnace.furnaceCookTime);
            }
            if(lastBurnTime != furnace.furnaceBurnTime)
            {
                icrafting.updateCraftingInventoryInfo(this, 1, furnace.furnaceBurnTime);
            }
            if(lastItemBurnTime != furnace.currentItemBurnTime)
            {
                icrafting.updateCraftingInventoryInfo(this, 2, furnace.currentItemBurnTime);
            }
        }

        lastCookTime = furnace.furnaceCookTime;
        lastBurnTime = furnace.furnaceBurnTime;
        lastItemBurnTime = furnace.currentItemBurnTime;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return furnace.canInteractWith(entityplayer);
    }

    public ItemStack func_27086_a(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if(slot != null && slot.func_27006_b())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i == 2)
            {
                func_28126_a(itemstack1, 3, 39, true);
            } else
            if(i >= 3 && i < 30)
            {
                func_28126_a(itemstack1, 30, 39, false);
            } else
            if(i >= 30 && i < 39)
            {
                func_28126_a(itemstack1, 3, 30, false);
            } else
            {
                func_28126_a(itemstack1, 3, 39, false);
            }
            if(itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            } else
            {
                slot.onSlotChanged();
            }
            if(itemstack1.stackSize != itemstack.stackSize)
            {
                slot.onPickupFromSlot(itemstack1);
            } else
            {
                return null;
            }
        }
        return itemstack;
    }

    private TileEntityFurnace furnace;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
}
