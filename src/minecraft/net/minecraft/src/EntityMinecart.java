// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.PrintStream;
import java.util.List;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Entity, IInventory, ItemStack, World, 
//            Item, EntityItem, Block, MathHelper, 
//            BlockRail, Vec3D, AxisAlignedBB, NBTTagCompound, 
//            NBTTagList, EntityLiving, EntityPlayer, InventoryPlayer

public class EntityMinecart extends Entity
    implements IInventory
{

    public EntityMinecart(World world)
    {
        super(world);
        cargoItems = new ItemStack[36];
        minecartCurrentDamage = 0;
        minecartTimeSinceHit = 0;
        minecartRockDirection = 1;
        field_856_i = false;
        preventEntitySpawning = true;
        setSize(0.98F, 0.7F);
        yOffset = height / 2.0F;
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
    }

    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return entity.boundingBox;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return null;
    }

    public boolean canBePushed()
    {
        return true;
    }

    public EntityMinecart(World world, double d, double d1, double d2, 
            int i)
    {
        this(world);
        setPosition(d, d1 + (double)yOffset, d2);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = d;
        prevPosY = d1;
        prevPosZ = d2;
        minecartType = i;
    }

    public double getMountedYOffset()
    {
        return (double)height * 0.0D - 0.30000001192092896D;
    }

    public boolean attackEntityFrom(Entity entity, int i)
    {
        if(worldObj.multiplayerWorld || isDead)
        {
            return true;
        }
        minecartRockDirection = -minecartRockDirection;
        minecartTimeSinceHit = 10;
        setBeenAttacked();
        minecartCurrentDamage += i * 10;
        if(minecartCurrentDamage > 40)
        {
            if(riddenByEntity != null)
            {
                riddenByEntity.mountEntity(this);
            }
            setEntityDead();
            dropItemWithOffset(Item.minecartEmpty.shiftedIndex, 1, 0.0F);
            if(minecartType == 1)
            {
                EntityMinecart entityminecart = this;
label0:
                for(int j = 0; j < entityminecart.getSizeInventory(); j++)
                {
                    ItemStack itemstack = entityminecart.getStackInSlot(j);
                    if(itemstack == null)
                    {
                        continue;
                    }
                    float f = rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = rand.nextFloat() * 0.8F + 0.1F;
                    do
                    {
                        if(itemstack.stackSize <= 0)
                        {
                            continue label0;
                        }
                        int k = rand.nextInt(21) + 10;
                        if(k > itemstack.stackSize)
                        {
                            k = itemstack.stackSize;
                        }
                        itemstack.stackSize -= k;
                        EntityItem entityitem = new EntityItem(worldObj, posX + (double)f, posY + (double)f1, posZ + (double)f2, new ItemStack(itemstack.itemID, k, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float)rand.nextGaussian() * f3;
                        entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float)rand.nextGaussian() * f3;
                        worldObj.entityJoinedWorld(entityitem);
                    } while(true);
                }

                dropItemWithOffset(Block.chest.blockID, 1, 0.0F);
            } else
            if(minecartType == 2)
            {
                dropItemWithOffset(Block.stoneOvenIdle.blockID, 1, 0.0F);
            }
        }
        return true;
    }

    public void performHurtAnimation()
    {
        System.out.println("Animating hurt");
        minecartRockDirection = -minecartRockDirection;
        minecartTimeSinceHit = 10;
        minecartCurrentDamage += minecartCurrentDamage * 10;
    }

    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    public void setEntityDead()
    {
label0:
        for(int i = 0; i < getSizeInventory(); i++)
        {
            ItemStack itemstack = getStackInSlot(i);
            if(itemstack == null)
            {
                continue;
            }
            float f = rand.nextFloat() * 0.8F + 0.1F;
            float f1 = rand.nextFloat() * 0.8F + 0.1F;
            float f2 = rand.nextFloat() * 0.8F + 0.1F;
            do
            {
                if(itemstack.stackSize <= 0)
                {
                    continue label0;
                }
                int j = rand.nextInt(21) + 10;
                if(j > itemstack.stackSize)
                {
                    j = itemstack.stackSize;
                }
                itemstack.stackSize -= j;
                EntityItem entityitem = new EntityItem(worldObj, posX + (double)f, posY + (double)f1, posZ + (double)f2, new ItemStack(itemstack.itemID, j, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = (float)rand.nextGaussian() * f3;
                entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)rand.nextGaussian() * f3;
                worldObj.entityJoinedWorld(entityitem);
            } while(true);
        }

        super.setEntityDead();
    }

    public void onUpdate()
    {
        if(minecartTimeSinceHit > 0)
        {
            minecartTimeSinceHit--;
        }
        if(minecartCurrentDamage > 0)
        {
            minecartCurrentDamage--;
        }
        if(worldObj.multiplayerWorld && field_9415_k > 0)
        {
            if(field_9415_k > 0)
            {
                double d = posX + (field_9414_l - posX) / (double)field_9415_k;
                double d1 = posY + (field_9413_m - posY) / (double)field_9415_k;
                double d3 = posZ + (field_9412_n - posZ) / (double)field_9415_k;
                double d4;
                for(d4 = field_9411_o - (double)rotationYaw; d4 < -180D; d4 += 360D) { }
                for(; d4 >= 180D; d4 -= 360D) { }
                rotationYaw += d4 / (double)field_9415_k;
                rotationPitch += (field_9410_p - (double)rotationPitch) / (double)field_9415_k;
                field_9415_k--;
                setPosition(d, d1, d3);
                setRotation(rotationYaw, rotationPitch);
            } else
            {
                setPosition(posX, posY, posZ);
                setRotation(rotationYaw, rotationPitch);
            }
            return;
        }
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= 0.039999999105930328D;
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(posY);
        int k = MathHelper.floor_double(posZ);
        if(BlockRail.isRailBlockAt(worldObj, i, j - 1, k))
        {
            j--;
        }
        double d2 = 0.40000000000000002D;
        boolean flag = false;
        double d5 = 0.0078125D;
        int l = worldObj.getBlockId(i, j, k);
        if(BlockRail.isRailBlock(l))
        {
            Vec3D vec3d = func_514_g(posX, posY, posZ);
            int i1 = worldObj.getBlockMetadata(i, j, k);
            posY = j;
            boolean flag1 = false;
            boolean flag2 = false;
            if(l == Block.railPowered.blockID)
            {
                flag1 = (i1 & 8) != 0;
                flag2 = !flag1;
            }
            if(((BlockRail)Block.blocksList[l]).getIsPowered())
            {
                i1 &= 7;
            }
            if(i1 >= 2 && i1 <= 5)
            {
                posY = j + 1;
            }
            if(i1 == 2)
            {
                motionX -= d5;
            }
            if(i1 == 3)
            {
                motionX += d5;
            }
            if(i1 == 4)
            {
                motionZ += d5;
            }
            if(i1 == 5)
            {
                motionZ -= d5;
            }
            int ai[][] = field_855_j[i1];
            double d9 = ai[1][0] - ai[0][0];
            double d10 = ai[1][2] - ai[0][2];
            double d11 = Math.sqrt(d9 * d9 + d10 * d10);
            double d12 = motionX * d9 + motionZ * d10;
            if(d12 < 0.0D)
            {
                d9 = -d9;
                d10 = -d10;
            }
            double d13 = Math.sqrt(motionX * motionX + motionZ * motionZ);
            motionX = (d13 * d9) / d11;
            motionZ = (d13 * d10) / d11;
            if(flag2)
            {
                double d16 = Math.sqrt(motionX * motionX + motionZ * motionZ);
                if(d16 < 0.029999999999999999D)
                {
                    motionX *= 0.0D;
                    motionY *= 0.0D;
                    motionZ *= 0.0D;
                } else
                {
                    motionX *= 0.5D;
                    motionY *= 0.0D;
                    motionZ *= 0.5D;
                }
            }
            double d17 = 0.0D;
            double d18 = (double)i + 0.5D + (double)ai[0][0] * 0.5D;
            double d19 = (double)k + 0.5D + (double)ai[0][2] * 0.5D;
            double d20 = (double)i + 0.5D + (double)ai[1][0] * 0.5D;
            double d21 = (double)k + 0.5D + (double)ai[1][2] * 0.5D;
            d9 = d20 - d18;
            d10 = d21 - d19;
            if(d9 == 0.0D)
            {
                posX = (double)i + 0.5D;
                d17 = posZ - (double)k;
            } else
            if(d10 == 0.0D)
            {
                posZ = (double)k + 0.5D;
                d17 = posX - (double)i;
            } else
            {
                double d22 = posX - d18;
                double d24 = posZ - d19;
                double d26 = (d22 * d9 + d24 * d10) * 2D;
                d17 = d26;
            }
            posX = d18 + d9 * d17;
            posZ = d19 + d10 * d17;
            setPosition(posX, posY + (double)yOffset, posZ);
            double d23 = motionX;
            double d25 = motionZ;
            if(riddenByEntity != null)
            {
                d23 *= 0.75D;
                d25 *= 0.75D;
            }
            if(d23 < -d2)
            {
                d23 = -d2;
            }
            if(d23 > d2)
            {
                d23 = d2;
            }
            if(d25 < -d2)
            {
                d25 = -d2;
            }
            if(d25 > d2)
            {
                d25 = d2;
            }
            moveEntity(d23, 0.0D, d25);
            if(ai[0][1] != 0 && MathHelper.floor_double(posX) - i == ai[0][0] && MathHelper.floor_double(posZ) - k == ai[0][2])
            {
                setPosition(posX, posY + (double)ai[0][1], posZ);
            } else
            if(ai[1][1] != 0 && MathHelper.floor_double(posX) - i == ai[1][0] && MathHelper.floor_double(posZ) - k == ai[1][2])
            {
                setPosition(posX, posY + (double)ai[1][1], posZ);
            }
            if(riddenByEntity != null)
            {
                motionX *= 0.99699997901916504D;
                motionY *= 0.0D;
                motionZ *= 0.99699997901916504D;
            } else
            {
                if(minecartType == 2)
                {
                    double d27 = MathHelper.sqrt_double(pushX * pushX + pushZ * pushZ);
                    if(d27 > 0.01D)
                    {
                        flag = true;
                        pushX /= d27;
                        pushZ /= d27;
                        double d29 = 0.040000000000000001D;
                        motionX *= 0.80000001192092896D;
                        motionY *= 0.0D;
                        motionZ *= 0.80000001192092896D;
                        motionX += pushX * d29;
                        motionZ += pushZ * d29;
                    } else
                    {
                        motionX *= 0.89999997615814209D;
                        motionY *= 0.0D;
                        motionZ *= 0.89999997615814209D;
                    }
                }
                motionX *= 0.95999997854232788D;
                motionY *= 0.0D;
                motionZ *= 0.95999997854232788D;
            }
            Vec3D vec3d1 = func_514_g(posX, posY, posZ);
            if(vec3d1 != null && vec3d != null)
            {
                double d28 = (vec3d.yCoord - vec3d1.yCoord) * 0.050000000000000003D;
                double d14 = Math.sqrt(motionX * motionX + motionZ * motionZ);
                if(d14 > 0.0D)
                {
                    motionX = (motionX / d14) * (d14 + d28);
                    motionZ = (motionZ / d14) * (d14 + d28);
                }
                setPosition(posX, vec3d1.yCoord, posZ);
            }
            int k1 = MathHelper.floor_double(posX);
            int l1 = MathHelper.floor_double(posZ);
            if(k1 != i || l1 != k)
            {
                double d15 = Math.sqrt(motionX * motionX + motionZ * motionZ);
                motionX = d15 * (double)(k1 - i);
                motionZ = d15 * (double)(l1 - k);
            }
            if(minecartType == 2)
            {
                double d30 = MathHelper.sqrt_double(pushX * pushX + pushZ * pushZ);
                if(d30 > 0.01D && motionX * motionX + motionZ * motionZ > 0.001D)
                {
                    pushX /= d30;
                    pushZ /= d30;
                    if(pushX * motionX + pushZ * motionZ < 0.0D)
                    {
                        pushX = 0.0D;
                        pushZ = 0.0D;
                    } else
                    {
                        pushX = motionX;
                        pushZ = motionZ;
                    }
                }
            }
            if(flag1)
            {
                double d31 = Math.sqrt(motionX * motionX + motionZ * motionZ);
                if(d31 > 0.01D)
                {
                    double d32 = 0.059999999999999998D;
                    motionX += (motionX / d31) * d32;
                    motionZ += (motionZ / d31) * d32;
                } else
                if(i1 == 1)
                {
                    if(worldObj.isBlockNormalCube(i - 1, j, k))
                    {
                        motionX = 0.02D;
                    } else
                    if(worldObj.isBlockNormalCube(i + 1, j, k))
                    {
                        motionX = -0.02D;
                    }
                } else
                if(i1 == 0)
                {
                    if(worldObj.isBlockNormalCube(i, j, k - 1))
                    {
                        motionZ = 0.02D;
                    } else
                    if(worldObj.isBlockNormalCube(i, j, k + 1))
                    {
                        motionZ = -0.02D;
                    }
                }
            }
        } else
        {
            if(motionX < -d2)
            {
                motionX = -d2;
            }
            if(motionX > d2)
            {
                motionX = d2;
            }
            if(motionZ < -d2)
            {
                motionZ = -d2;
            }
            if(motionZ > d2)
            {
                motionZ = d2;
            }
            if(onGround)
            {
                motionX *= 0.5D;
                motionY *= 0.5D;
                motionZ *= 0.5D;
            }
            moveEntity(motionX, motionY, motionZ);
            if(!onGround)
            {
                motionX *= 0.94999998807907104D;
                motionY *= 0.94999998807907104D;
                motionZ *= 0.94999998807907104D;
            }
        }
        rotationPitch = 0.0F;
        double d6 = prevPosX - posX;
        double d7 = prevPosZ - posZ;
        if(d6 * d6 + d7 * d7 > 0.001D)
        {
            rotationYaw = (float)((Math.atan2(d7, d6) * 180D) / 3.1415926535897931D);
            if(field_856_i)
            {
                rotationYaw += 180F;
            }
        }
        double d8;
        for(d8 = rotationYaw - prevRotationYaw; d8 >= 180D; d8 -= 360D) { }
        for(; d8 < -180D; d8 += 360D) { }
        if(d8 < -170D || d8 >= 170D)
        {
            rotationYaw += 180F;
            field_856_i = !field_856_i;
        }
        setRotation(rotationYaw, rotationPitch);
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        if(list != null && list.size() > 0)
        {
            for(int j1 = 0; j1 < list.size(); j1++)
            {
                Entity entity = (Entity)list.get(j1);
                if(entity != riddenByEntity && entity.canBePushed() && (entity instanceof EntityMinecart))
                {
                    entity.applyEntityCollision(this);
                }
            }

        }
        if(riddenByEntity != null && riddenByEntity.isDead)
        {
            riddenByEntity = null;
        }
        if(flag && rand.nextInt(4) == 0)
        {
            fuel--;
            if(fuel < 0)
            {
                pushX = pushZ = 0.0D;
            }
            worldObj.spawnParticle("largesmoke", posX, posY + 0.80000000000000004D, posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    public Vec3D func_515_a(double d, double d1, double d2, double d3)
    {
        int i = MathHelper.floor_double(d);
        int j = MathHelper.floor_double(d1);
        int k = MathHelper.floor_double(d2);
        if(BlockRail.isRailBlockAt(worldObj, i, j - 1, k))
        {
            j--;
        }
        int l = worldObj.getBlockId(i, j, k);
        if(BlockRail.isRailBlock(l))
        {
            int i1 = worldObj.getBlockMetadata(i, j, k);
            if(((BlockRail)Block.blocksList[l]).getIsPowered())
            {
                i1 &= 7;
            }
            d1 = j;
            if(i1 >= 2 && i1 <= 5)
            {
                d1 = j + 1;
            }
            int ai[][] = field_855_j[i1];
            double d4 = ai[1][0] - ai[0][0];
            double d5 = ai[1][2] - ai[0][2];
            double d6 = Math.sqrt(d4 * d4 + d5 * d5);
            d4 /= d6;
            d5 /= d6;
            d += d4 * d3;
            d2 += d5 * d3;
            if(ai[0][1] != 0 && MathHelper.floor_double(d) - i == ai[0][0] && MathHelper.floor_double(d2) - k == ai[0][2])
            {
                d1 += ai[0][1];
            } else
            if(ai[1][1] != 0 && MathHelper.floor_double(d) - i == ai[1][0] && MathHelper.floor_double(d2) - k == ai[1][2])
            {
                d1 += ai[1][1];
            }
            return func_514_g(d, d1, d2);
        } else
        {
            return null;
        }
    }

    public Vec3D func_514_g(double d, double d1, double d2)
    {
        int i = MathHelper.floor_double(d);
        int j = MathHelper.floor_double(d1);
        int k = MathHelper.floor_double(d2);
        if(BlockRail.isRailBlockAt(worldObj, i, j - 1, k))
        {
            j--;
        }
        int l = worldObj.getBlockId(i, j, k);
        if(BlockRail.isRailBlock(l))
        {
            int i1 = worldObj.getBlockMetadata(i, j, k);
            d1 = j;
            if(((BlockRail)Block.blocksList[l]).getIsPowered())
            {
                i1 &= 7;
            }
            if(i1 >= 2 && i1 <= 5)
            {
                d1 = j + 1;
            }
            int ai[][] = field_855_j[i1];
            double d3 = 0.0D;
            double d4 = (double)i + 0.5D + (double)ai[0][0] * 0.5D;
            double d5 = (double)j + 0.5D + (double)ai[0][1] * 0.5D;
            double d6 = (double)k + 0.5D + (double)ai[0][2] * 0.5D;
            double d7 = (double)i + 0.5D + (double)ai[1][0] * 0.5D;
            double d8 = (double)j + 0.5D + (double)ai[1][1] * 0.5D;
            double d9 = (double)k + 0.5D + (double)ai[1][2] * 0.5D;
            double d10 = d7 - d4;
            double d11 = (d8 - d5) * 2D;
            double d12 = d9 - d6;
            if(d10 == 0.0D)
            {
                d = (double)i + 0.5D;
                d3 = d2 - (double)k;
            } else
            if(d12 == 0.0D)
            {
                d2 = (double)k + 0.5D;
                d3 = d - (double)i;
            } else
            {
                double d13 = d - d4;
                double d14 = d2 - d6;
                double d15 = (d13 * d10 + d14 * d12) * 2D;
                d3 = d15;
            }
            d = d4 + d10 * d3;
            d1 = d5 + d11 * d3;
            d2 = d6 + d12 * d3;
            if(d11 < 0.0D)
            {
                d1++;
            }
            if(d11 > 0.0D)
            {
                d1 += 0.5D;
            }
            return Vec3D.createVector(d, d1, d2);
        } else
        {
            return null;
        }
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setInteger("Type", minecartType);
        if(minecartType == 2)
        {
            nbttagcompound.setDouble("PushX", pushX);
            nbttagcompound.setDouble("PushZ", pushZ);
            nbttagcompound.setShort("Fuel", (short)fuel);
        } else
        if(minecartType == 1)
        {
            NBTTagList nbttaglist = new NBTTagList();
            for(int i = 0; i < cargoItems.length; i++)
            {
                if(cargoItems[i] != null)
                {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    nbttagcompound1.setByte("Slot", (byte)i);
                    cargoItems[i].writeToNBT(nbttagcompound1);
                    nbttaglist.setTag(nbttagcompound1);
                }
            }

            nbttagcompound.setTag("Items", nbttaglist);
        }
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        minecartType = nbttagcompound.getInteger("Type");
        if(minecartType == 2)
        {
            pushX = nbttagcompound.getDouble("PushX");
            pushZ = nbttagcompound.getDouble("PushZ");
            fuel = nbttagcompound.getShort("Fuel");
        } else
        if(minecartType == 1)
        {
            NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
            cargoItems = new ItemStack[getSizeInventory()];
            for(int i = 0; i < nbttaglist.tagCount(); i++)
            {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
                int j = nbttagcompound1.getByte("Slot") & 0xff;
                if(j >= 0 && j < cargoItems.length)
                {
                    cargoItems[j] = new ItemStack(nbttagcompound1);
                }
            }

        }
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public void applyEntityCollision(Entity entity)
    {
        if(worldObj.multiplayerWorld)
        {
            return;
        }
        if(entity == riddenByEntity)
        {
            return;
        }
        if((entity instanceof EntityLiving) && !(entity instanceof EntityPlayer) && minecartType == 0 && motionX * motionX + motionZ * motionZ > 0.01D && riddenByEntity == null && entity.ridingEntity == null)
        {
            entity.mountEntity(this);
        }
        double d = entity.posX - posX;
        double d1 = entity.posZ - posZ;
        double d2 = d * d + d1 * d1;
        if(d2 >= 9.9999997473787516E-005D)
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
            d *= 0.10000000149011612D;
            d1 *= 0.10000000149011612D;
            d *= 1.0F - entityCollisionReduction;
            d1 *= 1.0F - entityCollisionReduction;
            d *= 0.5D;
            d1 *= 0.5D;
            if(entity instanceof EntityMinecart)
            {
                double d4 = entity.posX - posX;
                double d5 = entity.posZ - posZ;
                double d6 = d4 * entity.motionZ + d5 * entity.prevPosX;
                d6 *= d6;
                if(d6 > 5D)
                {
                    return;
                }
                double d7 = entity.motionX + motionX;
                double d8 = entity.motionZ + motionZ;
                if(((EntityMinecart)entity).minecartType == 2 && minecartType != 2)
                {
                    motionX *= 0.20000000298023224D;
                    motionZ *= 0.20000000298023224D;
                    addVelocity(entity.motionX - d, 0.0D, entity.motionZ - d1);
                    entity.motionX *= 0.69999998807907104D;
                    entity.motionZ *= 0.69999998807907104D;
                } else
                if(((EntityMinecart)entity).minecartType != 2 && minecartType == 2)
                {
                    entity.motionX *= 0.20000000298023224D;
                    entity.motionZ *= 0.20000000298023224D;
                    entity.addVelocity(motionX + d, 0.0D, motionZ + d1);
                    motionX *= 0.69999998807907104D;
                    motionZ *= 0.69999998807907104D;
                } else
                {
                    d7 /= 2D;
                    d8 /= 2D;
                    motionX *= 0.20000000298023224D;
                    motionZ *= 0.20000000298023224D;
                    addVelocity(d7 - d, 0.0D, d8 - d1);
                    entity.motionX *= 0.20000000298023224D;
                    entity.motionZ *= 0.20000000298023224D;
                    entity.addVelocity(d7 + d, 0.0D, d8 + d1);
                }
            } else
            {
                addVelocity(-d, 0.0D, -d1);
                entity.addVelocity(d / 4D, 0.0D, d1 / 4D);
            }
        }
    }

    public int getSizeInventory()
    {
        return 27;
    }

    public ItemStack getStackInSlot(int i)
    {
        return cargoItems[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if(cargoItems[i] != null)
        {
            if(cargoItems[i].stackSize <= j)
            {
                ItemStack itemstack = cargoItems[i];
                cargoItems[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = cargoItems[i].splitStack(j);
            if(cargoItems[i].stackSize == 0)
            {
                cargoItems[i] = null;
            }
            return itemstack1;
        } else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        cargoItems[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    public String getInvName()
    {
        return "Minecart";
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void onInventoryChanged()
    {
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        if(minecartType == 0)
        {
            if(riddenByEntity != null && (riddenByEntity instanceof EntityPlayer) && riddenByEntity != entityplayer)
            {
                return true;
            }
            if(!worldObj.multiplayerWorld)
            {
                entityplayer.mountEntity(this);
            }
        } else
        if(minecartType == 1)
        {
            if(!worldObj.multiplayerWorld)
            {
                entityplayer.displayGUIChest(this);
            }
        } else
        if(minecartType == 2)
        {
            ItemStack itemstack = entityplayer.inventory.getCurrentItem();
            if(itemstack != null && itemstack.itemID == Item.coal.shiftedIndex)
            {
                if(--itemstack.stackSize == 0)
                {
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                }
                fuel += 1200;
            }
            pushX = posX - entityplayer.posX;
            pushZ = posZ - entityplayer.posZ;
        }
        return true;
    }

    public void setPositionAndRotation2(double d, double d1, double d2, float f, 
            float f1, int i)
    {
        field_9414_l = d;
        field_9413_m = d1;
        field_9412_n = d2;
        field_9411_o = f;
        field_9410_p = f1;
        field_9415_k = i + 2;
        motionX = field_9409_q;
        motionY = field_9408_r;
        motionZ = field_9407_s;
    }

    public void setVelocity(double d, double d1, double d2)
    {
        field_9409_q = motionX = d;
        field_9408_r = motionY = d1;
        field_9407_s = motionZ = d2;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(isDead)
        {
            return false;
        }
        return entityplayer.getDistanceSqToEntity(this) <= 64D;
    }

    private ItemStack cargoItems[];
    public int minecartCurrentDamage;
    public int minecartTimeSinceHit;
    public int minecartRockDirection;
    private boolean field_856_i;
    public int minecartType;
    public int fuel;
    public double pushX;
    public double pushZ;
    private static final int field_855_j[][][] = {
        {
            {
                0, 0, -1
            }, {
                0, 0, 1
            }
        }, {
            {
                -1, 0, 0
            }, {
                1, 0, 0
            }
        }, {
            {
                -1, -1, 0
            }, {
                1, 0, 0
            }
        }, {
            {
                -1, 0, 0
            }, {
                1, -1, 0
            }
        }, {
            {
                0, 0, -1
            }, {
                0, -1, 1
            }
        }, {
            {
                0, -1, -1
            }, {
                0, 0, 1
            }
        }, {
            {
                0, 0, 1
            }, {
                1, 0, 0
            }
        }, {
            {
                0, 0, 1
            }, {
                -1, 0, 0
            }
        }, {
            {
                0, 0, -1
            }, {
                -1, 0, 0
            }
        }, {
            {
                0, 0, -1
            }, {
                1, 0, 0
            }
        }
    };
    private int field_9415_k;
    private double field_9414_l;
    private double field_9413_m;
    private double field_9412_n;
    private double field_9411_o;
    private double field_9410_p;
    private double field_9409_q;
    private double field_9408_r;
    private double field_9407_s;

}
