package net.modificationstation.stationapi.impl.level.chunk;

public interface StationHeigtmapProvider {
    byte[] getStoredHeightmap();
    void loadStoredHeightmap(byte[] heightmap);
}
