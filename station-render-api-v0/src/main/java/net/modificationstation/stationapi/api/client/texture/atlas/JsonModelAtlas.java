package net.modificationstation.stationapi.api.client.texture.atlas;

import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.model.json.JsonModel;
import net.modificationstation.stationapi.api.client.registry.ModelRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public final class JsonModelAtlas extends ExpandableAtlas {

    public JsonModelAtlas(Identifier identifier) {
        super(identifier);
    }

    @Override
    public Texture addTexture(String texture) {
        Texture textureInst = super.addTexture(texture);
        ModelRegistry.INSTANCE.forEach((identifier, model) -> {
            if (model instanceof JsonModel)
                ((JsonModel) model).updateUVs();
        });
        return textureInst;
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        imageCache = null;
        textures.clear();
        textureCache.clear();
        super.refreshTextureID();
    }
}
