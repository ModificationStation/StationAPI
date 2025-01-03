package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.class_454;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.DataTrackerEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.entity.factory.EntityWorldAndPosFactory;
import net.modificationstation.stationapi.api.client.registry.EntityHandlerRegistry;
import net.modificationstation.stationapi.api.client.registry.MobHandlerRegistry;
import net.modificationstation.stationapi.api.entity.HasOwner;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.MobHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.server.entity.StationSpawnDataProvider;
import net.modificationstation.stationapi.mixin.entity.client.ClientNetworkHandlerAccessor;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class EntityClientNetworkHandler {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void registerMessageListeners(MessageListenerRegistryEvent event) {
        Registry.register(event.registry, NAMESPACE.id("spawn_entity"), EntityClientNetworkHandler::handleEntitySpawn);
        StationAPI.EVENT_BUS.post(new EntityHandlerRegistryEvent());
        Registry.register(event.registry, NAMESPACE.id("spawn_mob"), EntityClientNetworkHandler::handleMobSpawn);
        StationAPI.EVENT_BUS.post(new MobHandlerRegistryEvent());
    }

    private static void handleEntitySpawn(PlayerEntity player, MessagePacket message) {
        EntityWorldAndPosFactory entityHandler = EntityHandlerRegistry.INSTANCE.get(of(message.strings[0]));
        if (entityHandler != null) {
            double
                    x = message.ints[1] / 32D,
                    y = message.ints[2] / 32D,
                    z = message.ints[3] / 32D;
            //noinspection deprecation
            ClientNetworkHandlerAccessor networkHandler = (ClientNetworkHandlerAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).getNetworkHandler();
            class_454 world = networkHandler.getField_1973();
            Entity entity = entityHandler.create(world, x, y, z);
            if (entity != null) {
                entity.field_1654 = message.ints[1];
                entity.field_1655 = message.ints[2];
                entity.field_1656 = message.ints[3];
                entity.yaw = 0.0F;
                entity.pitch = 0.0F;
                entity.id = message.ints[0];
                world.method_1495(message.ints[0], entity);
                if (message.ints[4] > 0) {
                    if (entity instanceof HasOwner hasOwner)
                        hasOwner.setOwner(networkHandler.invokeMethod_1645(message.ints[4]));
                    entity.method_1365((double) message.shorts[0] / 8000.0D, (double) message.shorts[1] / 8000.0D, (double) message.shorts[2] / 8000.0D);
                }
                if (message.bytes != null)
                    entity.method_1331().writeUpdatedEntries(DataTracker.readEntries(new DataInputStream(new ByteArrayInputStream(message.bytes))));
                if (entity instanceof StationSpawnDataProvider provider)
                    provider.readFromMessage(message);
            }
        }
    }

    private static void handleMobSpawn(PlayerEntity player, MessagePacket message) {
        Function<World, LivingEntity> mobHandler = MobHandlerRegistry.INSTANCE.get(of(message.strings[0]));
        if (mobHandler != null) {
            double
                    x = message.ints[1] / 32D,
                    y = message.ints[2] / 32D,
                    z = message.ints[3] / 32D;
            float yaw = (float)(message.bytes[0] * 360) / 256.0F;
            float pitch = (float)(message.bytes[1] * 360) / 256.0F;
            //noinspection deprecation
            ClientNetworkHandlerAccessor networkHandler = (ClientNetworkHandlerAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).getNetworkHandler();
            class_454 world = networkHandler.getField_1973();
            LivingEntity mob = mobHandler.apply(world);
            if (mob != null) {
                mob.field_1654 = message.ints[1];
                mob.field_1655 = message.ints[2];
                mob.field_1656 = message.ints[3];
                mob.id = message.ints[0];
                mob.method_1338(x, y, z, yaw, pitch);
                mob.field_1026 = true;
                world.method_1495(message.ints[0], mob);
                //noinspection unchecked
                List<DataTrackerEntry> data = DataTracker.readEntries(new DataInputStream(new ByteArrayInputStream(Arrays.copyOfRange(message.bytes, 2, message.bytes.length))));
                if (data != null)
                    mob.method_1331().writeUpdatedEntries(data);
                if (mob instanceof StationSpawnDataProvider provider)
                    provider.readFromMessage(message);
            }
        }
    }
}
