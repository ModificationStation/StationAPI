package net.modificationstation.stationapi.api.client.blaze3d.systems;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface RenderCall {
    void execute();
}