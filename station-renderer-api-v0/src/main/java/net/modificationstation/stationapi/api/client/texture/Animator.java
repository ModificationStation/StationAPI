package net.modificationstation.stationapi.api.client.texture;

public interface Animator extends AutoCloseable {
    void tick(int x, int y);

    @Override
    void close();
}

