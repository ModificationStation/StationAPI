package net.modificationstation.stationapi.api.client.texture.atlas;

import net.modificationstation.stationapi.api.registry.Identifier;

@Deprecated
public final class JsonModelAtlas extends ExpandableAtlas {

    public static final Identifier MISSING = ExpandableAtlas.MISSING;

    public JsonModelAtlas(Identifier identifier) {
        super(identifier);
    }

//    @Override
//    public void reloadFromTexturePack(TexturePack newTexturePack) {
//        imageCache = null;
//        textures.clear();
//        textureCache.clear();
//        ModelRegistry.INSTANCE.forEach((identifier, model) -> model.reloadFromTexturePack(newTexturePack));
//        stitch();
//    }
}
