package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_454;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;
import net.modificationstation.stationapi.impl.network.StationItemsNetworkHandler;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationItemEntitySpawnS2CPacket;

public class StationItemsClientNetworkHandler extends StationItemsNetworkHandler {
    @Override
    public boolean isServerSide() {
        return false;
    }

    @Override
    public void onItemEntitySpawn(StationItemEntitySpawnS2CPacket packet) {
        //noinspection deprecation
        class_454 clientWorld = (class_454) ((Minecraft) FabricLoader.getInstance().getGameInstance()).world;
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
        entity.field_1654 = packet.x;
        entity.field_1655 = packet.y;
        entity.field_1656 = packet.z;
        clientWorld.method_1495(packet.id, entity);
    }
}
