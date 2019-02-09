// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Item, World, Block, ItemStack, 
//            EntityPlayer

public class ItemSeeds extends Item
{

    public ItemSeeds(int i, int j)
    {
        super(i);
        field_318_a = j;
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        if(l != 1)
        {
            return false;
        }
        int i1 = world.getBlockId(i, j, k);
        if(i1 == Block.tilledField.blockID && world.isAirBlock(i, j + 1, k))
        {
            world.setBlockWithNotify(i, j + 1, k, field_318_a);
            itemstack.stackSize--;
            return true;
        } else
        {
            return false;
        }
    }

    private int field_318_a;
}
