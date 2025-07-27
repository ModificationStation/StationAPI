// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   ContainerFreezer.java

package net.modificationstation.sltest.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.sltest.tileentity.TileEntityFreezer;

// Referenced classes of package net.minecraft.src:
//            Container, Slot, SlotFurnace, InventoryPlayer, 
//            TileEntityFreezer, ICrafting, ItemStack, EntityPlayer

public class ContainerFreezer extends ScreenHandler
{

    public ContainerFreezer(PlayerInventory inventoryplayer, TileEntityFreezer tileentityfreezer)
    {
        cookTime = 0;
        burnTime = 0;
        itemBurnTime = 0;
        freezer = tileentityfreezer;
        addSlot(new Slot(tileentityfreezer, 0, 56, 17));
        addSlot(new Slot(tileentityfreezer, 1, 56, 53));
        addSlot(new FurnaceOutputSlot(inventoryplayer.player, tileentityfreezer, 2, 116, 35));
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

    @Override
    protected void insertItem(ItemStack itemstack1, int k, int l, boolean flag1)
    {
    }

    @Override
    public void sendContentUpdates()
    {
        super.sendContentUpdates();
        for (Object o : listeners) {
            ScreenHandlerListener icrafting = (ScreenHandlerListener) o;
            if (cookTime != freezer.frozenTimeForItem) {
                icrafting.onPropertyUpdate(this, 0, freezer.frozenTimeForItem);
            }
            if (burnTime != freezer.frozenProgress) {
                icrafting.onPropertyUpdate(this, 1, freezer.frozenProgress);
            }
            if (itemBurnTime != freezer.frozenPowerRemaining) {
                icrafting.onPropertyUpdate(this, 2, freezer.frozenPowerRemaining);
            }
        }

        cookTime = freezer.frozenTimeForItem;
        burnTime = freezer.frozenProgress;
        itemBurnTime = freezer.frozenPowerRemaining;
    }

    @Override
    public void setProperty(int i, int j)
    {
        if(i == 0)
        {
            freezer.frozenTimeForItem = j;
        }
        if(i == 1)
        {
            freezer.frozenProgress = j;
        }
        if(i == 2)
        {
            freezer.frozenPowerRemaining = j;
        }
    }

    @Override
    public boolean canUse(PlayerEntity entityplayer)
    {
        return freezer.canPlayerUse(entityplayer);
    }

    @Override
    public ItemStack quickMove(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)slots.get(i);
        if(slot != null && slot.hasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i == 2)
            {
                insertItem(itemstack1, 3, 39, true);
            } else
            if(i >= 3 && i < 30)
            {
                insertItem(itemstack1, 30, 39, false);
            } else
            if(i >= 30 && i < 39)
            {
                insertItem(itemstack1, 3, 30, false);
            } else
            {
                insertItem(itemstack1, 3, 39, false);
            }
            if(itemstack1.count == 0)
            {
                slot.setStack(null);
            } else
            {
                slot.markDirty();
            }
            if(itemstack1.count != itemstack.count)
            {
                slot.onTakeItem(itemstack1);
            } else
            {
                return null;
            }
        }
        return itemstack;
    }

    @Override
    public void addListener(ScreenHandlerListener crafting) {
        super.addListener(crafting);
        crafting.onPropertyUpdate(this, 0, freezer.frozenTimeForItem);
        crafting.onPropertyUpdate(this, 1, freezer.frozenProgress);
        crafting.onPropertyUpdate(this, 2, freezer.frozenPowerRemaining);
    }

    private TileEntityFreezer freezer;
    private int cookTime;
    private int burnTime;
    private int itemBurnTime;
}
