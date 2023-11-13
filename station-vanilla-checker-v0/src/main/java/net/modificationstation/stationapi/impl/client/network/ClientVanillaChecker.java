package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.network.ServerLoginSuccessEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.impl.network.ModdedPacketHandlerSetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class ClientVanillaChecker {

    @EventListener
    private static void handleServerLogin(ServerLoginSuccessEvent event) {
        if (Arrays.asList(event.loginHelloPacket.username.split(";")).contains(NAMESPACE.toString())) {
            ((ModdedPacketHandlerSetter) event.clientNetworkHandler).setModded();
            MessagePacket message = new MessagePacket(of(NAMESPACE, "modlist"));
            List<String> mods = new ArrayList<>();
            mods.add(NAMESPACE.getVersion().getFriendlyString());
            FabricLoader.getInstance().getAllMods().stream().map(ModContainer::getMetadata).forEach(modMetadata -> Collections.addAll(mods, modMetadata.getId(), modMetadata.getVersion().getFriendlyString()));
            message.strings = mods.toArray(new String[0]);
            event.clientNetworkHandler.sendPacket(message);
        }
    }
}
