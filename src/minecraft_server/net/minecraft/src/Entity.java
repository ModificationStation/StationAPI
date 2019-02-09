// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            AxisAlignedBB, DataWatcher, MathHelper, World, 
//            Block, StepSound, Material, BlockFluid, 
//            NBTTagCompound, NBTTagList, NBTTagDouble, NBTTagFloat, 
//            EntityList, ItemStack, EntityItem, EntityPlayer, 
//            Vec3D, EntityLightningBolt, EntityLiving

public abstract class Entity
{

    public Entity(World world)
    {
        entityId = nextEntityID++;
        renderDistanceWeight = 1.0D;
        preventEntitySpawning = false;
        onGround = false;
        isCollided = false;
        beenAttacked = false;
        field_9077_F = true;
        isDead = false;
        yOffset = 0.0F;
        width = 0.6F;
        height = 1.8F;
        prevDistanceWalkedModified = 0.0F;
        distanceWalkedModified = 0.0F;
        fallDistance = 0.0F;
        nextStepDistance = 1;
        ySize = 0.0F;
        stepHeight = 0.0F;
        noClip = false;
        entityCollisionReduction = 0.0F;
        rand = new Random();
        ticksExisted = 0;
        fireResistance = 1;
        fire = 0;
        maxAir = 300;
        inWater = false;
        field_9083_ac = 0;
        air = 300;
        firstUpdate = true;
        isImmuneToFire = false;
        dataWatcher = new DataWatcher();
        field_31001_bF = 0.0F;
        addedToChunk = false;
        worldObj = world;
        setPosition(0.0D, 0.0D, 0.0D);
        dataWatcher.addObject(0, Byte.valueOf((byte)0));
        entityInit();
    }

    protected abstract void entityInit();

    public DataWatcher getDataWatcher()
    {
        return dataWatcher;
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof Entity)
        {
            return ((Entity)obj).entityId == entityId;
        } else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return entityId;
    }

    public void setEntityDead()
    {
        isDead = true;
    }

    protected void setSize(float f, float f1)
    {
        width = f;
        height = f1;
    }

    protected void setRotation(float f, float f1)
    {
        rotationYaw = f % 360F;
        rotationPitch = f1 % 360F;
    }

    public void setPosition(double d, double d1, double d2)
    {
        posX = d;
        posY = d1;
        posZ = d2;
        float f = width / 2.0F;
        float f1 = height;
        boundingBox.setBounds(d - (double)f, (d1 - (double)yOffset) + (double)ySize, d2 - (double)f, d + (double)f, (d1 - (double)yOffset) + (double)ySize + (double)f1, d2 + (double)f);
    }

    public void onUpdate()
    {
        onEntityUpdate();
    }

    public void onEntityUpdate()
    {
        if(ridingEntity != null && ridingEntity.isDead)
        {
            ridingEntity = null;
        }
        ticksExisted++;
        prevDistanceWalkedModified = distanceWalkedModified;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        prevRotationPitch = rotationPitch;
        prevRotationYaw = rotationYaw;
        if(handleWaterMovement())
        {
            if(!inWater && !firstUpdate)
            {
                float f = MathHelper.sqrt_double(motionX * motionX * 0.20000000298023224D + motionY * motionY + motionZ * motionZ * 0.20000000298023224D) * 0.2F;
                if(f > 1.0F)
                {
                    f = 1.0F;
                }
                worldObj.playSoundAtEntity(this, "random.splash", f, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
                float f1 = MathHelper.floor_double(boundingBox.minY);
                for(int i = 0; (float)i < 1.0F + width * 20F; i++)
                {
                    float f2 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                    float f4 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                    worldObj.spawnParticle("bubble", posX + (double)f2, f1 + 1.0F, posZ + (double)f4, motionX, motionY - (double)(rand.nextFloat() * 0.2F), motionZ);
                }

                for(int j = 0; (float)j < 1.0F + width * 20F; j++)
                {
                    float f3 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                    float f5 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                    worldObj.spawnParticle("splash", posX + (double)f3, f1 + 1.0F, posZ + (double)f5, motionX, motionY, motionZ);
                }

            }
            fallDistance = 0.0F;
            inWater = true;
            fire = 0;
        } else
        {
            inWater = false;
        }
        if(worldObj.singleplayerWorld)
        {
            fire = 0;
        } else
        if(fire > 0)
        {
            if(isImmuneToFire)
            {
                fire -= 4;
                if(fire < 0)
                {
                    fire = 0;
                }
            } else
            {
                if(fire % 20 == 0)
                {
                    attackEntityFrom(null, 1);
                }
                fire--;
            }
        }
        if(handleLavaMovement())
        {
            setOnFireFromLava();
        }
        if(posY < -64D)
        {
            kill();
        }
        if(!worldObj.singleplayerWorld)
        {
            setFlag(0, fire > 0);
            setFlag(2, ridingEntity != null);
        }
        firstUpdate = false;
    }

    protected void setOnFireFromLava()
    {
        if(!isImmuneToFire)
        {
            attackEntityFrom(null, 4);
            fire = 600;
        }
    }

    protected void kill()
    {
        setEntityDead();
    }

    public boolean isOffsetPositionInLiquid(double d, double d1, double d2)
    {
        AxisAlignedBB axisalignedbb = boundingBox.getOffsetBoundingBox(d, d1, d2);
        List list = worldObj.getCollidingBoundingBoxes(this, axisalignedbb);
        if(list.size() > 0)
        {
            return false;
        }
        return !worldObj.getIsAnyLiquid(axisalignedbb);
    }

    public void moveEntity(double d, double d1, double d2)
    {
        if(noClip)
        {
            boundingBox.offset(d, d1, d2);
            posX = (boundingBox.minX + boundingBox.maxX) / 2D;
            posY = (boundingBox.minY + (double)yOffset) - (double)ySize;
            posZ = (boundingBox.minZ + boundingBox.maxZ) / 2D;
            return;
        }
        ySize *= 0.4F;
        double d3 = posX;
        double d4 = posZ;
        if(field_27012_bb)
        {
            field_27012_bb = false;
            d *= 0.25D;
            d1 *= 0.05000000074505806D;
            d2 *= 0.25D;
            motionX = 0.0D;
            motionY = 0.0D;
            motionZ = 0.0D;
        }
        double d5 = d;
        double d6 = d1;
        double d7 = d2;
        AxisAlignedBB axisalignedbb = boundingBox.copy();
        boolean flag = onGround && isSneaking();
        if(flag)
        {
            double d8 = 0.050000000000000003D;
            for(; d != 0.0D && worldObj.getCollidingBoundingBoxes(this, boundingBox.getOffsetBoundingBox(d, -1D, 0.0D)).size() == 0; d5 = d)
            {
                if(d < d8 && d >= -d8)
                {
                    d = 0.0D;
                    continue;
                }
                if(d > 0.0D)
                {
                    d -= d8;
                } else
                {
                    d += d8;
                }
            }

            for(; d2 != 0.0D && worldObj.getCollidingBoundingBoxes(this, boundingBox.getOffsetBoundingBox(0.0D, -1D, d2)).size() == 0; d7 = d2)
            {
                if(d2 < d8 && d2 >= -d8)
                {
                    d2 = 0.0D;
                    continue;
                }
                if(d2 > 0.0D)
                {
                    d2 -= d8;
                } else
                {
                    d2 += d8;
                }
            }

        }
        List list = worldObj.getCollidingBoundingBoxes(this, boundingBox.addCoord(d, d1, d2));
        for(int i = 0; i < list.size(); i++)
        {
            d1 = ((AxisAlignedBB)list.get(i)).calculateYOffset(boundingBox, d1);
        }

        boundingBox.offset(0.0D, d1, 0.0D);
        if(!field_9077_F && d6 != d1)
        {
            d = d1 = d2 = 0.0D;
        }
        boolean flag1 = onGround || d6 != d1 && d6 < 0.0D;
        for(int j = 0; j < list.size(); j++)
        {
            d = ((AxisAlignedBB)list.get(j)).calculateXOffset(boundingBox, d);
        }

        boundingBox.offset(d, 0.0D, 0.0D);
        if(!field_9077_F && d5 != d)
        {
            d = d1 = d2 = 0.0D;
        }
        for(int k = 0; k < list.size(); k++)
        {
            d2 = ((AxisAlignedBB)list.get(k)).calculateZOffset(boundingBox, d2);
        }

        boundingBox.offset(0.0D, 0.0D, d2);
        if(!field_9077_F && d7 != d2)
        {
            d = d1 = d2 = 0.0D;
        }
        if(stepHeight > 0.0F && flag1 && (flag || ySize < 0.05F) && (d5 != d || d7 != d2))
        {
            double d9 = d;
            double d11 = d1;
            double d13 = d2;
            d = d5;
            d1 = stepHeight;
            d2 = d7;
            AxisAlignedBB axisalignedbb1 = boundingBox.copy();
            boundingBox.setBB(axisalignedbb);
            List list1 = worldObj.getCollidingBoundingBoxes(this, boundingBox.addCoord(d, d1, d2));
            for(int j2 = 0; j2 < list1.size(); j2++)
            {
                d1 = ((AxisAlignedBB)list1.get(j2)).calculateYOffset(boundingBox, d1);
            }

            boundingBox.offset(0.0D, d1, 0.0D);
            if(!field_9077_F && d6 != d1)
            {
                d = d1 = d2 = 0.0D;
            }
            for(int k2 = 0; k2 < list1.size(); k2++)
            {
                d = ((AxisAlignedBB)list1.get(k2)).calculateXOffset(boundingBox, d);
            }

            boundingBox.offset(d, 0.0D, 0.0D);
            if(!field_9077_F && d5 != d)
            {
                d = d1 = d2 = 0.0D;
            }
            for(int l2 = 0; l2 < list1.size(); l2++)
            {
                d2 = ((AxisAlignedBB)list1.get(l2)).calculateZOffset(boundingBox, d2);
            }

            boundingBox.offset(0.0D, 0.0D, d2);
            if(!field_9077_F && d7 != d2)
            {
                d = d1 = d2 = 0.0D;
            }
            if(!field_9077_F && d6 != d1)
            {
                d = d1 = d2 = 0.0D;
            } else
            {
                d1 = -stepHeight;
                for(int i3 = 0; i3 < list1.size(); i3++)
                {
                    d1 = ((AxisAlignedBB)list1.get(i3)).calculateYOffset(boundingBox, d1);
                }

                boundingBox.offset(0.0D, d1, 0.0D);
            }
            if(d9 * d9 + d13 * d13 >= d * d + d2 * d2)
            {
                d = d9;
                d1 = d11;
                d2 = d13;
                boundingBox.setBB(axisalignedbb1);
            } else
            {
                double d14 = boundingBox.minY - (double)(int)boundingBox.minY;
                if(d14 > 0.0D)
                {
                    ySize += d14 + 0.01D;
                }
            }
        }
        posX = (boundingBox.minX + boundingBox.maxX) / 2D;
        posY = (boundingBox.minY + (double)yOffset) - (double)ySize;
        posZ = (boundingBox.minZ + boundingBox.maxZ) / 2D;
        isCollidedHorizontally = d5 != d || d7 != d2;
        isCollidedVertically = d6 != d1;
        onGround = d6 != d1 && d6 < 0.0D;
        isCollided = isCollidedHorizontally || isCollidedVertically;
        updateFallState(d1, onGround);
        if(d5 != d)
        {
            motionX = 0.0D;
        }
        if(d6 != d1)
        {
            motionY = 0.0D;
        }
        if(d7 != d2)
        {
            motionZ = 0.0D;
        }
        double d10 = posX - d3;
        double d12 = posZ - d4;
        if(func_25017_l() && !flag && ridingEntity == null)
        {
            distanceWalkedModified += (double)MathHelper.sqrt_double(d10 * d10 + d12 * d12) * 0.59999999999999998D;
            int l = MathHelper.floor_double(posX);
            int j1 = MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset);
            int l1 = MathHelper.floor_double(posZ);
            int j3 = worldObj.getBlockId(l, j1, l1);
            if(worldObj.getBlockId(l, j1 - 1, l1) == Block.fence.blockID)
            {
                j3 = worldObj.getBlockId(l, j1 - 1, l1);
            }
            if(distanceWalkedModified > (float)nextStepDistance && j3 > 0)
            {
                nextStepDistance++;
                StepSound stepsound = Block.blocksList[j3].stepSound;
                if(worldObj.getBlockId(l, j1 + 1, l1) == Block.snow.blockID)
                {
                    stepsound = Block.snow.stepSound;
                    worldObj.playSoundAtEntity(this, stepsound.func_737_c(), stepsound.getVolume() * 0.15F, stepsound.getPitch());
                } else
                if(!Block.blocksList[j3].blockMaterial.getIsLiquid())
                {
                    worldObj.playSoundAtEntity(this, stepsound.func_737_c(), stepsound.getVolume() * 0.15F, stepsound.getPitch());
                }
                Block.blocksList[j3].onEntityWalking(worldObj, l, j1, l1, this);
            }
        }
        int i1 = MathHelper.floor_double(boundingBox.minX + 0.001D);
        int k1 = MathHelper.floor_double(boundingBox.minY + 0.001D);
        int i2 = MathHelper.floor_double(boundingBox.minZ + 0.001D);
        int k3 = MathHelper.floor_double(boundingBox.maxX - 0.001D);
        int l3 = MathHelper.floor_double(boundingBox.maxY - 0.001D);
        int i4 = MathHelper.floor_double(boundingBox.maxZ - 0.001D);
        if(worldObj.checkChunksExist(i1, k1, i2, k3, l3, i4))
        {
            for(int j4 = i1; j4 <= k3; j4++)
            {
                for(int k4 = k1; k4 <= l3; k4++)
                {
                    for(int l4 = i2; l4 <= i4; l4++)
                    {
                        int i5 = worldObj.getBlockId(j4, k4, l4);
                        if(i5 > 0)
                        {
                            Block.blocksList[i5].onEntityCollidedWithBlock(worldObj, j4, k4, l4, this);
                        }
                    }

                }

            }

        }
        boolean flag2 = func_27008_Y();
        if(worldObj.isBoundingBoxBurning(boundingBox.getInsetBoundingBox(0.001D, 0.001D, 0.001D)))
        {
            dealFireDamage(1);
            if(!flag2)
            {
                fire++;
                if(fire == 0)
                {
                    fire = 300;
                }
            }
        } else
        if(fire <= 0)
        {
            fire = -fireResistance;
        }
        if(flag2 && fire > 0)
        {
            worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
            fire = -fireResistance;
        }
    }

    protected boolean func_25017_l()
    {
        return true;
    }

    protected void updateFallState(double d, boolean flag)
    {
        if(flag)
        {
            if(fallDistance > 0.0F)
            {
                fall(fallDistance);
                fallDistance = 0.0F;
            }
        } else
        if(d < 0.0D)
        {
            fallDistance -= d;
        }
    }

    public AxisAlignedBB getBoundingBox()
    {
        return null;
    }

    protected void dealFireDamage(int i)
    {
        if(!isImmuneToFire)
        {
            attackEntityFrom(null, i);
        }
    }

    protected void fall(float f)
    {
        if(riddenByEntity != null)
        {
            riddenByEntity.fall(f);
        }
    }

    public boolean func_27008_Y()
    {
        return inWater || worldObj.canLightningStrikeAt(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
    }

    public boolean isInWater()
    {
        return inWater;
    }

    public boolean handleWaterMovement()
    {
        return worldObj.handleMaterialAcceleration(boundingBox.expand(0.0D, -0.40000000596046448D, 0.0D).getInsetBoundingBox(0.001D, 0.001D, 0.001D), Material.water, this);
    }

    public boolean isInsideOfMaterial(Material material)
    {
        double d = posY + (double)getEyeHeight();
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_float(MathHelper.floor_double(d));
        int k = MathHelper.floor_double(posZ);
        int l = worldObj.getBlockId(i, j, k);
        if(l != 0 && Block.blocksList[l].blockMaterial == material)
        {
            float f = BlockFluid.setFluidHeight(worldObj.getBlockMetadata(i, j, k)) - 0.1111111F;
            float f1 = (float)(j + 1) - f;
            return d < (double)f1;
        } else
        {
            return false;
        }
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }

    public boolean handleLavaMovement()
    {
        return worldObj.isMaterialInBB(boundingBox.expand(-0.10000000149011612D, -0.40000000596046448D, -0.10000000149011612D), Material.lava);
    }

    public void moveFlying(float f, float f1, float f2)
    {
        float f3 = MathHelper.sqrt_float(f * f + f1 * f1);
        if(f3 < 0.01F)
        {
            return;
        }
        if(f3 < 1.0F)
        {
            f3 = 1.0F;
        }
        f3 = f2 / f3;
        f *= f3;
        f1 *= f3;
        float f4 = MathHelper.sin((rotationYaw * 3.141593F) / 180F);
        float f5 = MathHelper.cos((rotationYaw * 3.141593F) / 180F);
        motionX += f * f5 - f1 * f4;
        motionZ += f1 * f5 + f * f4;
    }

    public float getEntityBrightness(float f)
    {
        int i = MathHelper.floor_double(posX);
        double d = (boundingBox.maxY - boundingBox.minY) * 0.66000000000000003D;
        int j = MathHelper.floor_double((posY - (double)yOffset) + d);
        int k = MathHelper.floor_double(posZ);
        if(worldObj.checkChunksExist(MathHelper.floor_double(boundingBox.minX), MathHelper.floor_double(boundingBox.minY), MathHelper.floor_double(boundingBox.minZ), MathHelper.floor_double(boundingBox.maxX), MathHelper.floor_double(boundingBox.maxY), MathHelper.floor_double(boundingBox.maxZ)))
        {
            float f1 = worldObj.getLightBrightness(i, j, k);
            if(f1 < field_31001_bF)
            {
                f1 = field_31001_bF;
            }
            return f1;
        } else
        {
            return field_31001_bF;
        }
    }

    public void setWorldHandler(World world)
    {
        worldObj = world;
    }

    public void setPositionAndRotation(double d, double d1, double d2, float f, 
            float f1)
    {
        prevPosX = posX = d;
        prevPosY = posY = d1;
        prevPosZ = posZ = d2;
        prevRotationYaw = rotationYaw = f;
        prevRotationPitch = rotationPitch = f1;
        ySize = 0.0F;
        double d3 = prevRotationYaw - f;
        if(d3 < -180D)
        {
            prevRotationYaw += 360F;
        }
        if(d3 >= 180D)
        {
            prevRotationYaw -= 360F;
        }
        setPosition(posX, posY, posZ);
        setRotation(f, f1);
    }

    public void setLocationAndAngles(double d, double d1, double d2, float f, 
            float f1)
    {
        lastTickPosX = prevPosX = posX = d;
        lastTickPosY = prevPosY = posY = d1 + (double)yOffset;
        lastTickPosZ = prevPosZ = posZ = d2;
        rotationYaw = f;
        rotationPitch = f1;
        setPosition(posX, posY, posZ);
    }

    public float getDistanceToEntity(Entity entity)
    {
        float f = (float)(posX - entity.posX);
        float f1 = (float)(posY - entity.posY);
        float f2 = (float)(posZ - entity.posZ);
        return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
    }

    public double getDistanceSq(double d, double d1, double d2)
    {
        double d3 = posX - d;
        double d4 = posY - d1;
        double d5 = posZ - d2;
        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double getDistance(double d, double d1, double d2)
    {
        double d3 = posX - d;
        double d4 = posY - d1;
        double d5 = posZ - d2;
        return (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
    }

    public double getDistanceSqToEntity(Entity entity)
    {
        double d = posX - entity.posX;
        double d1 = posY - entity.posY;
        double d2 = posZ - entity.posZ;
        return d * d + d1 * d1 + d2 * d2;
    }

    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
    }

    public void applyEntityCollision(Entity entity)
    {
        if(entity.riddenByEntity == this || entity.ridingEntity == this)
        {
            return;
        }
        double d = entity.posX - posX;
        double d1 = entity.posZ - posZ;
        double d2 = MathHelper.abs_max(d, d1);
        if(d2 >= 0.0099999997764825821D)
        {
            d2 = MathHelper.sqrt_double(d2);
            d /= d2;
            d1 /= d2;
            double d3 = 1.0D / d2;
            if(d3 > 1.0D)
            {
                d3 = 1.0D;
            }
            d *= d3;
            d1 *= d3;
            d *= 0.05000000074505806D;
            d1 *= 0.05000000074505806D;
            d *= 1.0F - entityCollisionReduction;
            d1 *= 1.0F - entityCollisionReduction;
            addVelocity(-d, 0.0D, -d1);
            entity.addVelocity(d, 0.0D, d1);
        }
    }

    public void addVelocity(double d, double d1, double d2)
    {
        motionX += d;
        motionY += d1;
        motionZ += d2;
    }

    protected void setBeenAttacked()
    {
        beenAttacked = true;
    }

    public boolean attackEntityFrom(Entity entity, int i)
    {
        setBeenAttacked();
        return false;
    }

    public boolean canBeCollidedWith()
    {
        return false;
    }

    public boolean canBePushed()
    {
        return false;
    }

    public void addToPlayerScore(Entity entity, int i)
    {
    }

    public boolean addEntityID(NBTTagCompound nbttagcompound)
    {
        String s = getEntityString();
        if(isDead || s == null)
        {
            return false;
        } else
        {
            nbttagcompound.setString("id", s);
            writeToNBT(nbttagcompound);
            return true;
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setTag("Pos", newDoubleNBTList(new double[] {
            posX, posY + (double)ySize, posZ
        }));
        nbttagcompound.setTag("Motion", newDoubleNBTList(new double[] {
            motionX, motionY, motionZ
        }));
        nbttagcompound.setTag("Rotation", newFloatNBTList(new float[] {
            rotationYaw, rotationPitch
        }));
        nbttagcompound.setFloat("FallDistance", fallDistance);
        nbttagcompound.setShort("Fire", (short)fire);
        nbttagcompound.setShort("Air", (short)air);
        nbttagcompound.setBoolean("OnGround", onGround);
        writeEntityToNBT(nbttagcompound);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        NBTTagList nbttaglist = nbttagcompound.getTagList("Pos");
        NBTTagList nbttaglist1 = nbttagcompound.getTagList("Motion");
        NBTTagList nbttaglist2 = nbttagcompound.getTagList("Rotation");
        motionX = ((NBTTagDouble)nbttaglist1.tagAt(0)).doubleValue;
        motionY = ((NBTTagDouble)nbttaglist1.tagAt(1)).doubleValue;
        motionZ = ((NBTTagDouble)nbttaglist1.tagAt(2)).doubleValue;
        if(Math.abs(motionX) > 10D)
        {
            motionX = 0.0D;
        }
        if(Math.abs(motionY) > 10D)
        {
            motionY = 0.0D;
        }
        if(Math.abs(motionZ) > 10D)
        {
            motionZ = 0.0D;
        }
        prevPosX = lastTickPosX = posX = ((NBTTagDouble)nbttaglist.tagAt(0)).doubleValue;
        prevPosY = lastTickPosY = posY = ((NBTTagDouble)nbttaglist.tagAt(1)).doubleValue;
        prevPosZ = lastTickPosZ = posZ = ((NBTTagDouble)nbttaglist.tagAt(2)).doubleValue;
        prevRotationYaw = rotationYaw = ((NBTTagFloat)nbttaglist2.tagAt(0)).floatValue;
        prevRotationPitch = rotationPitch = ((NBTTagFloat)nbttaglist2.tagAt(1)).floatValue;
        fallDistance = nbttagcompound.getFloat("FallDistance");
        fire = nbttagcompound.getShort("Fire");
        air = nbttagcompound.getShort("Air");
        onGround = nbttagcompound.getBoolean("OnGround");
        setPosition(posX, posY, posZ);
        setRotation(rotationYaw, rotationPitch);
        readEntityFromNBT(nbttagcompound);
    }

    protected final String getEntityString()
    {
        return EntityList.getEntityString(this);
    }

    protected abstract void readEntityFromNBT(NBTTagCompound nbttagcompound);

    protected abstract void writeEntityToNBT(NBTTagCompound nbttagcompound);

    protected NBTTagList newDoubleNBTList(double ad[])
    {
        NBTTagList nbttaglist = new NBTTagList();
        double ad1[] = ad;
        int i = ad1.length;
        for(int j = 0; j < i; j++)
        {
            double d = ad1[j];
            nbttaglist.setTag(new NBTTagDouble(d));
        }

        return nbttaglist;
    }

    protected NBTTagList newFloatNBTList(float af[])
    {
        NBTTagList nbttaglist = new NBTTagList();
        float af1[] = af;
        int i = af1.length;
        for(int j = 0; j < i; j++)
        {
            float f = af1[j];
            nbttaglist.setTag(new NBTTagFloat(f));
        }

        return nbttaglist;
    }

    public EntityItem dropItem(int i, int j)
    {
        return dropItemWithOffset(i, j, 0.0F);
    }

    public EntityItem dropItemWithOffset(int i, int j, float f)
    {
        return entityDropItem(new ItemStack(i, j, 0), f);
    }

    public EntityItem entityDropItem(ItemStack itemstack, float f)
    {
        EntityItem entityitem = new EntityItem(worldObj, posX, posY + (double)f, posZ, itemstack);
        entityitem.delayBeforeCanPickup = 10;
        worldObj.entityJoinedWorld(entityitem);
        return entityitem;
    }

    public boolean isEntityAlive()
    {
        return !isDead;
    }

    public boolean isEntityInsideOpaqueBlock()
    {
        for(int i = 0; i < 8; i++)
        {
            float f = ((float)((i >> 0) % 2) - 0.5F) * width * 0.9F;
            float f1 = ((float)((i >> 1) % 2) - 0.5F) * 0.1F;
            float f2 = ((float)((i >> 2) % 2) - 0.5F) * width * 0.9F;
            int j = MathHelper.floor_double(posX + (double)f);
            int k = MathHelper.floor_double(posY + (double)getEyeHeight() + (double)f1);
            int l = MathHelper.floor_double(posZ + (double)f2);
            if(worldObj.isBlockNormalCube(j, k, l))
            {
                return true;
            }
        }

        return false;
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        return false;
    }

    public AxisAlignedBB func_89_d(Entity entity)
    {
        return null;
    }

    public void updateRidden()
    {
        if(ridingEntity.isDead)
        {
            ridingEntity = null;
            return;
        }
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        onUpdate();
        if(ridingEntity == null)
        {
            return;
        }
        ridingEntity.updateRiderPosition();
        entityRiderYawDelta += ridingEntity.rotationYaw - ridingEntity.prevRotationYaw;
        entityRiderPitchDelta += ridingEntity.rotationPitch - ridingEntity.prevRotationPitch;
        for(; entityRiderYawDelta >= 180D; entityRiderYawDelta -= 360D) { }
        for(; entityRiderYawDelta < -180D; entityRiderYawDelta += 360D) { }
        for(; entityRiderPitchDelta >= 180D; entityRiderPitchDelta -= 360D) { }
        for(; entityRiderPitchDelta < -180D; entityRiderPitchDelta += 360D) { }
        double d = entityRiderYawDelta * 0.5D;
        double d1 = entityRiderPitchDelta * 0.5D;
        float f = 10F;
        if(d > (double)f)
        {
            d = f;
        }
        if(d < (double)(-f))
        {
            d = -f;
        }
        if(d1 > (double)f)
        {
            d1 = f;
        }
        if(d1 < (double)(-f))
        {
            d1 = -f;
        }
        entityRiderYawDelta -= d;
        entityRiderPitchDelta -= d1;
        rotationYaw += d;
        rotationPitch += d1;
    }

    public void updateRiderPosition()
    {
        riddenByEntity.setPosition(posX, posY + getMountedYOffset() + riddenByEntity.getYOffset(), posZ);
    }

    public double getYOffset()
    {
        return (double)yOffset;
    }

    public double getMountedYOffset()
    {
        return (double)height * 0.75D;
    }

    public void mountEntity(Entity entity)
    {
        entityRiderPitchDelta = 0.0D;
        entityRiderYawDelta = 0.0D;
        if(entity == null)
        {
            if(ridingEntity != null)
            {
                setLocationAndAngles(ridingEntity.posX, ridingEntity.boundingBox.minY + (double)ridingEntity.height, ridingEntity.posZ, rotationYaw, rotationPitch);
                ridingEntity.riddenByEntity = null;
            }
            ridingEntity = null;
            return;
        }
        if(ridingEntity == entity)
        {
            ridingEntity.riddenByEntity = null;
            ridingEntity = null;
            setLocationAndAngles(entity.posX, entity.boundingBox.minY + (double)entity.height, entity.posZ, rotationYaw, rotationPitch);
            return;
        }
        if(ridingEntity != null)
        {
            ridingEntity.riddenByEntity = null;
        }
        if(entity.riddenByEntity != null)
        {
            entity.riddenByEntity.ridingEntity = null;
        }
        ridingEntity = entity;
        entity.riddenByEntity = this;
    }

    public Vec3D getLookVec()
    {
        return null;
    }

    public void setInPortal()
    {
    }

    public ItemStack[] getInventory()
    {
        return null;
    }

    public boolean isSneaking()
    {
        return getFlag(1);
    }

    public void setSneaking(boolean flag)
    {
        setFlag(1, flag);
    }

    protected boolean getFlag(int i)
    {
        return (dataWatcher.getWatchableObjectByte(0) & 1 << i) != 0;
    }

    protected void setFlag(int i, boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(0);
        if(flag)
        {
            dataWatcher.updateObject(0, Byte.valueOf((byte)(byte0 | 1 << i)));
        } else
        {
            dataWatcher.updateObject(0, Byte.valueOf((byte)(byte0 & ~(1 << i))));
        }
    }

    public void onStruckByLightning(EntityLightningBolt entitylightningbolt)
    {
        dealFireDamage(5);
        fire++;
        if(fire == 0)
        {
            fire = 300;
        }
    }

    public void func_27010_a(EntityLiving entityliving)
    {
    }

    protected boolean func_28005_g(double d, double d1, double d2)
    {
        int i = MathHelper.floor_double(d);
        int j = MathHelper.floor_double(d1);
        int k = MathHelper.floor_double(d2);
        double d3 = d - (double)i;
        double d4 = d1 - (double)j;
        double d5 = d2 - (double)k;
        if(worldObj.isBlockNormalCube(i, j, k))
        {
            boolean flag = !worldObj.isBlockNormalCube(i - 1, j, k);
            boolean flag1 = !worldObj.isBlockNormalCube(i + 1, j, k);
            boolean flag2 = !worldObj.isBlockNormalCube(i, j - 1, k);
            boolean flag3 = !worldObj.isBlockNormalCube(i, j + 1, k);
            boolean flag4 = !worldObj.isBlockNormalCube(i, j, k - 1);
            boolean flag5 = !worldObj.isBlockNormalCube(i, j, k + 1);
            byte byte0 = -1;
            double d6 = 9999D;
            if(flag && d3 < d6)
            {
                d6 = d3;
                byte0 = 0;
            }
            if(flag1 && 1.0D - d3 < d6)
            {
                d6 = 1.0D - d3;
                byte0 = 1;
            }
            if(flag2 && d4 < d6)
            {
                d6 = d4;
                byte0 = 2;
            }
            if(flag3 && 1.0D - d4 < d6)
            {
                d6 = 1.0D - d4;
                byte0 = 3;
            }
            if(flag4 && d5 < d6)
            {
                d6 = d5;
                byte0 = 4;
            }
            if(flag5 && 1.0D - d5 < d6)
            {
                double d7 = 1.0D - d5;
                byte0 = 5;
            }
            float f = rand.nextFloat() * 0.2F + 0.1F;
            if(byte0 == 0)
            {
                motionX = -f;
            }
            if(byte0 == 1)
            {
                motionX = f;
            }
            if(byte0 == 2)
            {
                motionY = -f;
            }
            if(byte0 == 3)
            {
                motionY = f;
            }
            if(byte0 == 4)
            {
                motionZ = -f;
            }
            if(byte0 == 5)
            {
                motionZ = f;
            }
        }
        return false;
    }

    private static int nextEntityID = 0;
    public int entityId;
    public double renderDistanceWeight;
    public boolean preventEntitySpawning;
    public Entity riddenByEntity;
    public Entity ridingEntity;
    public World worldObj;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;
    public double posX;
    public double posY;
    public double posZ;
    public double motionX;
    public double motionY;
    public double motionZ;
    public float rotationYaw;
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    public final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    public boolean onGround;
    public boolean isCollidedHorizontally;
    public boolean isCollidedVertically;
    public boolean isCollided;
    public boolean beenAttacked;
    public boolean field_27012_bb;
    public boolean field_9077_F;
    public boolean isDead;
    public float yOffset;
    public float width;
    public float height;
    public float prevDistanceWalkedModified;
    public float distanceWalkedModified;
    protected float fallDistance;
    private int nextStepDistance;
    public double lastTickPosX;
    public double lastTickPosY;
    public double lastTickPosZ;
    public float ySize;
    public float stepHeight;
    public boolean noClip;
    public float entityCollisionReduction;
    protected Random rand;
    public int ticksExisted;
    public int fireResistance;
    public int fire;
    protected int maxAir;
    protected boolean inWater;
    public int field_9083_ac;
    public int air;
    private boolean firstUpdate;
    protected boolean isImmuneToFire;
    protected DataWatcher dataWatcher;
    public float field_31001_bF;
    private double entityRiderPitchDelta;
    private double entityRiderYawDelta;
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public boolean field_28008_bI;

}
