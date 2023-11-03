// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   ContainerFreezer.java

package net.modificationstation.sltest.inventory;

import net.minecraft.class_497;
import net.minecraft.class_633;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.sltest.tileentity.TileEntityFreezer;

// Referenced classes of package net.minecraft.src:
//            Container, Slot, SlotFurnace, InventoryPlayer, 
//            TileEntityFreezer, ICrafting, ItemStack, EntityPlayer

public class ContainerFreezer extends Container
{

    public ContainerFreezer(PlayerInventory inventoryplayer, TileEntityFreezer tileentityfreezer)
    {
        cookTime = 0;
        burnTime = 0;
        itemBurnTime = 0;
        freezer = tileentityfreezer;
        method_2079(new Slot(tileentityfreezer, 0, 56, 17));
        method_2079(new Slot(tileentityfreezer, 1, 56, 53));
        method_2079(new class_497(inventoryplayer.player, tileentityfreezer, 2, 116, 35));
        for(int i = 0; i < 3; i++)
        {
            for(int k = 0; k < 9; k++)
            {
                method_2079(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }

        }

        for(int j = 0; j < 9; j++)
        {
            method_2079(new Slot(inventoryplayer, j, 8 + j * 18, 142));
        }

    }

    @Override
    protected void method_2081(ItemStack itemstack1, int k, int l, boolean flag1)
    {
    }

    @Override
    public void method_2075()
    {
        super.method_2075();
        for (Object o : field_2736) {
            class_633 icrafting = (class_633) o;
            if (cookTime != freezer.frozenTimeForItem) {
                icrafting.method_2099(this, 0, freezer.frozenTimeForItem);
            }
            if (burnTime != freezer.frozenProgress) {
                icrafting.method_2099(this, 1, freezer.frozenProgress);
            }
            if (itemBurnTime != freezer.frozenPowerRemaining) {
                icrafting.method_2099(this, 2, freezer.frozenPowerRemaining);
            }
        }

        cookTime = freezer.frozenTimeForItem;
        burnTime = freezer.frozenProgress;
        itemBurnTime = freezer.frozenPowerRemaining;
    }

    @Override
    public void method_2077(int i, int j)
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
    public boolean method_2094(PlayerEntity entityplayer)
    {
        return freezer.canPlayerUse(entityplayer);
    }

    @Override
    public ItemStack method_2086(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)slots.get(i);
        if(slot != null && slot.hasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i == 2)
            {
                method_2081(itemstack1, 3, 39, true);
            } else
            if(i >= 3 && i < 30)
            {
                method_2081(itemstack1, 30, 39, false);
            } else
            if(i >= 30 && i < 39)
            {
                method_2081(itemstack1, 3, 30, false);
            } else
            {
                method_2081(itemstack1, 3, 39, false);
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
                slot.onCrafted(itemstack1);
            } else
            {
                return null;
            }
        }
        return itemstack;
    }

    @Override
    public void method_2076(class_633 crafting) {
        super.method_2076(crafting);
        crafting.method_2099(this, 0, freezer.frozenTimeForItem);
        crafting.method_2099(this, 1, freezer.frozenProgress);
        crafting.method_2099(this, 2, freezer.frozenPowerRemaining);
    }

    private TileEntityFreezer freezer;
    private int cookTime;
    private int burnTime;
    private int itemBurnTime;
}
