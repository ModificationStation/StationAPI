// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            EntityMob, World, Entity, MathHelper, 
//            Item, NBTTagCompound

public class EntitySpider extends EntityMob
{

    public EntitySpider(World world)
    {
        super(world);
        texture = "/mob/spider.png";
        setSize(1.4F, 0.9F);
        moveSpeed = 0.8F;
    }

    public double getMountedYOffset()
    {
        return (double)height * 0.75D - 0.5D;
    }

    protected boolean func_25017_l()
    {
        return false;
    }

    protected Entity findPlayerToAttack()
    {
        float f = getEntityBrightness(1.0F);
        if(f < 0.5F)
        {
            double d = 16D;
            return worldObj.getClosestPlayerToEntity(this, d);
        } else
        {
            return null;
        }
    }

    protected String getLivingSound()
    {
        return "mob.spider";
    }

    protected String getHurtSound()
    {
        return "mob.spider";
    }

    protected String getDeathSound()
    {
        return "mob.spiderdeath";
    }

    protected void attackEntity(Entity entity, float f)
    {
        float f1 = getEntityBrightness(1.0F);
        if(f1 > 0.5F && rand.nextInt(100) == 0)
        {
            playerToAttack = null;
            return;
        }
        if(f > 2.0F && f < 6F && rand.nextInt(10) == 0)
        {
            if(onGround)
            {
                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                float f2 = MathHelper.sqrt_double(d * d + d1 * d1);
                motionX = (d / (double)f2) * 0.5D * 0.80000001192092896D + motionX * 0.20000000298023224D;
                motionZ = (d1 / (double)f2) * 0.5D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
                motionY = 0.40000000596046448D;
            }
        } else
        {
            super.attackEntity(entity, f);
        }
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
    }

    protected int getDropItemId()
    {
        return Item.silk.shiftedIndex;
    }

    public boolean isOnLadder()
    {
        return isCollidedHorizontally;
    }
}
