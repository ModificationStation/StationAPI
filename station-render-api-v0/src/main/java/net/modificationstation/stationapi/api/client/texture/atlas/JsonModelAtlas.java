package net.modificationstation.stationapi.api.client.texture.atlas;

import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.registry.Identifier;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderAPI.MODID;

public final class JsonModelAtlas extends ExpandableAtlas {

    public static final Identifier MISSING = Identifier.of(MODID, "missing");

    public JsonModelAtlas(Identifier identifier) {
        super(identifier);
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        imageCache = null;
        textures.clear();
        textureCache.clear();
    }
}
