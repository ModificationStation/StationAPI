// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Item, ItemStack, World, Block, 
//            BlockSapling, BlockCrops, BlockGrass, BlockTallGrass, 
//            BlockFlower, EntitySheep, BlockCloth, EntityPlayer, 
//            EntityLiving

public class ItemDye extends Item
{

    public ItemDye(int i)
    {
        super(i);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        if(itemstack.getItemDamage() == 15)
        {
            int i1 = world.getBlockId(i, j, k);
            if(i1 == Block.sapling.blockID)
            {
                if(!world.singleplayerWorld)
                {
                    ((BlockSapling)Block.sapling).growTree(world, i, j, k, world.rand);
                    itemstack.stackSize--;
                }
                return true;
            }
            if(i1 == Block.crops.blockID)
            {
                if(!world.singleplayerWorld)
                {
                    ((BlockCrops)Block.crops).fertilize(world, i, j, k);
                    itemstack.stackSize--;
                }
                return true;
            }
            if(i1 == Block.grass.blockID)
            {
                if(!world.singleplayerWorld)
                {
                    itemstack.stackSize--;
label0:
                    for(int j1 = 0; j1 < 128; j1++)
                    {
                        int k1 = i;
                        int l1 = j + 1;
                        int i2 = k;
                        for(int j2 = 0; j2 < j1 / 16; j2++)
                        {
                            k1 += itemRand.nextInt(3) - 1;
                            l1 += ((itemRand.nextInt(3) - 1) * itemRand.nextInt(3)) / 2;
                            i2 += itemRand.nextInt(3) - 1;
                            if(world.getBlockId(k1, l1 - 1, i2) != Block.grass.blockID || world.isBlockNormalCube(k1, l1, i2))
                            {
                                continue label0;
                            }
                        }

                        if(world.getBlockId(k1, l1, i2) != 0)
                        {
                            continue;
                        }
                        if(itemRand.nextInt(10) != 0)
                        {
                            world.setBlockAndMetadataWithNotify(k1, l1, i2, Block.tallGrass.blockID, 1);
                            continue;
                        }
                        if(itemRand.nextInt(3) != 0)
                        {
                            world.setBlockWithNotify(k1, l1, i2, Block.plantYellow.blockID);
                        } else
                        {
                            world.setBlockWithNotify(k1, l1, i2, Block.plantRed.blockID);
                        }
                    }

                }
                return true;
            }
        }
        return false;
    }

    public void saddleEntity(ItemStack itemstack, EntityLiving entityliving)
    {
        if(entityliving instanceof EntitySheep)
        {
            EntitySheep entitysheep = (EntitySheep)entityliving;
            int i = BlockCloth.func_21033_c(itemstack.getItemDamage());
            if(!entitysheep.func_21069_f_() && entitysheep.getFleeceColor() != i)
            {
                entitysheep.setFleeceColor(i);
                itemstack.stackSize--;
            }
        }
    }

    public static final String dyeColors[] = {
        "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", 
        "lime", "yellow", "lightBlue", "magenta", "orange", "white"
    };
    public static final int field_31023_bk[] = {
        0x1e1b1b, 0xb3312c, 0x3b511a, 0x51301a, 0x253192, 0x7b2fbe, 0x287697, 0x287697, 0x434343, 0xd88198, 
        0x41cd34, 0xdecf2a, 0x6689d3, 0xc354cd, 0xeb8844, 0xf0f0f0
    };

}
