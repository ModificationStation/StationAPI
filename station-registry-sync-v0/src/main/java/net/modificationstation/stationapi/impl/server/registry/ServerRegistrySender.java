package net.modificationstation.stationapi.impl.server.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.NBTIO;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.LevelSerialRegistry;
import net.modificationstation.stationapi.api.server.event.network.PlayerLoginEvent;

import java.io.*;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ServerRegistrySender {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void sendLevelRegistry(PlayerLoginEvent event) {
        if (((ModdedPacketHandler) event.player.packetHandler).isModded()) {
            LOGGER.info("Sending level registries to \"" + event.player.name + "\"...");
            CompoundTag registries = new CompoundTag();
            LevelSerialRegistry.saveAll(registries);
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            NBTIO.writeGzipped(registries, byteOutputStream);
            Message message = new Message(of(MODID, "server_registry_sync"));
            message.bytes = byteOutputStream.toByteArray();
            PacketHelper.sendTo(event.player, message);
        }
    }
}
