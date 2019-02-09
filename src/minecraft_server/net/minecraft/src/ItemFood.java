// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Item, ItemStack, EntityPlayer, World

public class ItemFood extends Item
{

    public ItemFood(int i, int j, boolean flag)
    {
        super(i);
        healAmount = j;
        field_25011_bi = flag;
        maxStackSize = 1;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        itemstack.stackSize--;
        entityplayer.heal(healAmount);
        return itemstack;
    }

    public int getHealAmount()
    {
        return healAmount;
    }

    public boolean func_25010_k()
    {
        return field_25011_bi;
    }

    private int healAmount;
    private boolean field_25011_bi;
}
