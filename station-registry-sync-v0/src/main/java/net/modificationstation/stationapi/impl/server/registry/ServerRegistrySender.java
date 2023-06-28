package net.modificationstation.stationapi.impl.server.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.NBTIO;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.legacy.LevelLegacyRegistry;
import net.modificationstation.stationapi.api.server.event.network.PlayerPacketHandlerSetEvent;

import java.io.ByteArrayOutputStream;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class ServerRegistrySender {

    @EventListener
    private static void sendLevelRegistry(PlayerPacketHandlerSetEvent event) {
        if (((ModdedPacketHandler) event.player.packetHandler).isModded()) {
            LOGGER.info("Sending level registries to \"" + event.player.name + "\"...");
            CompoundTag registries = new CompoundTag();
            LevelLegacyRegistry.saveAll(registries);
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            NBTIO.writeGzipped(registries, byteOutputStream);
            Message message = new Message(of(MODID, "server_registry_sync"));
            message.bytes = byteOutputStream.toByteArray();
            PacketHelper.sendTo(event.player, message);
        }
    }
}
