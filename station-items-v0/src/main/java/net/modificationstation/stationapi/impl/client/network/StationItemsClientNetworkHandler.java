package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ClientWorld;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;
import net.modificationstation.stationapi.impl.network.StationItemsNetworkHandler;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationEntityEquipmentUpdateS2CPacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationItemEntitySpawnS2CPacket;
import net.modificationstation.stationapi.mixin.item.client.ClientNetworkHandlerAccessor;

public class StationItemsClientNetworkHandler extends StationItemsNetworkHandler {
    @Override
    public boolean isServerSide() {
        return false;
    }

    @Override
    public void onItemEntitySpawn(StationItemEntitySpawnS2CPacket packet) {
        //noinspection deprecation
        ClientWorld clientWorld = (ClientWorld) ((Minecraft) FabricLoader.getInstance().getGameInstance()).world;
        double x = (double)packet.x / 32.0;
        double y = (double)packet.y / 32.0;
        double z = (double)packet.z / 32.0;
        ItemStack stack = new ItemStack(packet.itemRawId, packet.itemCount, packet.itemDamage);
        if (packet.stationNbt != null)
            StationNBTSetter.cast(stack).setStationNbt(packet.stationNbt);
        ItemEntity entity = new ItemEntity(clientWorld, x, y, z, stack);
        entity.velocityX = (double)packet.velocityX / 128.0;
        entity.velocityY = (double)packet.velocityY / 128.0;
        entity.velocityZ = (double)packet.velocityZ / 128.0;
        entity.trackedPosX = packet.x;
        entity.trackedPosY = packet.y;
        entity.trackedPosZ = packet.z;
        clientWorld.forceEntity(packet.id, entity);
    }

    @Override
    public void onEntityEquipmentUpdate(StationEntityEquipmentUpdateS2CPacket packet) {
        //noinspection deprecation
        Entity entity = ((ClientNetworkHandlerAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).getNetworkHandler()).stationapi_method_1645(packet.id);
        if (entity != null) {
            final ItemStack stack;
            if (packet.itemRawId < 0)
                stack = null;
            else {
                stack = new ItemStack(packet.itemRawId, 1, packet.itemDamage);
                if (packet.stationNbt != null) StationNBTSetter.cast(stack).setStationNbt(packet.stationNbt);
            }
            entity.equipStack(packet.slot, stack);
        }
    }
}
