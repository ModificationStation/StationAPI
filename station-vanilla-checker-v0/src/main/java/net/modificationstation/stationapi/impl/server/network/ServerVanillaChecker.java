package net.modificationstation.stationapi.impl.server.network;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.ServerPlayer;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.server.event.network.PlayerAttemptLoginEvent;
import net.modificationstation.stationapi.impl.network.ModdedPacketHandlerSetter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.impl.network.VanillaChecker.CLIENT_REQUIRED_MODS;
import static net.modificationstation.stationapi.impl.network.VanillaChecker.MASK;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class ServerVanillaChecker {

    @EventListener
    private static void onPlayerLogin(PlayerAttemptLoginEvent event) {
        if ((event.loginRequestPacket.worldSeed & MASK) == MASK)
            ((ModdedPacketHandlerSetter) event.serverPacketHandler).setModded();
        else if (!CLIENT_REQUIRED_MODS.isEmpty()) {
            LOGGER.error("Player \"" + event.loginRequestPacket.username + "\" attempted joining the server without " + MODID.getName() + ", disconnecting.");
            event.serverPacketHandler.disconnect(I18n.translate("disconnect.stationapi:missing_station"));
        }
    }

    @EventListener
    private static void registerMessages(MessageListenerRegistryEvent event) {
        Registry.register(event.registry, MODID.id("modlist"), (player, message) -> {
            if (!CLIENT_REQUIRED_MODS.isEmpty()) {
                LOGGER.info("Received a list of mods from player \"" + player.name + "\", verifying...");
                ServerPlayer serverPlayer = (ServerPlayer) player;
                String version = message.strings[0];
                String serverStationVersion = MODID.getVersion().getFriendlyString();
                if (!version.equals(serverStationVersion)) {
                    LOGGER.error("Player \"" + player.name + "\" has a mismatching " + MODID.getName() + " version \"" + version + "\", disconnecting.");
                    serverPlayer.packetHandler.kick(I18n.translate("disconnect.stationapi:station_version_mismatch", serverStationVersion, version));
                    return;
                }
                Map<String, String> clientMods = new HashMap<>();
                for (int i = 1; i < message.strings.length; i += 2)
                    clientMods.put(message.strings[i], message.strings[i + 1]);
                LOGGER.info("Player \"" + player.name + "\"'s mods: " + clientMods.entrySet().stream().map(stringStringEntry -> "modid=" + stringStringEntry.getKey() + " version=" + stringStringEntry.getValue()).collect(Collectors.joining(", ", "[", "]")));
                String modid;
                String clientVersion;
                String serverVersion;
                for (ModContainer serverMod : CLIENT_REQUIRED_MODS) {
                    ModMetadata modMetadata = serverMod.getMetadata();
                    modid = modMetadata.getId();
                    serverVersion = modMetadata.getVersion().getFriendlyString();
                    if (clientMods.containsKey(modid)) {
                        clientVersion = clientMods.get(modid);
                        if (clientVersion == null || !clientVersion.equals(serverVersion)) {
                            LOGGER.error("Player \"" + player.name + "\" has a mismatching " + modMetadata.getName() + " (" + modid + ")" + " version \"" + clientVersion + "\", disconnecting.");
                            serverPlayer.packetHandler.kick(I18n.translate("disconnect.stationapi:mod_version_mismatch", modMetadata.getName(), modid, serverVersion, clientVersion == null ? "null" : clientVersion));
                            return;
                        }
                    } else {
                        LOGGER.error("Player \"" + player.name + "\" has a missing mod " + modMetadata.getName() + " (" + modid + "), disconnecting.");
                        serverPlayer.packetHandler.kick(I18n.translate("disconnect.stationapi:missing_mod", modMetadata.getName(), modid, serverVersion));
                        return;
                    }
                }
                LOGGER.info("Player \"" + player.name + "\"'s mods have passed verification.");
            }
        });
    }
}
