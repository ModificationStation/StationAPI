package net.modificationstation.stationloader.impl.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationloader.api.common.mod.StationMod;
import net.modificationstation.stationloader.api.common.packet.StationHandshake;
import net.modificationstation.stationloader.api.server.event.network.HandleLogin;
import net.modificationstation.stationloader.impl.server.entity.player.PlayerHelper;
import net.modificationstation.stationloader.impl.server.packet.PacketHelper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

public class StationLoader extends net.modificationstation.stationloader.impl.common.StationLoader {

    protected StationLoader(ModMetadata data) {
        super(data);
    }

    @Override
    public void setup() throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException, URISyntaxException {
        if (new Exception().getStackTrace()[1].getClassName().equals(MinecraftServer.class.getName())) {
            super.setup();
        } else
            throw new IllegalAccessException("Tried running StationLoader.setup() from an unknown source!");
    }

    @Override
    public void setupAPI() {
        super.setupAPI();
        getLogger().info("Setting up PlayerHelper...");
        net.modificationstation.stationloader.api.common.entity.player.PlayerHelper.INSTANCE.setHandler(new PlayerHelper());
        getLogger().info("Setting up PacketHelper...");
        net.modificationstation.stationloader.api.common.packet.PacketHelper.INSTANCE.setHandler(new PacketHelper());
        getLogger().info("Setting up HandleLogin...");
        HandleLogin.EVENT.register((pendingConnection, arg) -> {
            if (onlyModdedClients) {
                StationHandshake handshake = (StationHandshake) arg;
                String stationLoader = handshake.getStationLoader();
                String version = handshake.getVersion();
                String serverStationLoader = "stationloader";
                TranslationStorage translationStorage = TranslationStorage.getInstance();
                Optional<ModContainer> optionalModContainer = FabricLoader.getInstance().getModContainer(serverStationLoader);
                if (optionalModContainer.isPresent()) {
                    ModContainer modContainer = optionalModContainer.get();
                    String serverVersion = modContainer.getMetadata().getVersion().getFriendlyString();
                    if (stationLoader == null || version == null || !stationLoader.equals(serverStationLoader)) {
                        pendingConnection.drop(translationStorage.translate("disconnect.stationloader:missing_sl"));
                        return;
                    } else if (!version.equals(serverVersion)) {
                        pendingConnection.drop(translationStorage.translate("disconnect.stationloader:sl_version_mismatch", serverVersion, version));
                        return;
                    }
                }
                Map<String, String> clientMods = handshake.getMods();
                ModMetadata data;
                String modid;
                String clientVersion;
                String serverVersion;
                for (StationMod serverMod : getAllMods()) {
                    if (serverMod.getSide() == null) {
                        data = serverMod.getData();
                        modid = data.getId();
                        serverVersion = data.getVersion().getFriendlyString();
                        if (clientMods.containsKey(modid)) {
                            clientVersion = clientMods.get(modid);
                            if (clientVersion == null || !clientVersion.equals(serverVersion)) {
                                pendingConnection.drop(translationStorage.translate("disconnect.stationloader:mod_version_mismatch", data.getName(), modid, serverVersion, clientVersion == null ? "null" : clientVersion));
                                return;
                            }
                        } else {
                            pendingConnection.drop(translationStorage.translate("disconnect.stationloader:missing_mod", data.getName(), modid, serverVersion));
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void addMod(ModMetadata data, EnvType envType, String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, URISyntaxException {
        if (!onlyModdedClients && envType == null)
            onlyModdedClients = true;
        super.addMod(data, envType, className);
    }

    private boolean onlyModdedClients = false;
}
