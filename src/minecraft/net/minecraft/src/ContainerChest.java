// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;

// Referenced classes of package net.minecraft.src:
//            Container, IInventory, Slot, ItemStack, 
//            EntityPlayer

public class ContainerChest extends Container
{

    public ContainerChest(IInventory iinventory, IInventory iinventory1)
    {
        field_20125_a = iinventory1;
        field_27282_b = iinventory1.getSizeInventory() / 9;
        int i = (field_27282_b - 4) * 18;
        for(int j = 0; j < field_27282_b; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(iinventory1, i1 + j * 9, 8 + i1 * 18, 18 + j * 18));
            }

        }

        for(int k = 0; k < 3; k++)
        {
            for(int j1 = 0; j1 < 9; j1++)
            {
                addSlot(new Slot(iinventory, j1 + k * 9 + 9, 8 + j1 * 18, 103 + k * 18 + i));
            }

        }

        for(int l = 0; l < 9; l++)
        {
            addSlot(new Slot(iinventory, l, 8 + l * 18, 161 + i));
        }

    }

    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return field_20125_a.canInteractWith(entityplayer);
    }

    public ItemStack getStackInSlot(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)slots.get(i);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i < field_27282_b * 9)
            {
                func_28125_a(itemstack1, field_27282_b * 9, slots.size(), true);
            } else
            {
                func_28125_a(itemstack1, 0, field_27282_b * 9, false);
            }
            if(itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            } else
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    private IInventory field_20125_a;
    private int field_27282_b;
}
