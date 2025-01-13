package net.modificationstation.stationapi.impl.server.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.legacy.WorldLegacyRegistry;
import net.modificationstation.stationapi.api.server.event.network.PlayerPacketHandlerSetEvent;

import java.io.ByteArrayOutputStream;
import java.lang.invoke.MethodHandles;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class ServerRegistrySender {
    static {
        Listener.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void sendWorldRegistry(PlayerPacketHandlerSetEvent event) {
        if (((ModdedPacketHandler) event.player.field_255).isModded()) {
            LOGGER.info("Sending level registries to \"" + event.player.name + "\"...");
            NbtCompound registries = new NbtCompound();
            WorldLegacyRegistry.saveAll(registries);
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            NbtIo.writeCompressed(registries, byteOutputStream);
            MessagePacket message = new MessagePacket(of(NAMESPACE, "server_registry_sync"));
            message.bytes = byteOutputStream.toByteArray();
            PacketHelper.sendTo(event.player, message);
        }
    }
}
