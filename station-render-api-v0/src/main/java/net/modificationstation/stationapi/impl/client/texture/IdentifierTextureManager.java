package net.modificationstation.stationapi.impl.client.texture;

import net.modificationstation.stationapi.api.client.texture.AbstractTexture;
import net.modificationstation.stationapi.api.registry.Identifier;

public interface IdentifierTextureManager {

    void registerTexture(Identifier identifier, AbstractTexture texture);

    void bindTexture(Identifier id);
}
