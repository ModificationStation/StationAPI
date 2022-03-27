package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.client.event.network.ServerLoginSuccessEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.impl.network.ModdedPacketHandlerSetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ClientVanillaChecker {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void handleServerLogin(ServerLoginSuccessEvent event) {
        if (Arrays.asList(event.loginRequestPacket.username.split(";")).contains(MODID.toString())) {
            ((ModdedPacketHandlerSetter) event.networkHandler).setModded();
            Message message = new Message(of(MODID, "modlist"));
            List<String> mods = new ArrayList<>();
            mods.add(MODID.getVersion().getFriendlyString());
            FabricLoader.getInstance().getAllMods().stream().map(ModContainer::getMetadata).forEach(modMetadata -> Collections.addAll(mods, modMetadata.getId(), modMetadata.getVersion().getFriendlyString()));
            message.strings = mods.toArray(new String[0]);
            event.networkHandler.sendPacket(message);
        }
    }
}
