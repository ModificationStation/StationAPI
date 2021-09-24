package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.class_270;
import net.minecraft.client.Minecraft;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.entity.HasOwner;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.MobHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.registry.EntityHandlerRegistry;
import net.modificationstation.stationapi.api.registry.MobHandlerRegistry;
import net.modificationstation.stationapi.api.server.entity.StationSpawnDataProvider;
import net.modificationstation.stationapi.mixin.entity.client.ClientPlayNetworkHandlerAccessor;

import java.io.*;
import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class EntityClientNetworkHandler {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerMessageListeners(MessageListenerRegistryEvent event) {
        event.registry.register(of(MODID, "spawn_entity"), EntityClientNetworkHandler::handleEntitySpawn);
        StationAPI.EVENT_BUS.post(new EntityHandlerRegistryEvent());
        event.registry.register(of(MODID, "spawn_mob"), EntityClientNetworkHandler::handleMobSpawn);
        StationAPI.EVENT_BUS.post(new MobHandlerRegistryEvent());
    }

    private static void handleEntitySpawn(PlayerBase player, Message message) {
        EntityHandlerRegistry.INSTANCE.get(of(message.strings[0])).ifPresent(entityProvider -> {
            double
                    x = message.ints[1] / 32D,
                    y = message.ints[2] / 32D,
                    z = message.ints[3] / 32D;
            //noinspection deprecation
            ClientPlayNetworkHandlerAccessor networkHandler = (ClientPlayNetworkHandlerAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).getNetworkHandler();
            ClientLevel level = networkHandler.getLevel();
            EntityBase entity = entityProvider.apply(level, x, y, z);
            if (entity != null) {
                entity.clientX = message.ints[1];
                entity.clientY = message.ints[2];
                entity.clientZ = message.ints[3];
                entity.yaw = 0.0F;
                entity.pitch = 0.0F;
                entity.entityId = message.ints[0];
                level.method_1495(message.ints[0], entity);
                if (message.ints[4] > 0) {
                    if (entity instanceof HasOwner)
                        ((HasOwner) entity).setOwner(networkHandler.invokeMethod_1645(message.ints[4]));
                    entity.setVelocity((double) message.shorts[0] / 8000.0D, (double) message.shorts[1] / 8000.0D, (double) message.shorts[2] / 8000.0D);
                }
                if (entity instanceof StationSpawnDataProvider)
                    ((StationSpawnDataProvider) entity).readFromMessage(message);
            }
        });
    }

    private static void handleMobSpawn(PlayerBase player, Message message) {
        MobHandlerRegistry.INSTANCE.get(of(message.strings[0])).ifPresent(levelLivingFunction -> {
            double
                    x = message.ints[1] / 32D,
                    y = message.ints[2] / 32D,
                    z = message.ints[3] / 32D;
            float yaw = (float)(message.bytes[0] * 360) / 256.0F;
            float pitch = (float)(message.bytes[1] * 360) / 256.0F;
            //noinspection deprecation
            ClientPlayNetworkHandlerAccessor networkHandler = (ClientPlayNetworkHandlerAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).getNetworkHandler();
            ClientLevel level = networkHandler.getLevel();
            Living mob = levelLivingFunction.apply(level);
            if (mob != null) {
                mob.clientX = message.ints[1];
                mob.clientY = message.ints[2];
                mob.clientZ = message.ints[3];
                mob.entityId = message.ints[0];
                mob.method_1338(x, y, z, yaw, pitch);
                mob.field_1026 = true;
                level.method_1495(message.ints[0], mob);
                //noinspection unchecked
                List<class_270> data = DataTracker.readTrackedData(new DataInputStream(new ByteArrayInputStream(Arrays.copyOfRange(message.bytes, 2, message.bytes.length))));
                if (data != null)
                    mob.getDataTracker().method_1511(data);
                if (mob instanceof StationSpawnDataProvider)
                    ((StationSpawnDataProvider) mob).readFromMessage(message);
            }
        });
    }
}
