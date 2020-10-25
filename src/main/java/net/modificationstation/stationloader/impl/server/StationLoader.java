package net.modificationstation.stationloader.impl.server;

import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationloader.impl.server.entity.player.PlayerHelper;
import net.modificationstation.stationloader.impl.server.packet.PacketHelper;

import java.io.IOException;
import java.net.URISyntaxException;

public class StationLoader extends net.modificationstation.stationloader.impl.common.StationLoader {

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
    }
}
