// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            EntityLiving, IMob, DataWatcher, NBTTagCompound, 
//            MathHelper, AxisAlignedBB, World, EntityPlayer, 
//            Item, Chunk

public class EntitySlime extends EntityLiving
    implements IMob
{

    public EntitySlime(World world)
    {
        super(world);
        ticksTillJump = 0;
        texture = "/mob/slime.png";
        int i = 1 << rand.nextInt(3);
        yOffset = 0.0F;
        ticksTillJump = rand.nextInt(20) + 10;
        setSlimeSize(i);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, new Byte((byte)1));
    }

    public void setSlimeSize(int i)
    {
        dataWatcher.updateObject(16, new Byte((byte)i));
        setSize(0.6F * (float)i, 0.6F * (float)i);
        health = i * i;
        setPosition(posX, posY, posZ);
    }

    public int func_25027_m()
    {
        return dataWatcher.getWatchableObjectByte(16);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Size", func_25027_m() - 1);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        setSlimeSize(nbttagcompound.getInteger("Size") + 1);
    }

    public void onUpdate()
    {
        field_400_b = field_401_a;
        boolean flag = onGround;
        super.onUpdate();
        if(onGround && !flag)
        {
            int i = func_25027_m();
            for(int j = 0; j < i * 8; j++)
            {
                float f = rand.nextFloat() * 3.141593F * 2.0F;
                float f1 = rand.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * (float)i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * (float)i * 0.5F * f1;
                worldObj.spawnParticle("slime", posX + (double)f2, boundingBox.minY, posZ + (double)f3, 0.0D, 0.0D, 0.0D);
            }

            if(i > 2)
            {
                worldObj.playSoundAtEntity(this, "mob.slime", getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }
            field_401_a = -0.5F;
        }
        field_401_a = field_401_a * 0.6F;
    }

    protected void updatePlayerActionState()
    {
        func_27013_Q();
        EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, 16D);
        if(entityplayer != null)
        {
            faceEntity(entityplayer, 10F, 20F);
        }
        if(onGround && ticksTillJump-- <= 0)
        {
            ticksTillJump = rand.nextInt(20) + 10;
            if(entityplayer != null)
            {
                ticksTillJump /= 3;
            }
            isJumping = true;
            if(func_25027_m() > 1)
            {
                worldObj.playSoundAtEntity(this, "mob.slime", getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }
            field_401_a = 1.0F;
            moveStrafing = 1.0F - rand.nextFloat() * 2.0F;
            moveForward = 1 * func_25027_m();
        } else
        {
            isJumping = false;
            if(onGround)
            {
                moveStrafing = moveForward = 0.0F;
            }
        }
    }

    public void setEntityDead()
    {
        int i = func_25027_m();
        if(!worldObj.singleplayerWorld && i > 1 && health == 0)
        {
            for(int j = 0; j < 4; j++)
            {
                float f = (((float)(j % 2) - 0.5F) * (float)i) / 4F;
                float f1 = (((float)(j / 2) - 0.5F) * (float)i) / 4F;
                EntitySlime entityslime = new EntitySlime(worldObj);
                entityslime.setSlimeSize(i / 2);
                entityslime.setLocationAndAngles(posX + (double)f, posY + 0.5D, posZ + (double)f1, rand.nextFloat() * 360F, 0.0F);
                worldObj.entityJoinedWorld(entityslime);
            }

        }
        super.setEntityDead();
    }

    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
        int i = func_25027_m();
        if(i > 1 && canEntityBeSeen(entityplayer) && (double)getDistanceToEntity(entityplayer) < 0.59999999999999998D * (double)i && entityplayer.attackEntityFrom(this, i))
        {
            worldObj.playSoundAtEntity(this, "mob.slimeattack", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        }
    }

    protected String getHurtSound()
    {
        return "mob.slime";
    }

    protected String getDeathSound()
    {
        return "mob.slime";
    }

    protected int getDropItemId()
    {
        if(func_25027_m() == 1)
        {
            return Item.slimeBall.shiftedIndex;
        } else
        {
            return 0;
        }
    }

    public boolean getCanSpawnHere()
    {
        Chunk chunk = worldObj.getChunkFromBlockCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ));
        return (func_25027_m() == 1 || worldObj.difficultySetting > 0) && rand.nextInt(10) == 0 && chunk.func_334_a(0x3ad8025fL).nextInt(10) == 0 && posY < 16D;
    }

    protected float getSoundVolume()
    {
        return 0.6F;
    }

    public float field_401_a;
    public float field_400_b;
    private int ticksTillJump;
}
