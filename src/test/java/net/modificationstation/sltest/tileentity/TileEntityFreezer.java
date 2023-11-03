// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   TileEntityFreezer.java

package net.modificationstation.sltest.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.sltest.util.Frozen;

import java.util.ArrayList;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            TileEntity, IInventory, ItemInstance, NBTTagCompound, 
//            NBTTagList, Frozen, ItemBase, AetherItems, 
//            AetherBlocks, BlockBase, World, EntityPlayer

public class TileEntityFreezer extends BlockEntity
    implements Inventory
{

    public TileEntityFreezer()
    {
        frozenItemStacks = new ItemStack[3];
        frozenProgress = 0;
        frozenPowerRemaining = 0;
        frozenTimeForItem = 0;
    }

    @Override
    public int size()
    {
        return frozenItemStacks.length;
    }

    @Override
    public ItemStack getStack(int i)
    {
        return frozenItemStacks[i];
    }

    @Override
    public ItemStack removeStack(int i, int j)
    {
        if(frozenItemStacks[i] != null)
        {
            if(frozenItemStacks[i].count <= j)
            {
                ItemStack itemstack = frozenItemStacks[i];
                frozenItemStacks[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = frozenItemStacks[i].split(j);
            if(frozenItemStacks[i].count == 0)
            {
                frozenItemStacks[i] = null;
            }
            return itemstack1;
        } else
        {
            return null;
        }
    }

    @Override
    public void setStack(int i, ItemStack itemstack)
    {
        frozenItemStacks[i] = itemstack;
        if(itemstack != null && itemstack.count > getMaxCountPerStack())
        {
            itemstack.count = getMaxCountPerStack();
        }
    }

    @Override
    public String getName()
    {
        return "Freezer";
    }

    @Override
    public void readNbt(NbtCompound nbttagcompound)
    {
        super.readNbt(nbttagcompound);
        NbtList nbttaglist = nbttagcompound.getList("Items");
        frozenItemStacks = new ItemStack[size()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            NbtCompound nbttagcompound1 = (NbtCompound)nbttaglist.get(i);
            byte byte0 = nbttagcompound1.getByte("Slot");
            if(byte0 >= 0 && byte0 < frozenItemStacks.length)
            {
                frozenItemStacks[byte0] = new ItemStack(nbttagcompound1);
            }
        }

        frozenProgress = nbttagcompound.getShort("BurnTime");
        frozenTimeForItem = nbttagcompound.getShort("CookTime");
    }

    @Override
    public void writeNbt(NbtCompound nbttagcompound)
    {
        super.writeNbt(nbttagcompound);
        nbttagcompound.putShort("BurnTime", (short)frozenProgress);
        nbttagcompound.putShort("CookTime", (short)frozenTimeForItem);
        NbtList nbttaglist = new NbtList();
        for(int i = 0; i < frozenItemStacks.length; i++)
        {
            if(frozenItemStacks[i] != null)
            {
                NbtCompound nbttagcompound1 = new NbtCompound();
                nbttagcompound1.putByte("Slot", (byte)i);
                frozenItemStacks[i].writeNbt(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
    }

    @Override
    public int getMaxCountPerStack()
    {
        return 64;
    }

    public int getCookProgressScaled(int i)
    {
        if(frozenTimeForItem == 0)
        {
            return 0;
        } else
        {
            return (frozenProgress * i) / frozenTimeForItem;
        }
    }

    public int getBurnTimeRemainingScaled(int i)
    {
        return (frozenPowerRemaining * i) / 500;
    }

    public boolean isBurning()
    {
        return frozenPowerRemaining > 0;
    }

    @Override
    public void method_1076()
    {
        if(frozenPowerRemaining > 0)
        {
            frozenPowerRemaining--;
            if(currentFrozen != null)
            {
                frozenProgress++;
            }
        }
        if(currentFrozen != null && (frozenItemStacks[0] == null || frozenItemStacks[0].itemId != currentFrozen.frozenFrom.itemId))
        {
            currentFrozen = null;
            frozenProgress = 0;
        }
        if(currentFrozen != null && frozenProgress >= currentFrozen.frozenPowerNeeded)
        {
            if(frozenItemStacks[2] == null)
            {
                setStack(2, new ItemStack(currentFrozen.frozenTo.getItem(), 1, currentFrozen.frozenTo.getDamage()));
            } else
            {
                setStack(2, new ItemStack(currentFrozen.frozenTo.getItem(), getStack(2).count + 1, currentFrozen.frozenTo.getDamage()));
            }
            if(getStack(0).itemId == Item.WATER_BUCKET.id || getStack(0).itemId == Item.LAVA_BUCKET.id)
            {
                setStack(0, new ItemStack(Item.BUCKET));
            } else
            {
                removeStack(0, 1);
            }
            frozenProgress = 0;
            currentFrozen = null;
            frozenTimeForItem = 0;
        }
        if(frozenPowerRemaining <= 0 && currentFrozen != null && getStack(1) != null && getStack(1).itemId == Block.SNOW_BLOCK.id)
        {
            frozenPowerRemaining += 500;
            removeStack(1, 1);
        }
        if(currentFrozen == null)
        {
            ItemStack itemstack = getStack(0);
            for (Frozen frozen : frozen) {
                if (itemstack == null || frozen == null || itemstack.itemId != frozen.frozenFrom.itemId || itemstack.getDamage() != frozen.frozenFrom.getDamage()) {
                    continue;
                }
                if (frozenItemStacks[2] == null) {
                    currentFrozen = frozen;
                    frozenTimeForItem = currentFrozen.frozenPowerNeeded;
                    continue;
                }
                if (frozenItemStacks[2].itemId == frozen.frozenTo.itemId && frozen.frozenTo.getItem().getMaxCount() > frozenItemStacks[2].count) {
                    currentFrozen = frozen;
                    frozenTimeForItem = currentFrozen.frozenPowerNeeded;
                }
            }

        }
    }

    public boolean canPlayerUse(PlayerEntity entityplayer)
    {
        if(world.method_1777(x, y, z) != this)
        {
            return false;
        } else
        {
            return entityplayer.method_1347((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
        }
    }

    public static void addFrozen(ItemStack from, ItemStack to, int i)
    {
        frozen.add(new Frozen(from, to, i));
    }

    private static final List<Frozen> frozen = new ArrayList<>();
    private ItemStack[] frozenItemStacks;
    public int frozenProgress;
    public int frozenPowerRemaining;
    public int frozenTimeForItem;
    private Frozen currentFrozen;

    static 
    {
        addFrozen(new ItemStack(Item.WATER_BUCKET, 1), new ItemStack(Block.ICE, 5), 500);
        addFrozen(new ItemStack(Item.LAVA_BUCKET, 1), new ItemStack(Block.OBSIDIAN, 2), 500);
    }
}
