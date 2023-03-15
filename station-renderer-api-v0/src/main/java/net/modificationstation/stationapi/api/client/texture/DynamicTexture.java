package net.modificationstation.stationapi.api.client.texture;

import net.modificationstation.stationapi.api.registry.Identifier;

import java.io.IOException;
import java.nio.file.Path;

public interface DynamicTexture {
    void save(Identifier id, Path path) throws IOException;
}

