// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;

// Referenced classes of package net.minecraft.src:
//            EntityLiving, InventoryPlayer, ContainerPlayer, World, 
//            ChunkCoordinates, DataWatcher, Container, StatList, 
//            MathHelper, AxisAlignedBB, Entity, ItemStack, 
//            Item, EntityItem, Material, NBTTagCompound, 
//            NBTTagList, EntityMob, EntityArrow, EntityCreeper, 
//            EntityGhast, EntityWolf, EnumStatus, WorldProvider, 
//            BlockBed, Block, IChunkProvider, EntityMinecart, 
//            AchievementList, EntityBoat, EntityPig, EntityFish, 
//            IInventory, TileEntityFurnace, TileEntityDispenser, TileEntitySign, 
//            StatBase

public abstract class EntityPlayer extends EntityLiving
{

    public EntityPlayer(World world)
    {
        super(world);
        inventory = new InventoryPlayer(this);
        field_9152_am = 0;
        score = 0;
        isSwinging = false;
        swingProgressInt = 0;
        timeUntilPortal = 20;
        inPortal = false;
        damageRemainder = 0;
        fishEntity = null;
        personalCraftingInventory = new ContainerPlayer(inventory, !world.singleplayerWorld);
        currentCraftingInventory = personalCraftingInventory;
        yOffset = 1.62F;
        ChunkCoordinates chunkcoordinates = world.getSpawnPoint();
        setLocationAndAngles((double)chunkcoordinates.posX + 0.5D, chunkcoordinates.posY + 1, (double)chunkcoordinates.posZ + 0.5D, 0.0F, 0.0F);
        health = 20;
        entityType = "humanoid";
        field_9117_aI = 180F;
        fireResistance = 20;
        texture = "/mob/char.png";
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public void onUpdate()
    {
        if(func_22057_E())
        {
            sleepTimer++;
            if(sleepTimer > 100)
            {
                sleepTimer = 100;
            }
            if(!worldObj.singleplayerWorld)
            {
                if(!isInBed())
                {
                    wakeUpPlayer(true, true, false);
                } else
                if(worldObj.isDaytime())
                {
                    wakeUpPlayer(false, true, true);
                }
            }
        } else
        if(sleepTimer > 0)
        {
            sleepTimer++;
            if(sleepTimer >= 110)
            {
                sleepTimer = 0;
            }
        }
        super.onUpdate();
        if(!worldObj.singleplayerWorld && currentCraftingInventory != null && !currentCraftingInventory.canInteractWith(this))
        {
            usePersonalCraftingInventory();
            currentCraftingInventory = personalCraftingInventory;
        }
        field_20047_ay = field_20050_aB;
        field_20046_az = field_20049_aC;
        field_20051_aA = field_20048_aD;
        double d = posX - field_20050_aB;
        double d1 = posY - field_20049_aC;
        double d2 = posZ - field_20048_aD;
        double d3 = 10D;
        if(d > d3)
        {
            field_20047_ay = field_20050_aB = posX;
        }
        if(d2 > d3)
        {
            field_20051_aA = field_20048_aD = posZ;
        }
        if(d1 > d3)
        {
            field_20046_az = field_20049_aC = posY;
        }
        if(d < -d3)
        {
            field_20047_ay = field_20050_aB = posX;
        }
        if(d2 < -d3)
        {
            field_20051_aA = field_20048_aD = posZ;
        }
        if(d1 < -d3)
        {
            field_20046_az = field_20049_aC = posY;
        }
        field_20050_aB += d * 0.25D;
        field_20048_aD += d2 * 0.25D;
        field_20049_aC += d1 * 0.25D;
        addStat(StatList.field_25114_j, 1);
        if(ridingEntity == null)
        {
            field_27995_d = null;
        }
    }

    protected boolean isMovementBlocked()
    {
        return health <= 0 || func_22057_E();
    }

    protected void usePersonalCraftingInventory()
    {
        currentCraftingInventory = personalCraftingInventory;
    }

    public void updateRidden()
    {
        double d = posX;
        double d1 = posY;
        double d2 = posZ;
        super.updateRidden();
        field_9150_ao = field_9149_ap;
        field_9149_ap = 0.0F;
        func_27015_h(posX - d, posY - d1, posZ - d2);
    }

    protected void updatePlayerActionState()
    {
        if(isSwinging)
        {
            swingProgressInt++;
            if(swingProgressInt >= 8)
            {
                swingProgressInt = 0;
                isSwinging = false;
            }
        } else
        {
            swingProgressInt = 0;
        }
        swingProgress = (float)swingProgressInt / 8F;
    }

    public void onLivingUpdate()
    {
        if(worldObj.difficultySetting == 0 && health < 20 && (ticksExisted % 20) * 12 == 0)
        {
            heal(1);
        }
        inventory.decrementAnimations();
        field_9150_ao = field_9149_ap;
        super.onLivingUpdate();
        float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        float f1 = (float)Math.atan(-motionY * 0.20000000298023224D) * 15F;
        if(f > 0.1F)
        {
            f = 0.1F;
        }
        if(!onGround || health <= 0)
        {
            f = 0.0F;
        }
        if(onGround || health <= 0)
        {
            f1 = 0.0F;
        }
        field_9149_ap += (f - field_9149_ap) * 0.4F;
        field_9101_aY += (f1 - field_9101_aY) * 0.8F;
        if(health > 0)
        {
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(1.0D, 0.0D, 1.0D));
            if(list != null)
            {
                for(int i = 0; i < list.size(); i++)
                {
                    Entity entity = (Entity)list.get(i);
                    if(!entity.isDead)
                    {
                        func_171_h(entity);
                    }
                }

            }
        }
    }

    private void func_171_h(Entity entity)
    {
        entity.onCollideWithPlayer(this);
    }

    public void onDeath(Entity entity)
    {
        super.onDeath(entity);
        setSize(0.2F, 0.2F);
        setPosition(posX, posY, posZ);
        motionY = 0.10000000149011612D;
        if(username.equals("Notch"))
        {
            dropPlayerItemWithRandomChoice(new ItemStack(Item.appleRed, 1), true);
        }
        inventory.dropAllItems();
        if(entity != null)
        {
            motionX = -MathHelper.cos(((attackedAtYaw + rotationYaw) * 3.141593F) / 180F) * 0.1F;
            motionZ = -MathHelper.sin(((attackedAtYaw + rotationYaw) * 3.141593F) / 180F) * 0.1F;
        } else
        {
            motionX = motionZ = 0.0D;
        }
        yOffset = 0.1F;
        addStat(StatList.field_25098_u, 1);
    }

    public void addToPlayerScore(Entity entity, int i)
    {
        score += i;
        if(entity instanceof EntityPlayer)
        {
            addStat(StatList.field_25096_w, 1);
        } else
        {
            addStat(StatList.field_25097_v, 1);
        }
    }

    public void dropCurrentItem()
    {
        dropPlayerItemWithRandomChoice(inventory.decrStackSize(inventory.currentItem, 1), false);
    }

    public void dropPlayerItem(ItemStack itemstack)
    {
        dropPlayerItemWithRandomChoice(itemstack, false);
    }

    public void dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag)
    {
        if(itemstack == null)
        {
            return;
        }
        EntityItem entityitem = new EntityItem(worldObj, posX, (posY - 0.30000001192092896D) + (double)getEyeHeight(), posZ, itemstack);
        entityitem.delayBeforeCanPickup = 40;
        float f = 0.1F;
        if(flag)
        {
            float f2 = rand.nextFloat() * 0.5F;
            float f4 = rand.nextFloat() * 3.141593F * 2.0F;
            entityitem.motionX = -MathHelper.sin(f4) * f2;
            entityitem.motionZ = MathHelper.cos(f4) * f2;
            entityitem.motionY = 0.20000000298023224D;
        } else
        {
            float f1 = 0.3F;
            entityitem.motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f1;
            entityitem.motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f1;
            entityitem.motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F) * f1 + 0.1F;
            f1 = 0.02F;
            float f3 = rand.nextFloat() * 3.141593F * 2.0F;
            f1 *= rand.nextFloat();
            entityitem.motionX += Math.cos(f3) * (double)f1;
            entityitem.motionY += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
            entityitem.motionZ += Math.sin(f3) * (double)f1;
        }
        joinEntityItemWithWorld(entityitem);
        addStat(StatList.field_25103_r, 1);
    }

    protected void joinEntityItemWithWorld(EntityItem entityitem)
    {
        worldObj.entityJoinedWorld(entityitem);
    }

    public float getCurrentPlayerStrVsBlock(Block block)
    {
        float f = inventory.getStrVsBlock(block);
        if(isInsideOfMaterial(Material.water))
        {
            f /= 5F;
        }
        if(!onGround)
        {
            f /= 5F;
        }
        return f;
    }

    public boolean canHarvestBlock(Block block)
    {
        return inventory.canHarvestBlock(block);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Inventory");
        inventory.readFromNBT(nbttaglist);
        dimension = nbttagcompound.getInteger("Dimension");
        sleeping = nbttagcompound.getBoolean("Sleeping");
        sleepTimer = nbttagcompound.getShort("SleepTimer");
        if(sleeping)
        {
            playerLocation = new ChunkCoordinates(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
            wakeUpPlayer(true, true, false);
        }
        if(nbttagcompound.hasKey("SpawnX") && nbttagcompound.hasKey("SpawnY") && nbttagcompound.hasKey("SpawnZ"))
        {
            spawnChunk = new ChunkCoordinates(nbttagcompound.getInteger("SpawnX"), nbttagcompound.getInteger("SpawnY"), nbttagcompound.getInteger("SpawnZ"));
        }
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setTag("Inventory", inventory.writeToNBT(new NBTTagList()));
        nbttagcompound.setInteger("Dimension", dimension);
        nbttagcompound.setBoolean("Sleeping", sleeping);
        nbttagcompound.setShort("SleepTimer", (short)sleepTimer);
        if(spawnChunk != null)
        {
            nbttagcompound.setInteger("SpawnX", spawnChunk.posX);
            nbttagcompound.setInteger("SpawnY", spawnChunk.posY);
            nbttagcompound.setInteger("SpawnZ", spawnChunk.posZ);
        }
    }

    public void displayGUIChest(IInventory iinventory)
    {
    }

    public void displayWorkbenchGUI(int i, int j, int k)
    {
    }

    public void onItemPickup(Entity entity, int i)
    {
    }

    public float getEyeHeight()
    {
        return 0.12F;
    }

    protected void resetHeight()
    {
        yOffset = 1.62F;
    }

    public boolean attackEntityFrom(Entity entity, int i)
    {
        age = 0;
        if(health <= 0)
        {
            return false;
        }
        if(func_22057_E() && !worldObj.singleplayerWorld)
        {
            wakeUpPlayer(true, true, false);
        }
        if((entity instanceof EntityMob) || (entity instanceof EntityArrow))
        {
            if(worldObj.difficultySetting == 0)
            {
                i = 0;
            }
            if(worldObj.difficultySetting == 1)
            {
                i = i / 3 + 1;
            }
            if(worldObj.difficultySetting == 3)
            {
                i = (i * 3) / 2;
            }
        }
        if(i == 0)
        {
            return false;
        }
        Object obj = entity;
        if((obj instanceof EntityArrow) && ((EntityArrow)obj).owner != null)
        {
            obj = ((EntityArrow)obj).owner;
        }
        if(obj instanceof EntityLiving)
        {
            func_25047_a((EntityLiving)obj, false);
        }
        addStat(StatList.field_25100_t, i);
        return super.attackEntityFrom(entity, i);
    }

    protected boolean isPVPEnabled()
    {
        return false;
    }

    protected void func_25047_a(EntityLiving entityliving, boolean flag)
    {
        if((entityliving instanceof EntityCreeper) || (entityliving instanceof EntityGhast))
        {
            return;
        }
        if(entityliving instanceof EntityWolf)
        {
            EntityWolf entitywolf = (EntityWolf)entityliving;
            if(entitywolf.func_25030_y() && username.equals(entitywolf.getOwner()))
            {
                return;
            }
        }
        if((entityliving instanceof EntityPlayer) && !isPVPEnabled())
        {
            return;
        }
        List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityWolf.class, AxisAlignedBB.getBoundingBoxFromPool(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
        Iterator iterator = list.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            Entity entity = (Entity)iterator.next();
            EntityWolf entitywolf1 = (EntityWolf)entity;
            if(entitywolf1.func_25030_y() && entitywolf1.getEntityToAttack() == null && username.equals(entitywolf1.getOwner()) && (!flag || !entitywolf1.getIsSitting()))
            {
                entitywolf1.setIsSitting(false);
                entitywolf1.setEntityToAttack(entityliving);
            }
        } while(true);
    }

    protected void damageEntity(int i)
    {
        int j = 25 - inventory.getTotalArmorValue();
        int k = i * j + damageRemainder;
        inventory.damageArmor(i);
        i = k / 25;
        damageRemainder = k % 25;
        super.damageEntity(i);
    }

    public void displayGUIFurnace(TileEntityFurnace tileentityfurnace)
    {
    }

    public void displayGUIDispenser(TileEntityDispenser tileentitydispenser)
    {
    }

    public void displayGUIEditSign(TileEntitySign tileentitysign)
    {
    }

    public void useCurrentItemOnEntity(Entity entity)
    {
        if(entity.interact(this))
        {
            return;
        }
        ItemStack itemstack = getCurrentEquippedItem();
        if(itemstack != null && (entity instanceof EntityLiving))
        {
            itemstack.useItemOnEntity((EntityLiving)entity);
            if(itemstack.stackSize <= 0)
            {
                itemstack.func_577_a(this);
                destroyCurrentEquippedItem();
            }
        }
    }

    public ItemStack getCurrentEquippedItem()
    {
        return inventory.getCurrentItem();
    }

    public void destroyCurrentEquippedItem()
    {
        inventory.setInventorySlotContents(inventory.currentItem, null);
    }

    public double getYOffset()
    {
        return (double)(yOffset - 0.5F);
    }

    public void swingItem()
    {
        swingProgressInt = -1;
        isSwinging = true;
    }

    public void attackTargetEntityWithCurrentItem(Entity entity)
    {
        int i = inventory.getDamageVsEntity(entity);
        if(i > 0)
        {
            if(motionY < 0.0D)
            {
                i++;
            }
            entity.attackEntityFrom(this, i);
            ItemStack itemstack = getCurrentEquippedItem();
            if(itemstack != null && (entity instanceof EntityLiving))
            {
                itemstack.hitEntity((EntityLiving)entity, this);
                if(itemstack.stackSize <= 0)
                {
                    itemstack.func_577_a(this);
                    destroyCurrentEquippedItem();
                }
            }
            if(entity instanceof EntityLiving)
            {
                if(entity.isEntityAlive())
                {
                    func_25047_a((EntityLiving)entity, true);
                }
                addStat(StatList.field_25102_s, i);
            }
        }
    }

    public void onItemStackChanged(ItemStack itemstack)
    {
    }

    public void setEntityDead()
    {
        super.setEntityDead();
        personalCraftingInventory.onCraftGuiClosed(this);
        if(currentCraftingInventory != null)
        {
            currentCraftingInventory.onCraftGuiClosed(this);
        }
    }

    public boolean isEntityInsideOpaqueBlock()
    {
        return !sleeping && super.isEntityInsideOpaqueBlock();
    }

    public EnumStatus goToSleep(int i, int j, int k)
    {
        if(!worldObj.singleplayerWorld)
        {
            if(func_22057_E() || !isEntityAlive())
            {
                return EnumStatus.OTHER_PROBLEM;
            }
            if(worldObj.worldProvider.field_6167_c)
            {
                return EnumStatus.NOT_POSSIBLE_HERE;
            }
            if(worldObj.isDaytime())
            {
                return EnumStatus.NOT_POSSIBLE_NOW;
            }
            if(Math.abs(posX - (double)i) > 3D || Math.abs(posY - (double)j) > 2D || Math.abs(posZ - (double)k) > 3D)
            {
                return EnumStatus.TOO_FAR_AWAY;
            }
        }
        setSize(0.2F, 0.2F);
        yOffset = 0.2F;
        if(worldObj.blockExists(i, j, k))
        {
            int l = worldObj.getBlockMetadata(i, j, k);
            int i1 = BlockBed.func_22019_c(l);
            float f = 0.5F;
            float f1 = 0.5F;
            switch(i1)
            {
            case 0: // '\0'
                f1 = 0.9F;
                break;

            case 2: // '\002'
                f1 = 0.1F;
                break;

            case 1: // '\001'
                f = 0.1F;
                break;

            case 3: // '\003'
                f = 0.9F;
                break;
            }
            func_22059_e(i1);
            setPosition((float)i + f, (float)j + 0.9375F, (float)k + f1);
        } else
        {
            setPosition((float)i + 0.5F, (float)j + 0.9375F, (float)k + 0.5F);
        }
        sleeping = true;
        sleepTimer = 0;
        playerLocation = new ChunkCoordinates(i, j, k);
        motionX = motionZ = motionY = 0.0D;
        if(!worldObj.singleplayerWorld)
        {
            worldObj.updateAllPlayersSleepingFlag();
        }
        return EnumStatus.OK;
    }

    private void func_22059_e(int i)
    {
        field_22066_z = 0.0F;
        field_22067_A = 0.0F;
        switch(i)
        {
        case 0: // '\0'
            field_22067_A = -1.8F;
            break;

        case 2: // '\002'
            field_22067_A = 1.8F;
            break;

        case 1: // '\001'
            field_22066_z = 1.8F;
            break;

        case 3: // '\003'
            field_22066_z = -1.8F;
            break;
        }
    }

    public void wakeUpPlayer(boolean flag, boolean flag1, boolean flag2)
    {
        setSize(0.6F, 1.8F);
        resetHeight();
        ChunkCoordinates chunkcoordinates = playerLocation;
        ChunkCoordinates chunkcoordinates1 = playerLocation;
        if(chunkcoordinates != null && worldObj.getBlockId(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ) == Block.bed.blockID)
        {
            BlockBed.func_22022_a(worldObj, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, false);
            ChunkCoordinates chunkcoordinates2 = BlockBed.func_22021_g(worldObj, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, 0);
            if(chunkcoordinates2 == null)
            {
                chunkcoordinates2 = new ChunkCoordinates(chunkcoordinates.posX, chunkcoordinates.posY + 1, chunkcoordinates.posZ);
            }
            setPosition((float)chunkcoordinates2.posX + 0.5F, (float)chunkcoordinates2.posY + yOffset + 0.1F, (float)chunkcoordinates2.posZ + 0.5F);
        }
        sleeping = false;
        if(!worldObj.singleplayerWorld && flag1)
        {
            worldObj.updateAllPlayersSleepingFlag();
        }
        if(flag)
        {
            sleepTimer = 0;
        } else
        {
            sleepTimer = 100;
        }
        if(flag2)
        {
            setSpawnChunk(playerLocation);
        }
    }

    private boolean isInBed()
    {
        return worldObj.getBlockId(playerLocation.posX, playerLocation.posY, playerLocation.posZ) == Block.bed.blockID;
    }

    public static ChunkCoordinates func_25051_a(World world, ChunkCoordinates chunkcoordinates)
    {
        IChunkProvider ichunkprovider = world.getChunkProvider();
        ichunkprovider.loadChunk(chunkcoordinates.posX - 3 >> 4, chunkcoordinates.posZ - 3 >> 4);
        ichunkprovider.loadChunk(chunkcoordinates.posX + 3 >> 4, chunkcoordinates.posZ - 3 >> 4);
        ichunkprovider.loadChunk(chunkcoordinates.posX - 3 >> 4, chunkcoordinates.posZ + 3 >> 4);
        ichunkprovider.loadChunk(chunkcoordinates.posX + 3 >> 4, chunkcoordinates.posZ + 3 >> 4);
        if(world.getBlockId(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ) != Block.bed.blockID)
        {
            return null;
        } else
        {
            ChunkCoordinates chunkcoordinates1 = BlockBed.func_22021_g(world, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, 0);
            return chunkcoordinates1;
        }
    }

    public boolean func_22057_E()
    {
        return sleeping;
    }

    public boolean isPlayerFullyAsleep()
    {
        return sleeping && sleepTimer >= 100;
    }

    public void func_22061_a(String s)
    {
    }

    public ChunkCoordinates getSpawnChunk()
    {
        return spawnChunk;
    }

    public void setSpawnChunk(ChunkCoordinates chunkcoordinates)
    {
        if(chunkcoordinates != null)
        {
            spawnChunk = new ChunkCoordinates(chunkcoordinates);
        } else
        {
            spawnChunk = null;
        }
    }

    public void func_27017_a(StatBase statbase)
    {
        addStat(statbase, 1);
    }

    public void addStat(StatBase statbase, int i)
    {
    }

    protected void jump()
    {
        super.jump();
        addStat(StatList.field_25106_q, 1);
    }

    public void moveEntityWithHeading(float f, float f1)
    {
        double d = posX;
        double d1 = posY;
        double d2 = posZ;
        super.moveEntityWithHeading(f, f1);
        func_25045_g(posX - d, posY - d1, posZ - d2);
    }

    private void func_25045_g(double d, double d1, double d2)
    {
        if(ridingEntity != null)
        {
            return;
        }
        if(isInsideOfMaterial(Material.water))
        {
            int i = Math.round(MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2) * 100F);
            if(i > 0)
            {
                addStat(StatList.field_25108_p, i);
            }
        } else
        if(isInWater())
        {
            int j = Math.round(MathHelper.sqrt_double(d * d + d2 * d2) * 100F);
            if(j > 0)
            {
                addStat(StatList.field_25112_l, j);
            }
        } else
        if(isOnLadder())
        {
            if(d1 > 0.0D)
            {
                addStat(StatList.field_25110_n, (int)Math.round(d1 * 100D));
            }
        } else
        if(onGround)
        {
            int k = Math.round(MathHelper.sqrt_double(d * d + d2 * d2) * 100F);
            if(k > 0)
            {
                addStat(StatList.field_25113_k, k);
            }
        } else
        {
            int l = Math.round(MathHelper.sqrt_double(d * d + d2 * d2) * 100F);
            if(l > 25)
            {
                addStat(StatList.field_25109_o, l);
            }
        }
    }

    private void func_27015_h(double d, double d1, double d2)
    {
        if(ridingEntity != null)
        {
            int i = Math.round(MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2) * 100F);
            if(i > 0)
            {
                if(ridingEntity instanceof EntityMinecart)
                {
                    addStat(StatList.field_27095_r, i);
                    if(field_27995_d == null)
                    {
                        field_27995_d = new ChunkCoordinates(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
                    } else
                    if(field_27995_d.getSqDistanceTo(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) >= 1000D)
                    {
                        addStat(AchievementList.field_27102_q, 1);
                    }
                } else
                if(ridingEntity instanceof EntityBoat)
                {
                    addStat(StatList.field_27094_s, i);
                } else
                if(ridingEntity instanceof EntityPig)
                {
                    addStat(StatList.field_27093_t, i);
                }
            }
        }
    }

    protected void fall(float f)
    {
        if(f >= 2.0F)
        {
            addStat(StatList.field_25111_m, (int)Math.round((double)f * 100D));
        }
        super.fall(f);
    }

    public void func_27010_a(EntityLiving entityliving)
    {
        if(entityliving instanceof EntityMob)
        {
            func_27017_a(AchievementList.field_27100_s);
        }
    }

    public void setInPortal()
    {
        if(timeUntilPortal > 0)
        {
            timeUntilPortal = 10;
            return;
        } else
        {
            inPortal = true;
            return;
        }
    }

    public InventoryPlayer inventory;
    public Container personalCraftingInventory;
    public Container currentCraftingInventory;
    public byte field_9152_am;
    public int score;
    public float field_9150_ao;
    public float field_9149_ap;
    public boolean isSwinging;
    public int swingProgressInt;
    public String username;
    public int dimension;
    public double field_20047_ay;
    public double field_20046_az;
    public double field_20051_aA;
    public double field_20050_aB;
    public double field_20049_aC;
    public double field_20048_aD;
    protected boolean sleeping;
    public ChunkCoordinates playerLocation;
    private int sleepTimer;
    public float field_22066_z;
    public float field_22067_A;
    private ChunkCoordinates spawnChunk;
    private ChunkCoordinates field_27995_d;
    public int timeUntilPortal;
    protected boolean inPortal;
    public float timeInPortal;
    private int damageRemainder;
    public EntityFish fishEntity;
}
