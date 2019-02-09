// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;
import net.minecraft.server.MinecraftServer;

// Referenced classes of package net.minecraft.src:
//            EntityPlayer, ICrafting, ItemStack, ItemInWorldManager, 
//            World, ChunkCoordinates, WorldProvider, WorldServer, 
//            Container, Packet5PlayerInventory, EntityTracker, InventoryPlayer, 
//            EntityArrow, Item, NetServerHandler, ItemMapBase, 
//            ChunkCoordIntPair, Packet51MapChunk, TileEntity, PropertyManager, 
//            ServerConfigurationManager, Packet8UpdateHealth, Entity, EntityItem, 
//            Packet22Collect, Packet18Animation, EnumStatus, Packet17Sleep, 
//            Packet39AttachEntity, Packet100OpenWindow, ContainerWorkbench, IInventory, 
//            ContainerChest, TileEntityFurnace, ContainerFurnace, TileEntityDispenser, 
//            ContainerDispenser, SlotCrafting, Packet103SetSlot, Packet104WindowItems, 
//            Packet105UpdateProgressbar, Packet101CloseWindow, StatBase, Packet200Statistic, 
//            StringTranslate, Packet3Chat

public class EntityPlayerMP extends EntityPlayer
    implements ICrafting
{

    public EntityPlayerMP(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager)
    {
        super(world);
        loadedChunks = new LinkedList();
        field_420_ah = new HashSet();
        lastHealth = 0xfa0a1f01;
        ticksOfInvuln = 60;
        currentWindowId = 0;
        iteminworldmanager.thisPlayer = this;
        itemInWorldManager = iteminworldmanager;
        ChunkCoordinates chunkcoordinates = world.getSpawnPoint();
        int i = chunkcoordinates.posX;
        int j = chunkcoordinates.posZ;
        int k = chunkcoordinates.posY;
        if(!world.worldProvider.field_4306_c)
        {
            i += rand.nextInt(20) - 10;
            k = world.findTopSolidBlock(i, j);
            j += rand.nextInt(20) - 10;
        }
        setLocationAndAngles((double)i + 0.5D, k, (double)j + 0.5D, 0.0F, 0.0F);
        mcServer = minecraftserver;
        stepHeight = 0.0F;
        username = s;
        yOffset = 0.0F;
    }

    public void setWorldHandler(World world)
    {
        super.setWorldHandler(world);
        itemInWorldManager = new ItemInWorldManager((WorldServer)world);
        itemInWorldManager.thisPlayer = this;
    }

    public void func_20057_k()
    {
        currentCraftingInventory.onCraftGuiOpened(this);
    }

    public ItemStack[] getInventory()
    {
        return playerInventory;
    }

    protected void resetHeight()
    {
        yOffset = 0.0F;
    }

    public float getEyeHeight()
    {
        return 1.62F;
    }

    public void onUpdate()
    {
        itemInWorldManager.func_328_a();
        ticksOfInvuln--;
        currentCraftingInventory.updateCraftingMatrix();
        for(int i = 0; i < 5; i++)
        {
            ItemStack itemstack = getEquipmentInSlot(i);
            if(itemstack != playerInventory[i])
            {
                mcServer.getEntityTracker(dimension).sendPacketToTrackedPlayers(this, new Packet5PlayerInventory(entityId, i, itemstack));
                playerInventory[i] = itemstack;
            }
        }

    }

    public ItemStack getEquipmentInSlot(int i)
    {
        if(i == 0)
        {
            return inventory.getCurrentItem();
        } else
        {
            return inventory.armorInventory[i - 1];
        }
    }

    public void onDeath(Entity entity)
    {
        inventory.dropAllItems();
    }

    public boolean attackEntityFrom(Entity entity, int i)
    {
        if(ticksOfInvuln > 0)
        {
            return false;
        }
        if(!mcServer.pvpOn)
        {
            if(entity instanceof EntityPlayer)
            {
                return false;
            }
            if(entity instanceof EntityArrow)
            {
                EntityArrow entityarrow = (EntityArrow)entity;
                if(entityarrow.owner instanceof EntityPlayer)
                {
                    return false;
                }
            }
        }
        return super.attackEntityFrom(entity, i);
    }

    protected boolean isPVPEnabled()
    {
        return mcServer.pvpOn;
    }

    public void heal(int i)
    {
        super.heal(i);
    }

    public void onUpdateEntity(boolean flag)
    {
        super.onUpdate();
        for(int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if(itemstack == null || !Item.itemsList[itemstack.itemID].func_28019_b() || playerNetServerHandler.getNumChunkDataPackets() > 2)
            {
                continue;
            }
            Packet packet = ((ItemMapBase)Item.itemsList[itemstack.itemID]).func_28022_b(itemstack, worldObj, this);
            if(packet != null)
            {
                playerNetServerHandler.sendPacket(packet);
            }
        }

        if(flag && !loadedChunks.isEmpty())
        {
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)loadedChunks.get(0);
            if(chunkcoordintpair != null)
            {
                boolean flag1 = false;
                if(playerNetServerHandler.getNumChunkDataPackets() < 4)
                {
                    flag1 = true;
                }
                if(flag1)
                {
                    WorldServer worldserver = mcServer.getWorldManager(dimension);
                    loadedChunks.remove(chunkcoordintpair);
                    playerNetServerHandler.sendPacket(new Packet51MapChunk(chunkcoordintpair.chunkXPos * 16, 0, chunkcoordintpair.chunkZPos * 16, 16, 128, 16, worldserver));
                    List list = worldserver.getTileEntityList(chunkcoordintpair.chunkXPos * 16, 0, chunkcoordintpair.chunkZPos * 16, chunkcoordintpair.chunkXPos * 16 + 16, 128, chunkcoordintpair.chunkZPos * 16 + 16);
                    for(int j = 0; j < list.size(); j++)
                    {
                        getTileEntityInfo((TileEntity)list.get(j));
                    }

                }
            }
        }
        if(inPortal)
        {
            if(mcServer.propertyManagerObj.getBooleanProperty("allow-nether", true))
            {
                if(currentCraftingInventory != personalCraftingInventory)
                {
                    usePersonalCraftingInventory();
                }
                if(ridingEntity != null)
                {
                    mountEntity(ridingEntity);
                } else
                {
                    timeInPortal += 0.0125F;
                    if(timeInPortal >= 1.0F)
                    {
                        timeInPortal = 1.0F;
                        timeUntilPortal = 10;
                        mcServer.configManager.sendPlayerToOtherDimension(this);
                    }
                }
                inPortal = false;
            }
        } else
        {
            if(timeInPortal > 0.0F)
            {
                timeInPortal -= 0.05F;
            }
            if(timeInPortal < 0.0F)
            {
                timeInPortal = 0.0F;
            }
        }
        if(timeUntilPortal > 0)
        {
            timeUntilPortal--;
        }
        if(health != lastHealth)
        {
            playerNetServerHandler.sendPacket(new Packet8UpdateHealth(health));
            lastHealth = health;
        }
    }

    private void getTileEntityInfo(TileEntity tileentity)
    {
        if(tileentity != null)
        {
            Packet packet = tileentity.getDescriptionPacket();
            if(packet != null)
            {
                playerNetServerHandler.sendPacket(packet);
            }
        }
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
    }

    public void onItemPickup(Entity entity, int i)
    {
        if(!entity.isDead)
        {
            EntityTracker entitytracker = mcServer.getEntityTracker(dimension);
            if(entity instanceof EntityItem)
            {
                entitytracker.sendPacketToTrackedPlayers(entity, new Packet22Collect(entity.entityId, entityId));
            }
            if(entity instanceof EntityArrow)
            {
                entitytracker.sendPacketToTrackedPlayers(entity, new Packet22Collect(entity.entityId, entityId));
            }
        }
        super.onItemPickup(entity, i);
        currentCraftingInventory.updateCraftingMatrix();
    }

    public void swingItem()
    {
        if(!isSwinging)
        {
            swingProgressInt = -1;
            isSwinging = true;
            EntityTracker entitytracker = mcServer.getEntityTracker(dimension);
            entitytracker.sendPacketToTrackedPlayers(this, new Packet18Animation(this, 1));
        }
    }

    public void func_22068_s()
    {
    }

    public EnumStatus goToSleep(int i, int j, int k)
    {
        EnumStatus enumstatus = super.goToSleep(i, j, k);
        if(enumstatus == EnumStatus.OK)
        {
            EntityTracker entitytracker = mcServer.getEntityTracker(dimension);
            Packet17Sleep packet17sleep = new Packet17Sleep(this, 0, i, j, k);
            entitytracker.sendPacketToTrackedPlayers(this, packet17sleep);
            playerNetServerHandler.teleportTo(posX, posY, posZ, rotationYaw, rotationPitch);
            playerNetServerHandler.sendPacket(packet17sleep);
        }
        return enumstatus;
    }

    public void wakeUpPlayer(boolean flag, boolean flag1, boolean flag2)
    {
        if(func_22057_E())
        {
            EntityTracker entitytracker = mcServer.getEntityTracker(dimension);
            entitytracker.sendPacketToTrackedPlayersAndTrackedEntity(this, new Packet18Animation(this, 3));
        }
        super.wakeUpPlayer(flag, flag1, flag2);
        if(playerNetServerHandler != null)
        {
            playerNetServerHandler.teleportTo(posX, posY, posZ, rotationYaw, rotationPitch);
        }
    }

    public void mountEntity(Entity entity)
    {
        super.mountEntity(entity);
        playerNetServerHandler.sendPacket(new Packet39AttachEntity(this, ridingEntity));
        playerNetServerHandler.teleportTo(posX, posY, posZ, rotationYaw, rotationPitch);
    }

    protected void updateFallState(double d, boolean flag)
    {
    }

    public void handleFalling(double d, boolean flag)
    {
        super.updateFallState(d, flag);
    }

    private void getNextWidowId()
    {
        currentWindowId = currentWindowId % 100 + 1;
    }

    public void displayWorkbenchGUI(int i, int j, int k)
    {
        getNextWidowId();
        playerNetServerHandler.sendPacket(new Packet100OpenWindow(currentWindowId, 1, "Crafting", 9));
        currentCraftingInventory = new ContainerWorkbench(inventory, worldObj, i, j, k);
        currentCraftingInventory.windowId = currentWindowId;
        currentCraftingInventory.onCraftGuiOpened(this);
    }

    public void displayGUIChest(IInventory iinventory)
    {
        getNextWidowId();
        playerNetServerHandler.sendPacket(new Packet100OpenWindow(currentWindowId, 0, iinventory.getInvName(), iinventory.getSizeInventory()));
        currentCraftingInventory = new ContainerChest(inventory, iinventory);
        currentCraftingInventory.windowId = currentWindowId;
        currentCraftingInventory.onCraftGuiOpened(this);
    }

    public void displayGUIFurnace(TileEntityFurnace tileentityfurnace)
    {
        getNextWidowId();
        playerNetServerHandler.sendPacket(new Packet100OpenWindow(currentWindowId, 2, tileentityfurnace.getInvName(), tileentityfurnace.getSizeInventory()));
        currentCraftingInventory = new ContainerFurnace(inventory, tileentityfurnace);
        currentCraftingInventory.windowId = currentWindowId;
        currentCraftingInventory.onCraftGuiOpened(this);
    }

    public void displayGUIDispenser(TileEntityDispenser tileentitydispenser)
    {
        getNextWidowId();
        playerNetServerHandler.sendPacket(new Packet100OpenWindow(currentWindowId, 3, tileentitydispenser.getInvName(), tileentitydispenser.getSizeInventory()));
        currentCraftingInventory = new ContainerDispenser(inventory, tileentitydispenser);
        currentCraftingInventory.windowId = currentWindowId;
        currentCraftingInventory.onCraftGuiOpened(this);
    }

    public void updateCraftingInventorySlot(Container container, int i, ItemStack itemstack)
    {
        if(container.getSlot(i) instanceof SlotCrafting)
        {
            return;
        }
        if(isChangingQuantityOnly)
        {
            return;
        } else
        {
            playerNetServerHandler.sendPacket(new Packet103SetSlot(container.windowId, i, itemstack));
            return;
        }
    }

    public void func_28017_a(Container container)
    {
        updateCraftingInventory(container, container.func_28127_b());
    }

    public void updateCraftingInventory(Container container, List list)
    {
        playerNetServerHandler.sendPacket(new Packet104WindowItems(container.windowId, list));
        playerNetServerHandler.sendPacket(new Packet103SetSlot(-1, -1, inventory.getItemStack()));
    }

    public void updateCraftingInventoryInfo(Container container, int i, int j)
    {
        playerNetServerHandler.sendPacket(new Packet105UpdateProgressbar(container.windowId, i, j));
    }

    public void onItemStackChanged(ItemStack itemstack)
    {
    }

    public void usePersonalCraftingInventory()
    {
        playerNetServerHandler.sendPacket(new Packet101CloseWindow(currentCraftingInventory.windowId));
        closeCraftingGui();
    }

    public void updateHeldItem()
    {
        if(isChangingQuantityOnly)
        {
            return;
        } else
        {
            playerNetServerHandler.sendPacket(new Packet103SetSlot(-1, -1, inventory.getItemStack()));
            return;
        }
    }

    public void closeCraftingGui()
    {
        currentCraftingInventory.onCraftGuiClosed(this);
        currentCraftingInventory = personalCraftingInventory;
    }

    public void setMovementType(float f, float f1, boolean flag, boolean flag1, float f2, float f3)
    {
        moveStrafing = f;
        moveForward = f1;
        isJumping = flag;
        setSneaking(flag1);
        rotationPitch = f2;
        rotationYaw = f3;
    }

    public void addStat(StatBase statbase, int i)
    {
        if(statbase == null)
        {
            return;
        }
        if(!statbase.field_27058_g)
        {
            for(; i > 100; i -= 100)
            {
                playerNetServerHandler.sendPacket(new Packet200Statistic(statbase.statId, 100));
            }

            playerNetServerHandler.sendPacket(new Packet200Statistic(statbase.statId, i));
        }
    }

    public void func_30002_A()
    {
        if(ridingEntity != null)
        {
            mountEntity(ridingEntity);
        }
        if(riddenByEntity != null)
        {
            riddenByEntity.mountEntity(this);
        }
        if(sleeping)
        {
            wakeUpPlayer(true, false, false);
        }
    }

    public void func_30001_B()
    {
        lastHealth = 0xfa0a1f01;
    }

    public void func_22061_a(String s)
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        String s1 = stringtranslate.translateKey(s);
        playerNetServerHandler.sendPacket(new Packet3Chat(s1));
    }

    public NetServerHandler playerNetServerHandler;
    public MinecraftServer mcServer;
    public ItemInWorldManager itemInWorldManager;
    public double field_9155_d;
    public double field_9154_e;
    public List loadedChunks;
    public Set field_420_ah;
    private int lastHealth;
    private int ticksOfInvuln;
    private ItemStack playerInventory[] = {
        null, null, null, null, null
    };
    private int currentWindowId;
    public boolean isChangingQuantityOnly;
}
