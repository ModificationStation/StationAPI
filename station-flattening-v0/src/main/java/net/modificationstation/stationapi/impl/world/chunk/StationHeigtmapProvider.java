package net.modificationstation.stationapi.impl.world.chunk;

public interface StationHeigtmapProvider {
    byte[] getStoredHeightmap();
    void loadStoredHeightmap(byte[] heightmap);
}
