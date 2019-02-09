// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            EntityWeatherEffect, World, MathHelper, Block, 
//            BlockFire, AxisAlignedBB, Entity, NBTTagCompound

public class EntityLightningBolt extends EntityWeatherEffect
{

    public EntityLightningBolt(World world, double d, double d1, double d2)
    {
        super(world);
        field_27019_a = 0L;
        setLocationAndAngles(d, d1, d2, 0.0F, 0.0F);
        field_27018_b = 2;
        field_27019_a = rand.nextLong();
        field_27020_c = rand.nextInt(3) + 1;
        if(world.difficultySetting >= 2 && world.doChunksNearChunkExist(MathHelper.floor_double(d), MathHelper.floor_double(d1), MathHelper.floor_double(d2), 10))
        {
            int i = MathHelper.floor_double(d);
            int k = MathHelper.floor_double(d1);
            int i1 = MathHelper.floor_double(d2);
            if(world.getBlockId(i, k, i1) == 0 && Block.fire.canPlaceBlockAt(world, i, k, i1))
            {
                world.setBlockWithNotify(i, k, i1, Block.fire.blockID);
            }
            for(int j = 0; j < 4; j++)
            {
                int l = (MathHelper.floor_double(d) + rand.nextInt(3)) - 1;
                int j1 = (MathHelper.floor_double(d1) + rand.nextInt(3)) - 1;
                int k1 = (MathHelper.floor_double(d2) + rand.nextInt(3)) - 1;
                if(world.getBlockId(l, j1, k1) == 0 && Block.fire.canPlaceBlockAt(world, l, j1, k1))
                {
                    world.setBlockWithNotify(l, j1, k1, Block.fire.blockID);
                }
            }

        }
    }

    public void onUpdate()
    {
        super.onUpdate();
        if(field_27018_b == 2)
        {
            worldObj.playSoundEffect(posX, posY, posZ, "ambient.weather.thunder", 10000F, 0.8F + rand.nextFloat() * 0.2F);
            worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 2.0F, 0.5F + rand.nextFloat() * 0.2F);
        }
        field_27018_b--;
        if(field_27018_b < 0)
        {
            if(field_27020_c == 0)
            {
                setEntityDead();
            } else
            if(field_27018_b < -rand.nextInt(10))
            {
                field_27020_c--;
                field_27018_b = 1;
                field_27019_a = rand.nextLong();
                if(worldObj.doChunksNearChunkExist(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ), 10))
                {
                    int i = MathHelper.floor_double(posX);
                    int j = MathHelper.floor_double(posY);
                    int k = MathHelper.floor_double(posZ);
                    if(worldObj.getBlockId(i, j, k) == 0 && Block.fire.canPlaceBlockAt(worldObj, i, j, k))
                    {
                        worldObj.setBlockWithNotify(i, j, k, Block.fire.blockID);
                    }
                }
            }
        }
        if(field_27018_b >= 0)
        {
            double d = 3D;
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBoxFromPool(posX - d, posY - d, posZ - d, posX + d, posY + 6D + d, posZ + d));
            for(int l = 0; l < list.size(); l++)
            {
                Entity entity = (Entity)list.get(l);
                entity.onStruckByLightning(this);
            }

            worldObj.field_27080_i = 2;
        }
    }

    protected void entityInit()
    {
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
    }

    private int field_27018_b;
    public long field_27019_a;
    private int field_27020_c;
}
