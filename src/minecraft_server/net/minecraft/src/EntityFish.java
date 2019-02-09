// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Entity, EntityPlayer, MathHelper, World, 
//            ItemStack, Item, AxisAlignedBB, Vec3D, 
//            MovingObjectPosition, Material, NBTTagCompound, EntityItem, 
//            StatList

public class EntityFish extends Entity
{

    public EntityFish(World world)
    {
        super(world);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inGround = false;
        shake = 0;
        ticksInAir = 0;
        ticksCatchable = 0;
        bobber = null;
        setSize(0.25F, 0.25F);
        field_28008_bI = true;
    }

    public EntityFish(World world, EntityPlayer entityplayer)
    {
        super(world);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inGround = false;
        shake = 0;
        ticksInAir = 0;
        ticksCatchable = 0;
        bobber = null;
        field_28008_bI = true;
        angler = entityplayer;
        angler.fishEntity = this;
        setSize(0.25F, 0.25F);
        setLocationAndAngles(entityplayer.posX, (entityplayer.posY + 1.6200000000000001D) - (double)entityplayer.yOffset, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
        posY -= 0.10000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
        setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        float f = 0.4F;
        motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
        motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
        motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F) * f;
        func_6142_a(motionX, motionY, motionZ, 1.5F, 1.0F);
    }

    protected void entityInit()
    {
    }

    public void func_6142_a(double d, double d1, double d2, float f, 
            float f1)
    {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d1 += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d2 += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        motionX = d;
        motionY = d1;
        motionZ = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / 3.1415927410125732D);
        prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f3) * 180D) / 3.1415927410125732D);
        ticksInGround = 0;
    }

    public void onUpdate()
    {
        super.onUpdate();
        if(field_6149_an > 0)
        {
            double d = posX + (field_6148_ao - posX) / (double)field_6149_an;
            double d1 = posY + (field_6147_ap - posY) / (double)field_6149_an;
            double d2 = posZ + (field_6146_aq - posZ) / (double)field_6149_an;
            double d4;
            for(d4 = field_6145_ar - (double)rotationYaw; d4 < -180D; d4 += 360D) { }
            for(; d4 >= 180D; d4 -= 360D) { }
            rotationYaw += d4 / (double)field_6149_an;
            rotationPitch += (field_6144_as - (double)rotationPitch) / (double)field_6149_an;
            field_6149_an--;
            setPosition(d, d1, d2);
            setRotation(rotationYaw, rotationPitch);
            return;
        }
        if(!worldObj.singleplayerWorld)
        {
            ItemStack itemstack = angler.getCurrentEquippedItem();
            if(angler.isDead || !angler.isEntityAlive() || itemstack == null || itemstack.getItem() != Item.fishingRod || getDistanceSqToEntity(angler) > 1024D)
            {
                setEntityDead();
                angler.fishEntity = null;
                return;
            }
            if(bobber != null)
            {
                if(bobber.isDead)
                {
                    bobber = null;
                } else
                {
                    posX = bobber.posX;
                    posY = bobber.boundingBox.minY + (double)bobber.height * 0.80000000000000004D;
                    posZ = bobber.posZ;
                    return;
                }
            }
        }
        if(shake > 0)
        {
            shake--;
        }
        if(inGround)
        {
            int i = worldObj.getBlockId(xTile, yTile, zTile);
            if(i != inTile)
            {
                inGround = false;
                motionX *= rand.nextFloat() * 0.2F;
                motionY *= rand.nextFloat() * 0.2F;
                motionZ *= rand.nextFloat() * 0.2F;
                ticksInGround = 0;
                ticksInAir = 0;
            } else
            {
                ticksInGround++;
                if(ticksInGround == 1200)
                {
                    setEntityDead();
                }
                return;
            }
        } else
        {
            ticksInAir++;
        }
        Vec3D vec3d = Vec3D.createVector(posX, posY, posZ);
        Vec3D vec3d1 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
        MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec3d, vec3d1);
        vec3d = Vec3D.createVector(posX, posY, posZ);
        vec3d1 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
        if(movingobjectposition != null)
        {
            vec3d1 = Vec3D.createVector(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }
        Entity entity = null;
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        double d3 = 0.0D;
        for(int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);
            if(!entity1.canBeCollidedWith() || entity1 == angler && ticksInAir < 5)
            {
                continue;
            }
            float f2 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f2, f2, f2);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.func_706_a(vec3d, vec3d1);
            if(movingobjectposition1 == null)
            {
                continue;
            }
            double d6 = vec3d.distanceTo(movingobjectposition1.hitVec);
            if(d6 < d3 || d3 == 0.0D)
            {
                entity = entity1;
                d3 = d6;
            }
        }

        if(entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }
        if(movingobjectposition != null)
        {
            if(movingobjectposition.entityHit != null)
            {
                if(movingobjectposition.entityHit.attackEntityFrom(angler, 0))
                {
                    bobber = movingobjectposition.entityHit;
                }
            } else
            {
                inGround = true;
            }
        }
        if(inGround)
        {
            return;
        }
        moveEntity(motionX, motionY, motionZ);
        float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / 3.1415927410125732D);
        for(rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / 3.1415927410125732D); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
        for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
        float f1 = 0.92F;
        if(onGround || isCollidedHorizontally)
        {
            f1 = 0.5F;
        }
        int k = 5;
        double d5 = 0.0D;
        for(int l = 0; l < k; l++)
        {
            double d8 = ((boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) * (double)(l + 0)) / (double)k) - 0.125D) + 0.125D;
            double d9 = ((boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) * (double)(l + 1)) / (double)k) - 0.125D) + 0.125D;
            AxisAlignedBB axisalignedbb1 = AxisAlignedBB.getBoundingBoxFromPool(boundingBox.minX, d8, boundingBox.minZ, boundingBox.maxX, d9, boundingBox.maxZ);
            if(worldObj.isAABBInMaterial(axisalignedbb1, Material.water))
            {
                d5 += 1.0D / (double)k;
            }
        }

        if(d5 > 0.0D)
        {
            if(ticksCatchable > 0)
            {
                ticksCatchable--;
            } else
            {
                char c = '\u01F4';
                if(worldObj.canLightningStrikeAt(MathHelper.floor_double(posX), MathHelper.floor_double(posY) + 1, MathHelper.floor_double(posZ)))
                {
                    c = '\u012C';
                }
                if(rand.nextInt(c) == 0)
                {
                    ticksCatchable = rand.nextInt(30) + 10;
                    motionY -= 0.20000000298023224D;
                    worldObj.playSoundAtEntity(this, "random.splash", 0.25F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
                    float f3 = MathHelper.floor_double(boundingBox.minY);
                    for(int i1 = 0; (float)i1 < 1.0F + width * 20F; i1++)
                    {
                        float f4 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                        float f6 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                        worldObj.spawnParticle("bubble", posX + (double)f4, f3 + 1.0F, posZ + (double)f6, motionX, motionY - (double)(rand.nextFloat() * 0.2F), motionZ);
                    }

                    for(int j1 = 0; (float)j1 < 1.0F + width * 20F; j1++)
                    {
                        float f5 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                        float f7 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                        worldObj.spawnParticle("splash", posX + (double)f5, f3 + 1.0F, posZ + (double)f7, motionX, motionY, motionZ);
                    }

                }
            }
        }
        if(ticksCatchable > 0)
        {
            motionY -= (double)(rand.nextFloat() * rand.nextFloat() * rand.nextFloat()) * 0.20000000000000001D;
        }
        double d7 = d5 * 2D - 1.0D;
        motionY += 0.039999999105930328D * d7;
        if(d5 > 0.0D)
        {
            f1 = (float)((double)f1 * 0.90000000000000002D);
            motionY *= 0.80000000000000004D;
        }
        motionX *= f1;
        motionY *= f1;
        motionZ *= f1;
        setPosition(posX, posY, posZ);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("xTile", (short)xTile);
        nbttagcompound.setShort("yTile", (short)yTile);
        nbttagcompound.setShort("zTile", (short)zTile);
        nbttagcompound.setByte("inTile", (byte)inTile);
        nbttagcompound.setByte("shake", (byte)shake);
        nbttagcompound.setByte("inGround", (byte)(inGround ? 1 : 0));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        xTile = nbttagcompound.getShort("xTile");
        yTile = nbttagcompound.getShort("yTile");
        zTile = nbttagcompound.getShort("zTile");
        inTile = nbttagcompound.getByte("inTile") & 0xff;
        shake = nbttagcompound.getByte("shake") & 0xff;
        inGround = nbttagcompound.getByte("inGround") == 1;
    }

    public int catchFish()
    {
        byte byte0 = 0;
        if(bobber != null)
        {
            double d = angler.posX - posX;
            double d2 = angler.posY - posY;
            double d4 = angler.posZ - posZ;
            double d6 = MathHelper.sqrt_double(d * d + d2 * d2 + d4 * d4);
            double d8 = 0.10000000000000001D;
            bobber.motionX += d * d8;
            bobber.motionY += d2 * d8 + (double)MathHelper.sqrt_double(d6) * 0.080000000000000002D;
            bobber.motionZ += d4 * d8;
            byte0 = 3;
        } else
        if(ticksCatchable > 0)
        {
            EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.fishRaw));
            double d1 = angler.posX - posX;
            double d3 = angler.posY - posY;
            double d5 = angler.posZ - posZ;
            double d7 = MathHelper.sqrt_double(d1 * d1 + d3 * d3 + d5 * d5);
            double d9 = 0.10000000000000001D;
            entityitem.motionX = d1 * d9;
            entityitem.motionY = d3 * d9 + (double)MathHelper.sqrt_double(d7) * 0.080000000000000002D;
            entityitem.motionZ = d5 * d9;
            worldObj.entityJoinedWorld(entityitem);
            angler.addStat(StatList.fishCaughtStat, 1);
            byte0 = 1;
        }
        if(inGround)
        {
            byte0 = 2;
        }
        setEntityDead();
        angler.fishEntity = null;
        return byte0;
    }

    private int xTile;
    private int yTile;
    private int zTile;
    private int inTile;
    private boolean inGround;
    public int shake;
    public EntityPlayer angler;
    private int ticksInGround;
    private int ticksInAir;
    private int ticksCatchable;
    public Entity bobber;
    private int field_6149_an;
    private double field_6148_ao;
    private double field_6147_ap;
    private double field_6146_aq;
    private double field_6145_ar;
    private double field_6144_as;
}
