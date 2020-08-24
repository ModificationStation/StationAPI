package net.modificationstation.stationloader.impl.server;

import net.minecraft.client.MinecraftApplet;
import net.minecraft.server.MinecraftServer;

public class StationLoader extends net.modificationstation.stationloader.impl.common.StationLoader {

    @Override
    public void setup() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (new Exception().getStackTrace()[1].getClassName().equals(MinecraftServer.class.getName())) {
            super.setup();
        } else
            throw new IllegalAccessException("Tried running StationLoader.setup() from an unknown source!");
    }
}
