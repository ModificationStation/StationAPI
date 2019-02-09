// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Entity, World, Block, Item, 
//            AxisAlignedBB, Material, MathHelper, EntityPlayer, 
//            NBTTagCompound

public class EntityBoat extends Entity
{

    public EntityBoat(World world)
    {
        super(world);
        damageTaken = 0;
        field_9177_b = 0;
        forwardDirection = 1;
        preventEntitySpawning = true;
        setSize(1.5F, 0.6F);
        yOffset = height / 2.0F;
    }

    protected boolean func_25017_l()
    {
        return false;
    }

    protected void entityInit()
    {
    }

    public AxisAlignedBB func_89_d(Entity entity)
    {
        return entity.boundingBox;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }

    public boolean canBePushed()
    {
        return true;
    }

    public EntityBoat(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1 + (double)yOffset, d2);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = d;
        prevPosY = d1;
        prevPosZ = d2;
    }

    public double getMountedYOffset()
    {
        return (double)height * 0.0D - 0.30000001192092896D;
    }

    public boolean attackEntityFrom(Entity entity, int i)
    {
        if(worldObj.singleplayerWorld || isDead)
        {
            return true;
        }
        forwardDirection = -forwardDirection;
        field_9177_b = 10;
        damageTaken += i * 10;
        setBeenAttacked();
        if(damageTaken > 40)
        {
            if(riddenByEntity != null)
            {
                riddenByEntity.mountEntity(this);
            }
            for(int j = 0; j < 3; j++)
            {
                dropItemWithOffset(Block.planks.blockID, 1, 0.0F);
            }

            for(int k = 0; k < 2; k++)
            {
                dropItemWithOffset(Item.stick.shiftedIndex, 1, 0.0F);
            }

            setEntityDead();
        }
        return true;
    }

    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    public void onUpdate()
    {
        super.onUpdate();
        if(field_9177_b > 0)
        {
            field_9177_b--;
        }
        if(damageTaken > 0)
        {
            damageTaken--;
        }
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        int i = 5;
        double d = 0.0D;
        for(int j = 0; j < i; j++)
        {
            double d5 = (boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) * (double)(j + 0)) / (double)i) - 0.125D;
            double d9 = (boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) * (double)(j + 1)) / (double)i) - 0.125D;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBoxFromPool(boundingBox.minX, d5, boundingBox.minZ, boundingBox.maxX, d9, boundingBox.maxZ);
            if(worldObj.isAABBInMaterial(axisalignedbb, Material.water))
            {
                d += 1.0D / (double)i;
            }
        }

        if(worldObj.singleplayerWorld)
        {
            if(field_9176_d > 0)
            {
                double d1 = posX + (field_9174_e - posX) / (double)field_9176_d;
                double d6 = posY + (field_9172_f - posY) / (double)field_9176_d;
                double d10 = posZ + (field_9175_aj - posZ) / (double)field_9176_d;
                double d14;
                for(d14 = field_9173_ak - (double)rotationYaw; d14 < -180D; d14 += 360D) { }
                for(; d14 >= 180D; d14 -= 360D) { }
                rotationYaw += d14 / (double)field_9176_d;
                rotationPitch += (field_9171_al - (double)rotationPitch) / (double)field_9176_d;
                field_9176_d--;
                setPosition(d1, d6, d10);
                setRotation(rotationYaw, rotationPitch);
            } else
            {
                double d2 = posX + motionX;
                double d7 = posY + motionY;
                double d11 = posZ + motionZ;
                setPosition(d2, d7, d11);
                if(onGround)
                {
                    motionX *= 0.5D;
                    motionY *= 0.5D;
                    motionZ *= 0.5D;
                }
                motionX *= 0.99000000953674316D;
                motionY *= 0.94999998807907104D;
                motionZ *= 0.99000000953674316D;
            }
            return;
        }
        if(d < 1.0D)
        {
            double d3 = d * 2D - 1.0D;
            motionY += 0.039999999105930328D * d3;
        } else
        {
            if(motionY < 0.0D)
            {
                motionY /= 2D;
            }
            motionY += 0.0070000002160668373D;
        }
        if(riddenByEntity != null)
        {
            motionX += riddenByEntity.motionX * 0.20000000000000001D;
            motionZ += riddenByEntity.motionZ * 0.20000000000000001D;
        }
        double d4 = 0.40000000000000002D;
        if(motionX < -d4)
        {
            motionX = -d4;
        }
        if(motionX > d4)
        {
            motionX = d4;
        }
        if(motionZ < -d4)
        {
            motionZ = -d4;
        }
        if(motionZ > d4)
        {
            motionZ = d4;
        }
        if(onGround)
        {
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }
        moveEntity(motionX, motionY, motionZ);
        double d8 = Math.sqrt(motionX * motionX + motionZ * motionZ);
        if(d8 > 0.14999999999999999D)
        {
            double d12 = Math.cos(((double)rotationYaw * 3.1415926535897931D) / 180D);
            double d15 = Math.sin(((double)rotationYaw * 3.1415926535897931D) / 180D);
            for(int i1 = 0; (double)i1 < 1.0D + d8 * 60D; i1++)
            {
                double d18 = rand.nextFloat() * 2.0F - 1.0F;
                double d20 = (double)(rand.nextInt(2) * 2 - 1) * 0.69999999999999996D;
                if(rand.nextBoolean())
                {
                    double d21 = (posX - d12 * d18 * 0.80000000000000004D) + d15 * d20;
                    double d23 = posZ - d15 * d18 * 0.80000000000000004D - d12 * d20;
                    worldObj.spawnParticle("splash", d21, posY - 0.125D, d23, motionX, motionY, motionZ);
                } else
                {
                    double d22 = posX + d12 + d15 * d18 * 0.69999999999999996D;
                    double d24 = (posZ + d15) - d12 * d18 * 0.69999999999999996D;
                    worldObj.spawnParticle("splash", d22, posY - 0.125D, d24, motionX, motionY, motionZ);
                }
            }

        }
        if(isCollidedHorizontally && d8 > 0.14999999999999999D)
        {
            if(!worldObj.singleplayerWorld)
            {
                setEntityDead();
                for(int k = 0; k < 3; k++)
                {
                    dropItemWithOffset(Block.planks.blockID, 1, 0.0F);
                }

                for(int l = 0; l < 2; l++)
                {
                    dropItemWithOffset(Item.stick.shiftedIndex, 1, 0.0F);
                }

            }
        } else
        {
            motionX *= 0.99000000953674316D;
            motionY *= 0.94999998807907104D;
            motionZ *= 0.99000000953674316D;
        }
        rotationPitch = 0.0F;
        double d13 = rotationYaw;
        double d16 = prevPosX - posX;
        double d17 = prevPosZ - posZ;
        if(d16 * d16 + d17 * d17 > 0.001D)
        {
            d13 = (float)((Math.atan2(d17, d16) * 180D) / 3.1415926535897931D);
        }
        double d19;
        for(d19 = d13 - (double)rotationYaw; d19 >= 180D; d19 -= 360D) { }
        for(; d19 < -180D; d19 += 360D) { }
        if(d19 > 20D)
        {
            d19 = 20D;
        }
        if(d19 < -20D)
        {
            d19 = -20D;
        }
        rotationYaw += d19;
        setRotation(rotationYaw, rotationPitch);
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        if(list != null && list.size() > 0)
        {
            for(int j1 = 0; j1 < list.size(); j1++)
            {
                Entity entity = (Entity)list.get(j1);
                if(entity != riddenByEntity && entity.canBePushed() && (entity instanceof EntityBoat))
                {
                    entity.applyEntityCollision(this);
                }
            }

        }
        for(int k1 = 0; k1 < 4; k1++)
        {
            int l1 = MathHelper.floor_double(posX + ((double)(k1 % 2) - 0.5D) * 0.80000000000000004D);
            int i2 = MathHelper.floor_double(posY);
            int j2 = MathHelper.floor_double(posZ + ((double)(k1 / 2) - 0.5D) * 0.80000000000000004D);
            if(worldObj.getBlockId(l1, i2, j2) == Block.snow.blockID)
            {
                worldObj.setBlockWithNotify(l1, i2, j2, 0);
            }
        }

        if(riddenByEntity != null && riddenByEntity.isDead)
        {
            riddenByEntity = null;
        }
    }

    public void updateRiderPosition()
    {
        if(riddenByEntity == null)
        {
            return;
        } else
        {
            double d = Math.cos(((double)rotationYaw * 3.1415926535897931D) / 180D) * 0.40000000000000002D;
            double d1 = Math.sin(((double)rotationYaw * 3.1415926535897931D) / 180D) * 0.40000000000000002D;
            riddenByEntity.setPosition(posX + d, posY + getMountedYOffset() + riddenByEntity.getYOffset(), posZ + d1);
            return;
        }
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        if(riddenByEntity != null && (riddenByEntity instanceof EntityPlayer) && riddenByEntity != entityplayer)
        {
            return true;
        }
        if(!worldObj.singleplayerWorld)
        {
            entityplayer.mountEntity(this);
        }
        return true;
    }

    public int damageTaken;
    public int field_9177_b;
    public int forwardDirection;
    private int field_9176_d;
    private double field_9174_e;
    private double field_9172_f;
    private double field_9175_aj;
    private double field_9173_ak;
    private double field_9171_al;
}
