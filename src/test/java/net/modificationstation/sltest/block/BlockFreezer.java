// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   BlockFreezer.java

package net.modificationstation.sltest.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.sltest.inventory.ContainerFreezer;
import net.modificationstation.sltest.tileentity.TileEntityFreezer;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.Random;

import static net.modificationstation.sltest.SLTest.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

// Referenced classes of package net.minecraft.src:
//            BlockContainer, Material, ModLoader, Level, 
//            Block, TileEntityFreezer, GuiFreezer, EntityPlayer, 
//            EntityLiving, MathHelper, ItemStack, EntityItem, 
//            TileEntity

public class BlockFreezer extends TemplateBlockWithEntity
{

    protected BlockFreezer(Identifier blockID)
    {
        super(blockID, Material.field_998);
        FrozenRand = new Random();
    }

    @Override
    public void onPlaced(World world, int i, int j, int k)
    {
        super.onPlaced(world, i, j, k);
        setDefaultDirection(world, i, j, k);
    }

    private void setDefaultDirection(World world, int i, int j, int k)
    {
        if(world.isRemote)
        {
            return;
        }
        int l = world.getBlockId(i, j, k - 1);
        int i1 = world.getBlockId(i, j, k + 1);
        int j1 = world.getBlockId(i - 1, j, k);
        int k1 = world.getBlockId(i + 1, j, k);
        byte byte0 = 3;
        if(Block.BLOCKS_OPAQUE[l] && !Block.BLOCKS_OPAQUE[i1])
        {
            byte0 = 3;
        }
        if(Block.BLOCKS_OPAQUE[i1] && !Block.BLOCKS_OPAQUE[l])
        {
            byte0 = 2;
        }
        if(Block.BLOCKS_OPAQUE[j1] && !Block.BLOCKS_OPAQUE[k1])
        {
            byte0 = 5;
        }
        if(Block.BLOCKS_OPAQUE[k1] && !Block.BLOCKS_OPAQUE[j1])
        {
            byte0 = 4;
        }
        world.method_215(i, j, k, byte0);
    }

    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        TileEntityFreezer tileentity = (TileEntityFreezer)world.method_1777(i, j, k);
        if(world.isRemote ? tileentity.method_1069() == 1 : tileentity.isBurning())
        {
            float f = (float)i + 0.5F;
            float f1 = (float)j + 1.0F + (random.nextFloat() * 6F) / 16F;
            float f2 = (float)k + 0.5F;
            world.addParticle("smoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
            world.addParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
            world.addParticle("snowshovel", f, f1, f2, 0.0D, 0.0D, 0.0D);
            world.addParticle("snowshovel", f, f1, f2, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public int getTexture(int i)
    {
        if(i == 1)
        {
            return textureId;
        }
        if(i == 0)
        {
            return textureId;
        } else
        {
            return sideTexture;
        }
    }

    @Override
    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityplayer)
    {
        BlockEntity tileentityFreezer = world.method_1777(i, j, k);
        if (tileentityFreezer instanceof TileEntityFreezer freezer)
            GuiHelper.openGUI(entityplayer, of(MODID, "freezer"), freezer, new ContainerFreezer(entityplayer.inventory, freezer));
        return true;
    }

    public static void updateFreezerBlockState(boolean flag, World world, int i, int j, int k)
    {
        int l = world.getBlockMeta(i, j, k);
        BlockEntity tileentity = world.method_1777(i, j, k);
        world.setBlock(i, j, k, l);
        world.method_157(i, j, k, tileentity);
    }

    @Override
    protected BlockEntity createBlockEntity()
    {
        return new TileEntityFreezer();
    }

    @Override
    public void onPlaced(World world, int i, int j, int k, LivingEntity entityliving)
    {
        int l = MathHelper.floor((double)((entityliving.yaw * 4F) / 360F) + 0.5D) & 3;
        if(l == 0)
        {
            world.method_215(i, j, k, 2);
        }
        if(l == 1)
        {
            world.method_215(i, j, k, 5);
        }
        if(l == 2)
        {
            world.method_215(i, j, k, 3);
        }
        if(l == 3)
        {
            world.method_215(i, j, k, 4);
        }
    }

    @Override
    public void onBreak(World world, int i, int j, int k)
    {
        TileEntityFreezer tileentityFreezer = (TileEntityFreezer)world.method_1777(i, j, k);
label0:
        for(int l = 0; l < tileentityFreezer.size(); l++)
        {
            ItemStack itemstack = tileentityFreezer.getStack(l);
            if(itemstack == null)
            {
                continue;
            }
            float f = FrozenRand.nextFloat() * 0.8F + 0.1F;
            float f1 = FrozenRand.nextFloat() * 0.8F + 0.1F;
            float f2 = FrozenRand.nextFloat() * 0.8F + 0.1F;
            do
            {
                if(itemstack.count <= 0)
                {
                    continue label0;
                }
                int i1 = FrozenRand.nextInt(21) + 10;
                if(i1 > itemstack.count)
                {
                    i1 = itemstack.count;
                }
                itemstack.count -= i1;
                ItemEntity entityitem = new ItemEntity(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemId, i1, itemstack.getDamage()));
                float f3 = 0.05F;
                entityitem.velocityX = (float)FrozenRand.nextGaussian() * f3;
                entityitem.velocityY = (float)FrozenRand.nextGaussian() * f3 + 0.2F;
                entityitem.velocityZ = (float)FrozenRand.nextGaussian() * f3;
                world.method_210(entityitem);
            } while(true);
        }

        super.onBreak(world, i, j, k);
    }

    private Random FrozenRand;
    public int sideTexture;
}
