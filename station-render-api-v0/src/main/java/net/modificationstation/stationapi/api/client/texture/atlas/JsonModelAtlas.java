package net.modificationstation.stationapi.api.client.texture.atlas;

import net.modificationstation.stationapi.api.client.registry.JsonModelRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class JsonModelAtlas extends ExpandableAtlas {

    public static final JsonModelAtlas STATION_JSON_MODELS = new JsonModelAtlas(Identifier.of(MODID, "json_textures")).initTessellator();

    private JsonModelAtlas(Identifier identifier) {
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
        JsonModelRegistry.INSTANCE.forEach((identifier, jsonModel) -> jsonModel.reload());
        super.refreshTextureID();
    }
}
