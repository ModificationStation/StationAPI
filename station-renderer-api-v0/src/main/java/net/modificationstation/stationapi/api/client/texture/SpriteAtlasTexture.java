package net.modificationstation.stationapi.api.client.texture;

import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceHelper;

public class SpriteAtlasTexture {

    private final Identifier id;
    private final int glId;

    public SpriteAtlasTexture(TextureManager manager, Identifier id) {
        this.id = id;
        glId = manager.getTextureId(ResourceHelper.ASSETS.toPath(id, "textures/atlas", "png"));

    }
}
