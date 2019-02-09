// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            TileEntity, World, EntityList, EntityLiving, 
//            AxisAlignedBB, NBTTagCompound

public class TileEntityMobSpawner extends TileEntity
{

    public TileEntityMobSpawner()
    {
        delay = -1;
        yaw2 = 0.0D;
        mobID = "Pig";
        delay = 20;
    }

    public String getMobID()
    {
        return mobID;
    }

    public void setMobID(String s)
    {
        mobID = s;
    }

    public boolean anyPlayerInRange()
    {
        return worldObj.getClosestPlayer((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, 16D) != null;
    }

    public void updateEntity()
    {
        yaw2 = yaw;
        if(!anyPlayerInRange())
        {
            return;
        }
        double d = (float)xCoord + worldObj.rand.nextFloat();
        double d2 = (float)yCoord + worldObj.rand.nextFloat();
        double d4 = (float)zCoord + worldObj.rand.nextFloat();
        worldObj.spawnParticle("smoke", d, d2, d4, 0.0D, 0.0D, 0.0D);
        worldObj.spawnParticle("flame", d, d2, d4, 0.0D, 0.0D, 0.0D);
        for(yaw += 1000F / ((float)delay + 200F); yaw > 360D;)
        {
            yaw -= 360D;
            yaw2 -= 360D;
        }

        if(!worldObj.multiplayerWorld)
        {
            if(delay == -1)
            {
                updateDelay();
            }
            if(delay > 0)
            {
                delay--;
                return;
            }
            byte byte0 = 4;
            for(int i = 0; i < byte0; i++)
            {
                EntityLiving entityliving = (EntityLiving)EntityList.createEntityInWorld(mobID, worldObj);
                if(entityliving == null)
                {
                    return;
                }
                int j = worldObj.getEntitiesWithinAABB(entityliving.getClass(), AxisAlignedBB.getBoundingBoxFromPool(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(8D, 4D, 8D)).size();
                if(j >= 6)
                {
                    updateDelay();
                    return;
                }
                if(entityliving == null)
                {
                    continue;
                }
                double d6 = (double)xCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * 4D;
                double d7 = (yCoord + worldObj.rand.nextInt(3)) - 1;
                double d8 = (double)zCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * 4D;
                entityliving.setLocationAndAngles(d6, d7, d8, worldObj.rand.nextFloat() * 360F, 0.0F);
                if(!entityliving.getCanSpawnHere())
                {
                    continue;
                }
                worldObj.entityJoinedWorld(entityliving);
                for(int k = 0; k < 20; k++)
                {
                    double d1 = (double)xCoord + 0.5D + ((double)worldObj.rand.nextFloat() - 0.5D) * 2D;
                    double d3 = (double)yCoord + 0.5D + ((double)worldObj.rand.nextFloat() - 0.5D) * 2D;
                    double d5 = (double)zCoord + 0.5D + ((double)worldObj.rand.nextFloat() - 0.5D) * 2D;
                    worldObj.spawnParticle("smoke", d1, d3, d5, 0.0D, 0.0D, 0.0D);
                    worldObj.spawnParticle("flame", d1, d3, d5, 0.0D, 0.0D, 0.0D);
                }

                entityliving.spawnExplosionParticle();
                updateDelay();
            }

        }
        super.updateEntity();
    }

    private void updateDelay()
    {
        delay = 200 + worldObj.rand.nextInt(600);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        mobID = nbttagcompound.getString("EntityId");
        delay = nbttagcompound.getShort("Delay");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setString("EntityId", mobID);
        nbttagcompound.setShort("Delay", (short)delay);
    }

    public int delay;
    private String mobID;
    public double yaw;
    public double yaw2;
}
