package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.network.ServerLoginSuccessEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.impl.network.ModdedPacketHandlerSetter;

import java.lang.invoke.MethodHandles;
import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class ClientVanillaChecker {
    static {
        Listener.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void handleServerLogin(ServerLoginSuccessEvent event) {
        List<String> splitName = Arrays.asList(event.loginHelloPacket.username.split(";"));
        if (splitName.contains(NAMESPACE.toString())) {
            MessagePacket message = new MessagePacket(of(NAMESPACE, "modlist"));
            List<String> mods = new ArrayList<>();
            mods.add(NAMESPACE.getVersion().getFriendlyString());
            FabricLoader.getInstance().getAllMods().stream().map(ModContainer::getMetadata).forEach(modMetadata -> Collections.addAll(mods, modMetadata.getId(), modMetadata.getVersion().getFriendlyString()));
            message.strings = mods.toArray(new String[0]);
            event.clientNetworkHandler.sendPacket(message);

            // This definitely doesn't have a modlist entry.
            if(splitName.size() <= splitName.indexOf(NAMESPACE.toString()))
                return;

            String modListString = splitName.get(splitName.indexOf(NAMESPACE.toString()) + 1); // Mod list string should ALWAYS follow stapi's string.

            // If this doesn't contain stationapi, this isn't a stationapi mod list.
            if(!modListString.contains("stationapi="))
                return;

            Map<String, String> modList = new HashMap<>();
            Arrays.stream(modListString.split(":")).forEach(nameVersion -> {
                String[] nameVersionArr = nameVersion.split("=");
                modList.put(nameVersionArr[0], nameVersionArr[1]);
            });
            ((ModdedPacketHandlerSetter) event.clientNetworkHandler).setModded(modList);
        }
    }
}
