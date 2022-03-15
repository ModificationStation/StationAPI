// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   TileEntityFreezer.java

package net.modificationstation.sltest.tileentity;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import net.modificationstation.sltest.util.Frozen;

import java.util.ArrayList;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            TileEntity, IInventory, ItemInstance, NBTTagCompound, 
//            NBTTagList, Frozen, ItemBase, AetherItems, 
//            AetherBlocks, BlockBase, World, EntityPlayer

public class TileEntityFreezer extends TileEntityBase
    implements InventoryBase
{

    public TileEntityFreezer()
    {
        frozenItemStacks = new ItemInstance[3];
        frozenProgress = 0;
        frozenPowerRemaining = 0;
        frozenTimeForItem = 0;
    }

    @Override
    public int getInventorySize()
    {
        return frozenItemStacks.length;
    }

    @Override
    public ItemInstance getInventoryItem(int i)
    {
        return frozenItemStacks[i];
    }

    @Override
    public ItemInstance takeInventoryItem(int i, int j)
    {
        if(frozenItemStacks[i] != null)
        {
            if(frozenItemStacks[i].count <= j)
            {
                ItemInstance itemstack = frozenItemStacks[i];
                frozenItemStacks[i] = null;
                return itemstack;
            }
            ItemInstance itemstack1 = frozenItemStacks[i].split(j);
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
    public void setInventoryItem(int i, ItemInstance itemstack)
    {
        frozenItemStacks[i] = itemstack;
        if(itemstack != null && itemstack.count > getMaxItemCount())
        {
            itemstack.count = getMaxItemCount();
        }
    }

    @Override
    public String getContainerName()
    {
        return "Freezer";
    }

    @Override
    public void readIdentifyingData(CompoundTag nbttagcompound)
    {
        super.readIdentifyingData(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getListTag("Items");
        frozenItemStacks = new ItemInstance[getInventorySize()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.get(i);
            byte byte0 = nbttagcompound1.getByte("Slot");
            if(byte0 >= 0 && byte0 < frozenItemStacks.length)
            {
                frozenItemStacks[byte0] = new ItemInstance(nbttagcompound1);
            }
        }

        frozenProgress = nbttagcompound.getShort("BurnTime");
        frozenTimeForItem = nbttagcompound.getShort("CookTime");
    }

    @Override
    public void writeIdentifyingData(CompoundTag nbttagcompound)
    {
        super.writeIdentifyingData(nbttagcompound);
        nbttagcompound.put("BurnTime", (short)frozenProgress);
        nbttagcompound.put("CookTime", (short)frozenTimeForItem);
        ListTag nbttaglist = new ListTag();
        for(int i = 0; i < frozenItemStacks.length; i++)
        {
            if(frozenItemStacks[i] != null)
            {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.put("Slot", (byte)i);
                frozenItemStacks[i].toTag(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
    }

    @Override
    public int getMaxItemCount()
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
    public void tick()
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
                setInventoryItem(2, new ItemInstance(currentFrozen.frozenTo.getType(), 1, currentFrozen.frozenTo.getDamage()));
            } else
            {
                setInventoryItem(2, new ItemInstance(currentFrozen.frozenTo.getType(), getInventoryItem(2).count + 1, currentFrozen.frozenTo.getDamage()));
            }
            if(getInventoryItem(0).itemId == ItemBase.waterBucket.id || getInventoryItem(0).itemId == ItemBase.lavaBucket.id)
            {
                setInventoryItem(0, new ItemInstance(ItemBase.bucket));
            } else
            {
                takeInventoryItem(0, 1);
            }
            frozenProgress = 0;
            currentFrozen = null;
            frozenTimeForItem = 0;
        }
        if(frozenPowerRemaining <= 0 && currentFrozen != null && getInventoryItem(1) != null && getInventoryItem(1).itemId == BlockBase.SNOW_BLOCK.id)
        {
            frozenPowerRemaining += 500;
            takeInventoryItem(1, 1);
        }
        if(currentFrozen == null)
        {
            ItemInstance itemstack = getInventoryItem(0);
            for (Frozen frozen : frozen) {
                if (itemstack == null || frozen == null || itemstack.itemId != frozen.frozenFrom.itemId || itemstack.getDamage() != frozen.frozenFrom.getDamage()) {
                    continue;
                }
                if (frozenItemStacks[2] == null) {
                    currentFrozen = frozen;
                    frozenTimeForItem = currentFrozen.frozenPowerNeeded;
                    continue;
                }
                if (frozenItemStacks[2].itemId == frozen.frozenTo.itemId && frozen.frozenTo.getType().getMaxStackSize() > frozenItemStacks[2].count) {
                    currentFrozen = frozen;
                    frozenTimeForItem = currentFrozen.frozenPowerNeeded;
                }
            }

        }
    }

    public boolean canPlayerUse(PlayerBase entityplayer)
    {
        if(level.getTileEntity(x, y, z) != this)
        {
            return false;
        } else
        {
            return entityplayer.squaredDistanceTo((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
        }
    }

    public static void addFrozen(ItemInstance from, ItemInstance to, int i)
    {
        frozen.add(new Frozen(from, to, i));
    }

    private static final List<Frozen> frozen = new ArrayList<>();
    private ItemInstance[] frozenItemStacks;
    public int frozenProgress;
    public int frozenPowerRemaining;
    public int frozenTimeForItem;
    private Frozen currentFrozen;

    static 
    {
        addFrozen(new ItemInstance(ItemBase.waterBucket, 1), new ItemInstance(BlockBase.ICE, 5), 500);
        addFrozen(new ItemInstance(ItemBase.lavaBucket, 1), new ItemInstance(BlockBase.OBSIDIAN, 2), 500);
    }
}
