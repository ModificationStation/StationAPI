// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            IInventory, ItemStack, EntityPlayer, NBTTagCompound, 
//            NBTTagList, Block, Material, ItemArmor, 
//            Entity

public class InventoryPlayer
    implements IInventory
{

    public InventoryPlayer(EntityPlayer entityplayer)
    {
        mainInventory = new ItemStack[36];
        armorInventory = new ItemStack[4];
        currentItem = 0;
        inventoryChanged = false;
        player = entityplayer;
    }

    public ItemStack getCurrentItem()
    {
        if(currentItem < 9 && currentItem >= 0)
        {
            return mainInventory[currentItem];
        } else
        {
            return null;
        }
    }

    public static int func_25054_e()
    {
        return 9;
    }

    private int getInventorySlotContainItem(int i)
    {
        for(int j = 0; j < mainInventory.length; j++)
        {
            if(mainInventory[j] != null && mainInventory[j].itemID == i)
            {
                return j;
            }
        }

        return -1;
    }

    private int func_21082_c(ItemStack itemstack)
    {
        for(int i = 0; i < mainInventory.length; i++)
        {
            if(mainInventory[i] != null && mainInventory[i].itemID == itemstack.itemID && mainInventory[i].func_21132_c() && mainInventory[i].stackSize < mainInventory[i].getMaxStackSize() && mainInventory[i].stackSize < getInventoryStackLimit() && (!mainInventory[i].getHasSubtypes() || mainInventory[i].getItemDamage() == itemstack.getItemDamage()))
            {
                return i;
            }
        }

        return -1;
    }

    private int getFirstEmptyStack()
    {
        for(int i = 0; i < mainInventory.length; i++)
        {
            if(mainInventory[i] == null)
            {
                return i;
            }
        }

        return -1;
    }

    private int func_21083_d(ItemStack itemstack)
    {
        int i = itemstack.itemID;
        int j = itemstack.stackSize;
        int k = func_21082_c(itemstack);
        if(k < 0)
        {
            k = getFirstEmptyStack();
        }
        if(k < 0)
        {
            return j;
        }
        if(mainInventory[k] == null)
        {
            mainInventory[k] = new ItemStack(i, 0, itemstack.getItemDamage());
        }
        int l = j;
        if(l > mainInventory[k].getMaxStackSize() - mainInventory[k].stackSize)
        {
            l = mainInventory[k].getMaxStackSize() - mainInventory[k].stackSize;
        }
        if(l > getInventoryStackLimit() - mainInventory[k].stackSize)
        {
            l = getInventoryStackLimit() - mainInventory[k].stackSize;
        }
        if(l == 0)
        {
            return j;
        } else
        {
            j -= l;
            mainInventory[k].stackSize += l;
            mainInventory[k].animationsToGo = 5;
            return j;
        }
    }

    public void decrementAnimations()
    {
        for(int i = 0; i < mainInventory.length; i++)
        {
            if(mainInventory[i] != null)
            {
                mainInventory[i].func_28143_a(player.worldObj, player, i, currentItem == i);
            }
        }

    }

    public boolean consumeInventoryItem(int i)
    {
        int j = getInventorySlotContainItem(i);
        if(j < 0)
        {
            return false;
        }
        if(--mainInventory[j].stackSize <= 0)
        {
            mainInventory[j] = null;
        }
        return true;
    }

    public boolean addItemStackToInventory(ItemStack itemstack)
    {
        if(!itemstack.isItemDamaged())
        {
            int i;
            do
            {
                i = itemstack.stackSize;
                itemstack.stackSize = func_21083_d(itemstack);
            } while(itemstack.stackSize > 0 && itemstack.stackSize < i);
            return itemstack.stackSize < i;
        }
        int j = getFirstEmptyStack();
        if(j >= 0)
        {
            mainInventory[j] = ItemStack.func_20117_a(itemstack);
            mainInventory[j].animationsToGo = 5;
            itemstack.stackSize = 0;
            return true;
        } else
        {
            return false;
        }
    }

    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack aitemstack[] = mainInventory;
        if(i >= mainInventory.length)
        {
            aitemstack = armorInventory;
            i -= mainInventory.length;
        }
        if(aitemstack[i] != null)
        {
            if(aitemstack[i].stackSize <= j)
            {
                ItemStack itemstack = aitemstack[i];
                aitemstack[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = aitemstack[i].splitStack(j);
            if(aitemstack[i].stackSize == 0)
            {
                aitemstack[i] = null;
            }
            return itemstack1;
        } else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        ItemStack aitemstack[] = mainInventory;
        if(i >= aitemstack.length)
        {
            i -= aitemstack.length;
            aitemstack = armorInventory;
        }
        aitemstack[i] = itemstack;
    }

    public float getStrVsBlock(Block block)
    {
        float f = 1.0F;
        if(mainInventory[currentItem] != null)
        {
            f *= mainInventory[currentItem].getStrVsBlock(block);
        }
        return f;
    }

    public NBTTagList writeToNBT(NBTTagList nbttaglist)
    {
        for(int i = 0; i < mainInventory.length; i++)
        {
            if(mainInventory[i] != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                mainInventory[i].writeToNBT(nbttagcompound);
                nbttaglist.setTag(nbttagcompound);
            }
        }

        for(int j = 0; j < armorInventory.length; j++)
        {
            if(armorInventory[j] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)(j + 100));
                armorInventory[j].writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }

        return nbttaglist;
    }

    public void readFromNBT(NBTTagList nbttaglist)
    {
        mainInventory = new ItemStack[36];
        armorInventory = new ItemStack[4];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound.getByte("Slot") & 0xff;
            ItemStack itemstack = new ItemStack(nbttagcompound);
            if(itemstack.getItem() == null)
            {
                continue;
            }
            if(j >= 0 && j < mainInventory.length)
            {
                mainInventory[j] = itemstack;
            }
            if(j >= 100 && j < armorInventory.length + 100)
            {
                armorInventory[j - 100] = itemstack;
            }
        }

    }

    public int getSizeInventory()
    {
        return mainInventory.length + 4;
    }

    public ItemStack getStackInSlot(int i)
    {
        ItemStack aitemstack[] = mainInventory;
        if(i >= aitemstack.length)
        {
            i -= aitemstack.length;
            aitemstack = armorInventory;
        }
        return aitemstack[i];
    }

    public String getInvName()
    {
        return "Inventory";
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getDamageVsEntity(Entity entity)
    {
        ItemStack itemstack = getStackInSlot(currentItem);
        if(itemstack != null)
        {
            return itemstack.getDamageVsEntity(entity);
        } else
        {
            return 1;
        }
    }

    public boolean canHarvestBlock(Block block)
    {
        if(block.blockMaterial.func_31055_i())
        {
            return true;
        }
        ItemStack itemstack = getStackInSlot(currentItem);
        if(itemstack != null)
        {
            return itemstack.canHarvestBlock(block);
        } else
        {
            return false;
        }
    }

    public int getTotalArmorValue()
    {
        int i = 0;
        int j = 0;
        int k = 0;
        for(int l = 0; l < armorInventory.length; l++)
        {
            if(armorInventory[l] != null && (armorInventory[l].getItem() instanceof ItemArmor))
            {
                int i1 = armorInventory[l].getMaxDamage();
                int j1 = armorInventory[l].getItemDamageForDisplay();
                int k1 = i1 - j1;
                j += k1;
                k += i1;
                int l1 = ((ItemArmor)armorInventory[l].getItem()).damageReduceAmount;
                i += l1;
            }
        }

        if(k == 0)
        {
            return 0;
        } else
        {
            return ((i - 1) * j) / k + 1;
        }
    }

    public void damageArmor(int i)
    {
        for(int j = 0; j < armorInventory.length; j++)
        {
            if(armorInventory[j] == null || !(armorInventory[j].getItem() instanceof ItemArmor))
            {
                continue;
            }
            armorInventory[j].damageItem(i, player);
            if(armorInventory[j].stackSize == 0)
            {
                armorInventory[j].func_577_a(player);
                armorInventory[j] = null;
            }
        }

    }

    public void dropAllItems()
    {
        for(int i = 0; i < mainInventory.length; i++)
        {
            if(mainInventory[i] != null)
            {
                player.dropPlayerItemWithRandomChoice(mainInventory[i], true);
                mainInventory[i] = null;
            }
        }

        for(int j = 0; j < armorInventory.length; j++)
        {
            if(armorInventory[j] != null)
            {
                player.dropPlayerItemWithRandomChoice(armorInventory[j], true);
                armorInventory[j] = null;
            }
        }

    }

    public void onInventoryChanged()
    {
        inventoryChanged = true;
    }

    public void setItemStack(ItemStack itemstack)
    {
        itemStack = itemstack;
        player.onItemStackChanged(itemstack);
    }

    public ItemStack getItemStack()
    {
        return itemStack;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(player.isDead)
        {
            return false;
        }
        return entityplayer.getDistanceSqToEntity(player) <= 64D;
    }

    public boolean func_28010_c(ItemStack itemstack)
    {
        for(int i = 0; i < armorInventory.length; i++)
        {
            if(armorInventory[i] != null && armorInventory[i].func_28144_c(itemstack))
            {
                return true;
            }
        }

        for(int j = 0; j < mainInventory.length; j++)
        {
            if(mainInventory[j] != null && mainInventory[j].func_28144_c(itemstack))
            {
                return true;
            }
        }

        return false;
    }

    public ItemStack mainInventory[];
    public ItemStack armorInventory[];
    public int currentItem;
    public EntityPlayer player;
    private ItemStack itemStack;
    public boolean inventoryChanged;
}
