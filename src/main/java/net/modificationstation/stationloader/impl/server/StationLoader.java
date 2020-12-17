package net.modificationstation.stationloader.impl.server;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationloader.api.common.event.EventRegistry;
import net.modificationstation.stationloader.api.common.packet.StationHandshake;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import net.modificationstation.stationloader.api.common.registry.ModID;
import net.modificationstation.stationloader.api.server.event.network.HandleLogin;
import net.modificationstation.stationloader.api.server.event.network.PlayerLogin;
import net.modificationstation.stationloader.impl.server.entity.player.PlayerHelper;
import net.modificationstation.stationloader.impl.server.gui.GuiHelper;
import net.modificationstation.stationloader.impl.server.packet.PacketHelper;

import java.util.Map;

public class StationLoader extends net.modificationstation.stationloader.impl.common.StationLoader {

    @Override
    public void setupAPI() {
        super.setupAPI();
        getLogger().info("Setting up PlayerHelper...");
        net.modificationstation.stationloader.api.common.entity.player.PlayerHelper.INSTANCE.setHandler(new PlayerHelper());
        getLogger().info("Setting up PacketHelper...");
        net.modificationstation.stationloader.api.common.packet.PacketHelper.INSTANCE.setHandler(new PacketHelper());
        getLogger().info("Setting up HandleLogin...");
        HandleLogin.EVENT.register((pendingConnection, arg) -> {
            if (!getModsToVerifyOnClient().isEmpty()) {
                StationHandshake handshake = (StationHandshake) arg;
                String stationLoader = handshake.getStationLoader();
                String version = handshake.getVersion();
                ModID modID = getModID();
                String serverStationLoader = modID.toString();
                String serverSLVersion = modID.getVersion().getFriendlyString();
                TranslationStorage translationStorage = TranslationStorage.getInstance();
                if (stationLoader == null || version == null || !stationLoader.equals(serverStationLoader)) {
                    pendingConnection.drop(translationStorage.translate("disconnect.stationloader:missing_sl"));
                    return;
                } else if (!version.equals(serverSLVersion)) {
                    pendingConnection.drop(translationStorage.translate("disconnect.stationloader:sl_version_mismatch", serverSLVersion, version));
                    return;
                }
                Map<String, String> clientMods = handshake.getMods();
                String modid;
                String clientVersion;
                String serverVersion;
                for (ModContainer serverMod : getModsToVerifyOnClient()) {
                    ModMetadata modMetadata = serverMod.getMetadata();
                    modid = modMetadata.getId();
                    serverVersion = modMetadata.getVersion().getFriendlyString();
                    if (clientMods.containsKey(modid)) {
                        clientVersion = clientMods.get(modid);
                        if (clientVersion == null || !clientVersion.equals(serverVersion)) {
                            pendingConnection.drop(translationStorage.translate("disconnect.stationloader:mod_version_mismatch", modMetadata.getName(), modid, serverVersion, clientVersion == null ? "null" : clientVersion));
                            return;
                        }
                    } else {
                        pendingConnection.drop(translationStorage.translate("disconnect.stationloader:missing_mod", modMetadata.getName(), modid, serverVersion));
                        return;
                    }
                }
            }
        });
        getLogger().info("Setting up GuiHelper...");
        net.modificationstation.stationloader.api.common.gui.GuiHelper.INSTANCE.setHandler(new GuiHelper());
    }

    @Override
    public void preInit(EventRegistry eventRegistry, ModID modID) {
        super.preInit(eventRegistry, modID);
        eventRegistry.registerValue(Identifier.of(modID, "handle_login"), HandleLogin.EVENT);
        eventRegistry.registerValue(Identifier.of(modID, "player_login"), PlayerLogin.EVENT);
    }
}
