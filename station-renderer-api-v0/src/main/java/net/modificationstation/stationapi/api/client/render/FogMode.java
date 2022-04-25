package net.modificationstation.stationapi.api.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum FogMode {

    EXP(0),
    EXP2(1),
    LINEAR(2);

    private final int id;

    FogMode(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
