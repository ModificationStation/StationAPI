// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Item, Block, BlockLeaves, ItemStack, 
//            EntityLiving

public class ItemShears extends Item
{

    public ItemShears(int i)
    {
        super(i);
        setMaxStackSize(1);
        setMaxDamage(238);
    }

    public boolean func_25007_a(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving)
    {
        if(i == Block.leaves.blockID || i == Block.web.blockID)
        {
            itemstack.damageItem(1, entityliving);
        }
        return super.func_25007_a(itemstack, i, j, k, l, entityliving);
    }

    public boolean canHarvestBlock(Block block)
    {
        return block.blockID == Block.web.blockID;
    }

    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        if(block.blockID == Block.web.blockID || block.blockID == Block.leaves.blockID)
        {
            return 15F;
        }
        if(block.blockID == Block.cloth.blockID)
        {
            return 5F;
        } else
        {
            return super.getStrVsBlock(itemstack, block);
        }
    }
}
