// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            EntityLiving, World, Entity, AxisAlignedBB, 
//            MathHelper, PathEntity, Vec3D

public class EntityCreature extends EntityLiving
{

    public EntityCreature(World world)
    {
        super(world);
        hasAttacked = false;
    }

    protected boolean isMovementCeased()
    {
        return false;
    }

    protected void updatePlayerActionState()
    {
        hasAttacked = isMovementCeased();
        float f = 16F;
        if(playerToAttack == null)
        {
            playerToAttack = findPlayerToAttack();
            if(playerToAttack != null)
            {
                pathToEntity = worldObj.getPathToEntity(this, playerToAttack, f);
            }
        } else
        if(!playerToAttack.isEntityAlive())
        {
            playerToAttack = null;
        } else
        {
            float f1 = playerToAttack.getDistanceToEntity(this);
            if(canEntityBeSeen(playerToAttack))
            {
                attackEntity(playerToAttack, f1);
            } else
            {
                attackBlockedEntity(playerToAttack, f1);
            }
        }
        if(!hasAttacked && playerToAttack != null && (pathToEntity == null || rand.nextInt(20) == 0))
        {
            pathToEntity = worldObj.getPathToEntity(this, playerToAttack, f);
        } else
        if(!hasAttacked && (pathToEntity == null && rand.nextInt(80) == 0 || rand.nextInt(80) == 0))
        {
            func_31026_E();
        }
        int i = MathHelper.floor_double(boundingBox.minY + 0.5D);
        boolean flag = isInWater();
        boolean flag1 = handleLavaMovement();
        rotationPitch = 0.0F;
        if(pathToEntity == null || rand.nextInt(100) == 0)
        {
            super.updatePlayerActionState();
            pathToEntity = null;
            return;
        }
        Vec3D vec3d = pathToEntity.getPosition(this);
        for(double d = width * 2.0F; vec3d != null && vec3d.squareDistanceTo(posX, vec3d.yCoord, posZ) < d * d;)
        {
            pathToEntity.incrementPathIndex();
            if(pathToEntity.isFinished())
            {
                vec3d = null;
                pathToEntity = null;
            } else
            {
                vec3d = pathToEntity.getPosition(this);
            }
        }

        isJumping = false;
        if(vec3d != null)
        {
            double d1 = vec3d.xCoord - posX;
            double d2 = vec3d.zCoord - posZ;
            double d3 = vec3d.yCoord - (double)i;
            float f2 = (float)((Math.atan2(d2, d1) * 180D) / 3.1415927410125732D) - 90F;
            float f3 = f2 - rotationYaw;
            moveForward = moveSpeed;
            for(; f3 < -180F; f3 += 360F) { }
            for(; f3 >= 180F; f3 -= 360F) { }
            if(f3 > 30F)
            {
                f3 = 30F;
            }
            if(f3 < -30F)
            {
                f3 = -30F;
            }
            rotationYaw += f3;
            if(hasAttacked && playerToAttack != null)
            {
                double d4 = playerToAttack.posX - posX;
                double d5 = playerToAttack.posZ - posZ;
                float f5 = rotationYaw;
                rotationYaw = (float)((Math.atan2(d5, d4) * 180D) / 3.1415927410125732D) - 90F;
                float f4 = (((f5 - rotationYaw) + 90F) * 3.141593F) / 180F;
                moveStrafing = -MathHelper.sin(f4) * moveForward * 1.0F;
                moveForward = MathHelper.cos(f4) * moveForward * 1.0F;
            }
            if(d3 > 0.0D)
            {
                isJumping = true;
            }
        }
        if(playerToAttack != null)
        {
            faceEntity(playerToAttack, 30F, 30F);
        }
        if(isCollidedHorizontally && !hasPath())
        {
            isJumping = true;
        }
        if(rand.nextFloat() < 0.8F && (flag || flag1))
        {
            isJumping = true;
        }
    }

    protected void func_31026_E()
    {
        boolean flag = false;
        int i = -1;
        int j = -1;
        int k = -1;
        float f = -99999F;
        for(int l = 0; l < 10; l++)
        {
            int i1 = MathHelper.floor_double((posX + (double)rand.nextInt(13)) - 6D);
            int j1 = MathHelper.floor_double((posY + (double)rand.nextInt(7)) - 3D);
            int k1 = MathHelper.floor_double((posZ + (double)rand.nextInt(13)) - 6D);
            float f1 = getBlockPathWeight(i1, j1, k1);
            if(f1 > f)
            {
                f = f1;
                i = i1;
                j = j1;
                k = k1;
                flag = true;
            }
        }

        if(flag)
        {
            pathToEntity = worldObj.getEntityPathToXYZ(this, i, j, k, 10F);
        }
    }

    protected void attackEntity(Entity entity, float f)
    {
    }

    protected void attackBlockedEntity(Entity entity, float f)
    {
    }

    protected float getBlockPathWeight(int i, int j, int k)
    {
        return 0.0F;
    }

    protected Entity findPlayerToAttack()
    {
        return null;
    }

    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(boundingBox.minY);
        int k = MathHelper.floor_double(posZ);
        return super.getCanSpawnHere() && getBlockPathWeight(i, j, k) >= 0.0F;
    }

    public boolean hasPath()
    {
        return pathToEntity != null;
    }

    public void setPathToEntity(PathEntity pathentity)
    {
        pathToEntity = pathentity;
    }

    public Entity getTarget()
    {
        return playerToAttack;
    }

    public void setTarget(Entity entity)
    {
        playerToAttack = entity;
    }

    private PathEntity pathToEntity;
    protected Entity playerToAttack;
    protected boolean hasAttacked;
}
