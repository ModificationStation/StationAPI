// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            EntityWaterMob, ItemStack, Item, AxisAlignedBB, 
//            Material, World, MathHelper, NBTTagCompound, 
//            EntityPlayer

public class EntitySquid extends EntityWaterMob
{

    public EntitySquid(World world)
    {
        super(world);
        field_21089_a = 0.0F;
        field_21088_b = 0.0F;
        field_21087_c = 0.0F;
        field_21086_f = 0.0F;
        field_21085_g = 0.0F;
        field_21084_h = 0.0F;
        field_21083_i = 0.0F;
        field_21082_j = 0.0F;
        randomMotionSpeed = 0.0F;
        field_21080_l = 0.0F;
        field_21079_m = 0.0F;
        randomMotionVecX = 0.0F;
        randomMotionVecY = 0.0F;
        randomMotionVecZ = 0.0F;
        texture = "/mob/squid.png";
        setSize(0.95F, 0.95F);
        field_21080_l = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
    }

    protected String getLivingSound()
    {
        return null;
    }

    protected String getHurtSound()
    {
        return null;
    }

    protected String getDeathSound()
    {
        return null;
    }

    protected float getSoundVolume()
    {
        return 0.4F;
    }

    protected int getDropItemId()
    {
        return 0;
    }

    protected void dropFewItems()
    {
        int i = rand.nextInt(3) + 1;
        for(int j = 0; j < i; j++)
        {
            entityDropItem(new ItemStack(Item.dyePowder, 1, 0), 0.0F);
        }

    }

    public boolean interact(EntityPlayer entityplayer)
    {
        return false;
    }

    public boolean isInWater()
    {
        return worldObj.handleMaterialAcceleration(boundingBox.expand(0.0D, -0.60000002384185791D, 0.0D), Material.water, this);
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        field_21088_b = field_21089_a;
        field_21086_f = field_21087_c;
        field_21084_h = field_21085_g;
        field_21082_j = field_21083_i;
        field_21085_g += field_21080_l;
        if(field_21085_g > 6.283185F)
        {
            field_21085_g -= 6.283185F;
            if(rand.nextInt(10) == 0)
            {
                field_21080_l = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
            }
        }
        if(isInWater())
        {
            if(field_21085_g < 3.141593F)
            {
                float f = field_21085_g / 3.141593F;
                field_21083_i = MathHelper.sin(f * f * 3.141593F) * 3.141593F * 0.25F;
                if((double)f > 0.75D)
                {
                    randomMotionSpeed = 1.0F;
                    field_21079_m = 1.0F;
                } else
                {
                    field_21079_m = field_21079_m * 0.8F;
                }
            } else
            {
                field_21083_i = 0.0F;
                randomMotionSpeed = randomMotionSpeed * 0.9F;
                field_21079_m = field_21079_m * 0.99F;
            }
            if(!isMultiplayerEntity)
            {
                motionX = randomMotionVecX * randomMotionSpeed;
                motionY = randomMotionVecY * randomMotionSpeed;
                motionZ = randomMotionVecZ * randomMotionSpeed;
            }
            float f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            renderYawOffset += ((-(float)Math.atan2(motionX, motionZ) * 180F) / 3.141593F - renderYawOffset) * 0.1F;
            rotationYaw = renderYawOffset;
            field_21087_c = field_21087_c + 3.141593F * field_21079_m * 1.5F;
            field_21089_a += ((-(float)Math.atan2(f1, motionY) * 180F) / 3.141593F - field_21089_a) * 0.1F;
        } else
        {
            field_21083_i = MathHelper.abs(MathHelper.sin(field_21085_g)) * 3.141593F * 0.25F;
            if(!isMultiplayerEntity)
            {
                motionX = 0.0D;
                motionY -= 0.080000000000000002D;
                motionY *= 0.98000001907348633D;
                motionZ = 0.0D;
            }
            field_21089_a += (double)(-90F - field_21089_a) * 0.02D;
        }
    }

    public void moveEntityWithHeading(float f, float f1)
    {
        moveEntity(motionX, motionY, motionZ);
    }

    protected void updatePlayerActionState()
    {
        if(rand.nextInt(50) == 0 || !inWater || randomMotionVecX == 0.0F && randomMotionVecY == 0.0F && randomMotionVecZ == 0.0F)
        {
            float f = rand.nextFloat() * 3.141593F * 2.0F;
            randomMotionVecX = MathHelper.cos(f) * 0.2F;
            randomMotionVecY = -0.1F + rand.nextFloat() * 0.2F;
            randomMotionVecZ = MathHelper.sin(f) * 0.2F;
        }
        func_27021_X();
    }

    public float field_21089_a;
    public float field_21088_b;
    public float field_21087_c;
    public float field_21086_f;
    public float field_21085_g;
    public float field_21084_h;
    public float field_21083_i;
    public float field_21082_j;
    private float randomMotionSpeed;
    private float field_21080_l;
    private float field_21079_m;
    private float randomMotionVecX;
    private float randomMotionVecY;
    private float randomMotionVecZ;
}
