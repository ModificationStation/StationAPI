// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   BlockFreezer.java

package net.modificationstation.sltest.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Item;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.sltest.inventory.ContainerFreezer;
import net.modificationstation.sltest.tileentity.TileEntityFreezer;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.*;

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
        super(blockID, Material.SNOW);
        FrozenRand = new Random();
    }

    @Override
    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        super.onBlockPlaced(world, i, j, k);
        setDefaultDirection(world, i, j, k);
    }

    private void setDefaultDirection(Level world, int i, int j, int k)
    {
        if(world.isClient)
        {
            return;
        }
        int l = world.getTileId(i, j, k - 1);
        int i1 = world.getTileId(i, j, k + 1);
        int j1 = world.getTileId(i - 1, j, k);
        int k1 = world.getTileId(i + 1, j, k);
        byte byte0 = 3;
        if(BlockBase.FULL_OPAQUE[l] && !BlockBase.FULL_OPAQUE[i1])
        {
            byte0 = 3;
        }
        if(BlockBase.FULL_OPAQUE[i1] && !BlockBase.FULL_OPAQUE[l])
        {
            byte0 = 2;
        }
        if(BlockBase.FULL_OPAQUE[j1] && !BlockBase.FULL_OPAQUE[k1])
        {
            byte0 = 5;
        }
        if(BlockBase.FULL_OPAQUE[k1] && !BlockBase.FULL_OPAQUE[j1])
        {
            byte0 = 4;
        }
        world.setTileMeta(i, j, k, byte0);
    }

    @Override
    public void randomDisplayTick(Level world, int i, int j, int k, Random random)
    {
        TileEntityFreezer tileentity = (TileEntityFreezer)world.getTileEntity(i, j, k);
        if(world.isClient ? tileentity.getCachedId() == 1 : tileentity.isBurning())
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
    public int getTextureForSide(int i)
    {
        if(i == 1)
        {
            return texture;
        }
        if(i == 0)
        {
            return texture;
        } else
        {
            return sideTexture;
        }
    }

    @Override
    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityplayer)
    {
        TileEntityBase tileentityFreezer = world.getTileEntity(i, j, k);
        if (tileentityFreezer instanceof TileEntityFreezer freezer)
            GuiHelper.openGUI(entityplayer, of(MODID, "freezer"), freezer, new ContainerFreezer(entityplayer.inventory, freezer));
        return true;
    }

    public static void updateFreezerBlockState(boolean flag, Level world, int i, int j, int k)
    {
        int l = world.getTileMeta(i, j, k);
        TileEntityBase tileentity = world.getTileEntity(i, j, k);
        world.setTile(i, j, k, l);
        world.setTileEntity(i, j, k, tileentity);
    }

    @Override
    protected TileEntityBase createTileEntity()
    {
        return new TileEntityFreezer();
    }

    @Override
    public void afterPlaced(Level world, int i, int j, int k, Living entityliving)
    {
        int l = MathHelper.floor((double)((entityliving.yaw * 4F) / 360F) + 0.5D) & 3;
        if(l == 0)
        {
            world.setTileMeta(i, j, k, 2);
        }
        if(l == 1)
        {
            world.setTileMeta(i, j, k, 5);
        }
        if(l == 2)
        {
            world.setTileMeta(i, j, k, 3);
        }
        if(l == 3)
        {
            world.setTileMeta(i, j, k, 4);
        }
    }

    @Override
    public void onBlockRemoved(Level world, int i, int j, int k)
    {
        TileEntityFreezer tileentityFreezer = (TileEntityFreezer)world.getTileEntity(i, j, k);
label0:
        for(int l = 0; l < tileentityFreezer.getInventorySize(); l++)
        {
            ItemInstance itemstack = tileentityFreezer.getInventoryItem(l);
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
                Item entityitem = new Item(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemInstance(itemstack.itemId, i1, itemstack.getDamage()));
                float f3 = 0.05F;
                entityitem.velocityX = (float)FrozenRand.nextGaussian() * f3;
                entityitem.velocityY = (float)FrozenRand.nextGaussian() * f3 + 0.2F;
                entityitem.velocityZ = (float)FrozenRand.nextGaussian() * f3;
                world.spawnEntity(entityitem);
            } while(true);
        }

        super.onBlockRemoved(world, i, j, k);
    }

    private Random FrozenRand;
    public int sideTexture;
}
