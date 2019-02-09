// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;

// Referenced classes of package net.minecraft.src:
//            Container, InventoryCrafting, InventoryCraftResult, SlotCrafting, 
//            InventoryPlayer, Slot, CraftingManager, IInventory, 
//            World, EntityPlayer, Block, ItemStack

public class ContainerWorkbench extends Container
{

    public ContainerWorkbench(InventoryPlayer inventoryplayer, World world, int i, int j, int k)
    {
        craftMatrix = new InventoryCrafting(this, 3, 3);
        craftResult = new InventoryCraftResult();
        field_20150_c = world;
        field_20149_h = i;
        field_20148_i = j;
        field_20147_j = k;
        addSlot(new SlotCrafting(inventoryplayer.player, craftMatrix, craftResult, 0, 124, 35));
        for(int l = 0; l < 3; l++)
        {
            for(int k1 = 0; k1 < 3; k1++)
            {
                addSlot(new Slot(craftMatrix, k1 + l * 3, 30 + k1 * 18, 17 + l * 18));
            }

        }

        for(int i1 = 0; i1 < 3; i1++)
        {
            for(int l1 = 0; l1 < 9; l1++)
            {
                addSlot(new Slot(inventoryplayer, l1 + i1 * 9 + 9, 8 + l1 * 18, 84 + i1 * 18));
            }

        }

        for(int j1 = 0; j1 < 9; j1++)
        {
            addSlot(new Slot(inventoryplayer, j1, 8 + j1 * 18, 142));
        }

        onCraftMatrixChanged(craftMatrix);
    }

    public void onCraftMatrixChanged(IInventory iinventory)
    {
        craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix));
    }

    public void onCraftGuiClosed(EntityPlayer entityplayer)
    {
        super.onCraftGuiClosed(entityplayer);
        if(field_20150_c.singleplayerWorld)
        {
            return;
        }
        for(int i = 0; i < 9; i++)
        {
            ItemStack itemstack = craftMatrix.getStackInSlot(i);
            if(itemstack != null)
            {
                entityplayer.dropPlayerItem(itemstack);
            }
        }

    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(field_20150_c.getBlockId(field_20149_h, field_20148_i, field_20147_j) != Block.workbench.blockID)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)field_20149_h + 0.5D, (double)field_20148_i + 0.5D, (double)field_20147_j + 0.5D) <= 64D;
    }

    public ItemStack func_27086_a(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if(slot != null && slot.func_27006_b())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i == 0)
            {
                func_28126_a(itemstack1, 10, 46, true);
            } else
            if(i >= 10 && i < 37)
            {
                func_28126_a(itemstack1, 37, 46, false);
            } else
            if(i >= 37 && i < 46)
            {
                func_28126_a(itemstack1, 10, 37, false);
            } else
            {
                func_28126_a(itemstack1, 10, 46, false);
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

    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    private World field_20150_c;
    private int field_20149_h;
    private int field_20148_i;
    private int field_20147_j;
}
