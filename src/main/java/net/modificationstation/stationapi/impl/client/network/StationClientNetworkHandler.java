package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.entity.EntityHandlerRegistry;
import net.modificationstation.stationapi.api.common.entity.HasOwner;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.common.gui.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.common.packet.Message;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.server.entity.IStationSpawnData;
import net.modificationstation.stationapi.mixin.client.accessor.ClientPlayNetworkHandlerAccessor;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class StationClientNetworkHandler {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerMessageListeners(RegistryEvent.MessageListeners event) {
        event.registry.registerValue(Identifier.of(StationAPI.MODID, "open_gui"), StationClientNetworkHandler::handleGui);
        StationAPI.EVENT_BUS.post(new RegistryEvent.GuiHandlers());
        event.registry.registerValue(Identifier.of(StationAPI.MODID, "spawn_entity"), StationClientNetworkHandler::handleEntity);
        StationAPI.EVENT_BUS.post(new RegistryEvent.EntityHandlers());
    }

    private static void handleGui(PlayerBase player, Message message) {
        boolean isClient = player.level.isClient;
        //noinspection deprecation
        GuiHandlerRegistry.INSTANCE.getByIdentifier(Identifier.of(message.strings()[0])).ifPresent(guiHandler -> ((Minecraft) FabricLoader.getInstance().getGameInstance()).openScreen(guiHandler.one().apply(player, isClient ? guiHandler.two().get() : (InventoryBase) message.objects()[0], message)));
        if (isClient)
            player.container.currentContainerId = message.ints()[0];
    }

    private static void handleEntity(PlayerBase player, Message message) {
        EntityHandlerRegistry.INSTANCE.getByIdentifier(Identifier.of(message.strings()[0])).ifPresent(entityProvider -> {
            double x = message.ints()[1] / 32D, y = message.ints()[2] / 32D, z = message.ints()[3] / 32D;
            //noinspection deprecation
            ClientPlayNetworkHandlerAccessor networkHandler = (ClientPlayNetworkHandlerAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).getNetworkHandler();
            ClientLevel level = networkHandler.getLevel();
            EntityBase entity = entityProvider.apply(level, x, y, z);
            if (entity != null) {
                entity.clientX = message.ints()[1];
                entity.clientY = message.ints()[2];
                entity.clientZ = message.ints()[3];
                entity.yaw = 0.0F;
                entity.pitch = 0.0F;
                entity.entityId = message.ints()[0];
                level.method_1495(message.ints()[0], entity);
                if (message.ints()[4] > 0) {
                    if (entity instanceof HasOwner)
                        ((HasOwner) entity).setOwner(networkHandler.invokeMethod_1645(message.ints()[4]));
                    entity.setVelocity((double) message.shorts()[0] / 8000.0D, (double) message.shorts()[1] / 8000.0D, (double) message.shorts()[2] / 8000.0D);
                }
                if (entity instanceof IStationSpawnData)
                    ((IStationSpawnData) entity).readFromMessage(message);
            }
        });
    }
}
