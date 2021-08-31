package net.modificationstation.stationapi.api.client.texture.atlas;

import net.modificationstation.stationapi.api.client.registry.JsonModelRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public final class JsonModelAtlas extends ExpandableAtlas {

    public JsonModelAtlas(Identifier identifier) {
        super(identifier);
    }

    @Override
    public Texture addTexture(String texture) {
        Texture textureInst = super.addTexture(texture);
        JsonModelRegistry.INSTANCE.forEach((identifier, jsonModel) -> jsonModel.updateUVs());
        return textureInst;
    }

    @Override
    public void refreshTextures() {
        imageCache = null;
        textures.clear();
        textureCache.clear();
        JsonModelRegistry.INSTANCE.forEach((identifier, jsonModel) -> jsonModel.reload());
        super.refreshTextureID();
    }
}
