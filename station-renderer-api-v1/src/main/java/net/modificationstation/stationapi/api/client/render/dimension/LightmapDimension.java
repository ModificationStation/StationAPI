package net.modificationstation.stationapi.api.client.render.dimension;

public interface LightmapDimension {
    float ambientLight();

    void adjustLightmap(float delta);
}
