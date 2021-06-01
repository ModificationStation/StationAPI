package net.modificationstation.stationapi.impl.server.network;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.mod.PreInitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.server.event.network.HandshakeSuccessEvent;

import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.MODID;

//import net.minecraft.client.resource.language.TranslationStorage;
//import net.modificationstation.stationapi.api.StationAPI;
//import net.modificationstation.stationapi.api.packet.StationHandshake;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class StationServerNetworkHandler {

    /**
     * A set of mods that need client-side verification when the client joins server.
     */
    private static final Set<ModContainer> modsToVerifyOnClient = new HashSet<>();

    @EventListener(priority = ListenerPriority.HIGH)
    private static void onInit(PreInitEvent event) {
        LOGGER.info("Gathering mods that require client verification...");
        String value = MODID + ":verify_client";
        FabricLoader.getInstance().getAllMods().forEach(modContainer -> {
            ModMetadata modMetadata = modContainer.getMetadata();
            if (modMetadata.containsCustomValue(value) && modMetadata.getCustomValue(value).getAsBoolean())
                modsToVerifyOnClient.add(modContainer);
        });
    }

    // TODO: needs complete refactor.
    @EventListener(priority = ListenerPriority.HIGH)
    private static void handleHandshake(HandshakeSuccessEvent event) {
//        if (!StationAPI.INSTANCE.getModsToVerifyOnClient().isEmpty()) {
//            StationHandshake handshake = (StationHandshake) event.handshakePacket;
//            String stationAPI = handshake.getStationAPI();
//            String version = handshake.getVersion();
//            String serverStationAPI = StationAPI.MODID.toString();
//            String serverStationVersion = StationAPI.MODID.getVersion().getFriendlyString();
//            TranslationStorage translationStorage = TranslationStorage.getInstance();
//            if (stationAPI == null || version == null || !stationAPI.equals(serverStationAPI)) {
//                event.pendingConnection.drop(translationStorage.translate("disconnect.stationapi:missing_station"));
//                return;
//            } else if (!version.equals(serverStationVersion)) {
//                event.pendingConnection.drop(translationStorage.translate("disconnect.stationapi:station_version_mismatch", serverStationVersion, version));
//                return;
//            }
//            Map<String, String> clientMods = handshake.getMods();
//            String modid;
//            String clientVersion;
//            String serverVersion;
//            for (ModContainer serverMod : StationAPI.INSTANCE.getModsToVerifyOnClient()) {
//                ModMetadata modMetadata = serverMod.getMetadata();
//                modid = modMetadata.getId();
//                serverVersion = modMetadata.getVersion().getFriendlyString();
//                if (clientMods.containsKey(modid)) {
//                    clientVersion = clientMods.get(modid);
//                    if (clientVersion == null || !clientVersion.equals(serverVersion)) {
//                        event.pendingConnection.drop(translationStorage.translate("disconnect.stationapi:mod_version_mismatch", modMetadata.getName(), modid, serverVersion, clientVersion == null ? "null" : clientVersion));
//                        return;
//                    }
//                } else {
//                    event.pendingConnection.drop(translationStorage.translate("disconnect.stationapi:missing_mod", modMetadata.getName(), modid, serverVersion));
//                    return;
//                }
//            }
//        }
    }
}
