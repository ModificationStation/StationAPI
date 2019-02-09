// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;

// Referenced classes of package net.minecraft.src:
//            EntityAnimal, DataWatcher, NBTTagCompound, World, 
//            EntityPlayer, EntitySheep, AxisAlignedBB, Entity, 
//            InventoryPlayer, ItemStack, Item, ItemFood, 
//            MathHelper, EntityArrow, EntityLiving

public class EntityWolf extends EntityAnimal
{

    public EntityWolf(World world)
    {
        super(world);
        field_25039_a = false;
        texture = "/mob/wolf.png";
        setSize(0.8F, 0.8F);
        moveSpeed = 1.1F;
        health = 8;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, Byte.valueOf((byte)0));
        dataWatcher.addObject(17, "");
        dataWatcher.addObject(18, new Integer(health));
    }

    protected boolean func_25017_l()
    {
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Angry", getIsAngry());
        nbttagcompound.setBoolean("Sitting", getIsSitting());
        if(getOwner() == null)
        {
            nbttagcompound.setString("Owner", "");
        } else
        {
            nbttagcompound.setString("Owner", getOwner());
        }
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        setIsAngry(nbttagcompound.getBoolean("Angry"));
        setIsSitting(nbttagcompound.getBoolean("Sitting"));
        String s = nbttagcompound.getString("Owner");
        if(s.length() > 0)
        {
            setOwner(s);
            setIsTamed(true);
        }
    }

    protected boolean func_25020_s()
    {
        return !func_25030_y();
    }

    protected String getLivingSound()
    {
        if(getIsAngry())
        {
            return "mob.wolf.growl";
        }
        if(rand.nextInt(3) == 0)
        {
            if(func_25030_y() && dataWatcher.getWatchableObjectInteger(18) < 10)
            {
                return "mob.wolf.whine";
            } else
            {
                return "mob.wolf.panting";
            }
        } else
        {
            return "mob.wolf.bark";
        }
    }

    protected String getHurtSound()
    {
        return "mob.wolf.hurt";
    }

    protected String getDeathSound()
    {
        return "mob.wolf.death";
    }

    protected float getSoundVolume()
    {
        return 0.4F;
    }

    protected int getDropItemId()
    {
        return -1;
    }

    protected void updatePlayerActionState()
    {
        super.updatePlayerActionState();
        if(!hasAttacked && !getGotPath() && func_25030_y() && ridingEntity == null)
        {
            EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwner());
            if(entityplayer != null)
            {
                float f = entityplayer.getDistanceToEntity(this);
                if(f > 5F)
                {
                    setPathEntity(entityplayer, f);
                }
            } else
            if(!isInWater())
            {
                setIsSitting(true);
            }
        } else
        if(playerToAttack == null && !getGotPath() && !func_25030_y() && worldObj.rand.nextInt(100) == 0)
        {
            List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntitySheep.class, AxisAlignedBB.getBoundingBoxFromPool(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
            if(!list.isEmpty())
            {
                setEntityToAttack((Entity)list.get(worldObj.rand.nextInt(list.size())));
            }
        }
        if(isInWater())
        {
            setIsSitting(false);
        }
        if(!worldObj.singleplayerWorld)
        {
            dataWatcher.updateObject(18, Integer.valueOf(health));
        }
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        field_25039_a = false;
        if(func_25021_O() && !getGotPath() && !getIsAngry())
        {
            Entity entity = getCurrentTarget();
            if(entity instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entity;
                ItemStack itemstack = entityplayer.inventory.getCurrentItem();
                if(itemstack != null)
                {
                    if(!func_25030_y() && itemstack.itemID == Item.bone.shiftedIndex)
                    {
                        field_25039_a = true;
                    } else
                    if(func_25030_y() && (Item.itemsList[itemstack.itemID] instanceof ItemFood))
                    {
                        field_25039_a = ((ItemFood)Item.itemsList[itemstack.itemID]).func_25010_k();
                    }
                }
            }
        }
        if(!isMultiplayerEntity && isWet && !field_25042_g && !getGotPath() && onGround)
        {
            field_25042_g = true;
            field_25041_h = 0.0F;
            field_25040_i = 0.0F;
            worldObj.sendTrackedEntityStatusUpdatePacket(this, (byte)8);
        }
    }

    public void onUpdate()
    {
        super.onUpdate();
        field_25044_c = field_25038_b;
        if(field_25039_a)
        {
            field_25038_b = field_25038_b + (1.0F - field_25038_b) * 0.4F;
        } else
        {
            field_25038_b = field_25038_b + (0.0F - field_25038_b) * 0.4F;
        }
        if(field_25039_a)
        {
            numTicksToChaseTarget = 10;
        }
        if(func_27008_Y())
        {
            isWet = true;
            field_25042_g = false;
            field_25041_h = 0.0F;
            field_25040_i = 0.0F;
        } else
        if((isWet || field_25042_g) && field_25042_g)
        {
            if(field_25041_h == 0.0F)
            {
                worldObj.playSoundAtEntity(this, "mob.wolf.shake", getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }
            field_25040_i = field_25041_h;
            field_25041_h += 0.05F;
            if(field_25040_i >= 2.0F)
            {
                isWet = false;
                field_25042_g = false;
                field_25040_i = 0.0F;
                field_25041_h = 0.0F;
            }
            if(field_25041_h > 0.4F)
            {
                float f = (float)boundingBox.minY;
                int i = (int)(MathHelper.sin((field_25041_h - 0.4F) * 3.141593F) * 7F);
                for(int j = 0; j < i; j++)
                {
                    float f1 = (rand.nextFloat() * 2.0F - 1.0F) * width * 0.5F;
                    float f2 = (rand.nextFloat() * 2.0F - 1.0F) * width * 0.5F;
                    worldObj.spawnParticle("splash", posX + (double)f1, f + 0.8F, posZ + (double)f2, motionX, motionY, motionZ);
                }

            }
        }
    }

    public float getEyeHeight()
    {
        return height * 0.8F;
    }

    protected int func_25018_n_()
    {
        if(getIsSitting())
        {
            return 20;
        } else
        {
            return super.func_25018_n_();
        }
    }

    private void setPathEntity(Entity entity, float f)
    {
        PathEntity pathentity = worldObj.getPathToEntity(this, entity, 16F);
        if(pathentity == null && f > 12F)
        {
            int i = MathHelper.floor_double(entity.posX) - 2;
            int j = MathHelper.floor_double(entity.posZ) - 2;
            int k = MathHelper.floor_double(entity.boundingBox.minY);
            for(int l = 0; l <= 4; l++)
            {
                for(int i1 = 0; i1 <= 4; i1++)
                {
                    if((l < 1 || i1 < 1 || l > 3 || i1 > 3) && worldObj.isBlockNormalCube(i + l, k - 1, j + i1) && !worldObj.isBlockNormalCube(i + l, k, j + i1) && !worldObj.isBlockNormalCube(i + l, k + 1, j + i1))
                    {
                        setLocationAndAngles((float)(i + l) + 0.5F, k, (float)(j + i1) + 0.5F, rotationYaw, rotationPitch);
                        return;
                    }
                }

            }

        } else
        {
            setPathToEntity(pathentity);
        }
    }

    protected boolean func_25026_u()
    {
        return getIsSitting() || field_25042_g;
    }

    public boolean attackEntityFrom(Entity entity, int i)
    {
        setIsSitting(false);
        if(entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
        {
            i = (i + 1) / 2;
        }
        if(super.attackEntityFrom(entity, i))
        {
            if(!func_25030_y() && !getIsAngry())
            {
                if(entity instanceof EntityPlayer)
                {
                    setIsAngry(true);
                    playerToAttack = entity;
                }
                if((entity instanceof EntityArrow) && ((EntityArrow)entity).owner != null)
                {
                    entity = ((EntityArrow)entity).owner;
                }
                if(entity instanceof EntityLiving)
                {
                    List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityWolf.class, AxisAlignedBB.getBoundingBoxFromPool(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
                    Iterator iterator = list.iterator();
                    do
                    {
                        if(!iterator.hasNext())
                        {
                            break;
                        }
                        Entity entity1 = (Entity)iterator.next();
                        EntityWolf entitywolf = (EntityWolf)entity1;
                        if(!entitywolf.func_25030_y() && entitywolf.playerToAttack == null)
                        {
                            entitywolf.playerToAttack = entity;
                            if(entity instanceof EntityPlayer)
                            {
                                entitywolf.setIsAngry(true);
                            }
                        }
                    } while(true);
                }
            } else
            if(entity != this && entity != null)
            {
                if(func_25030_y() && (entity instanceof EntityPlayer) && ((EntityPlayer)entity).username.equalsIgnoreCase(getOwner()))
                {
                    return true;
                }
                playerToAttack = entity;
            }
            return true;
        } else
        {
            return false;
        }
    }

    protected Entity findPlayerToAttack()
    {
        if(getIsAngry())
        {
            return worldObj.getClosestPlayerToEntity(this, 16D);
        } else
        {
            return null;
        }
    }

    protected void attackEntity(Entity entity, float f)
    {
        if(f > 2.0F && f < 6F && rand.nextInt(10) == 0)
        {
            if(onGround)
            {
                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
                motionX = (d / (double)f1) * 0.5D * 0.80000001192092896D + motionX * 0.20000000298023224D;
                motionZ = (d1 / (double)f1) * 0.5D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
                motionY = 0.40000000596046448D;
            }
        } else
        if((double)f < 1.5D && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            byte byte0 = 2;
            if(func_25030_y())
            {
                byte0 = 4;
            }
            entity.attackEntityFrom(this, byte0);
        }
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        if(!func_25030_y())
        {
            if(itemstack != null && itemstack.itemID == Item.bone.shiftedIndex && !getIsAngry())
            {
                itemstack.stackSize--;
                if(itemstack.stackSize <= 0)
                {
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                }
                if(!worldObj.singleplayerWorld)
                {
                    if(rand.nextInt(3) == 0)
                    {
                        setIsTamed(true);
                        setPathToEntity(null);
                        setIsSitting(true);
                        health = 20;
                        setOwner(entityplayer.username);
                        isNowTamed(true);
                        worldObj.sendTrackedEntityStatusUpdatePacket(this, (byte)7);
                    } else
                    {
                        isNowTamed(false);
                        worldObj.sendTrackedEntityStatusUpdatePacket(this, (byte)6);
                    }
                }
                return true;
            }
        } else
        {
            if(itemstack != null && (Item.itemsList[itemstack.itemID] instanceof ItemFood))
            {
                ItemFood itemfood = (ItemFood)Item.itemsList[itemstack.itemID];
                if(itemfood.func_25010_k() && dataWatcher.getWatchableObjectInteger(18) < 20)
                {
                    itemstack.stackSize--;
                    if(itemstack.stackSize <= 0)
                    {
                        entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                    }
                    heal(((ItemFood)Item.porkRaw).getHealAmount());
                    return true;
                }
            }
            if(entityplayer.username.equalsIgnoreCase(getOwner()))
            {
                if(!worldObj.singleplayerWorld)
                {
                    setIsSitting(!getIsSitting());
                    isJumping = false;
                    setPathToEntity(null);
                }
                return true;
            }
        }
        return false;
    }

    void isNowTamed(boolean flag)
    {
        String s = "heart";
        if(!flag)
        {
            s = "smoke";
        }
        for(int i = 0; i < 7; i++)
        {
            double d = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            worldObj.spawnParticle(s, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
        }

    }

    public int getMaxSpawnedInChunk()
    {
        return 8;
    }

    public String getOwner()
    {
        return dataWatcher.getWatchableObjectString(17);
    }

    public void setOwner(String s)
    {
        dataWatcher.updateObject(17, s);
    }

    public boolean getIsSitting()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setIsSitting(boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);
        if(flag)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 1)));
        } else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -2)));
        }
    }

    public boolean getIsAngry()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    public void setIsAngry(boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);
        if(flag)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 2)));
        } else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -3)));
        }
    }

    public boolean func_25030_y()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 4) != 0;
    }

    public void setIsTamed(boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);
        if(flag)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 4)));
        } else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -5)));
        }
    }

    private boolean field_25039_a;
    private float field_25038_b;
    private float field_25044_c;
    private boolean isWet;
    private boolean field_25042_g;
    private float field_25041_h;
    private float field_25040_i;
}
