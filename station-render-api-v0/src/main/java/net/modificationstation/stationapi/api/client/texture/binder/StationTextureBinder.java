package net.modificationstation.stationapi.api.client.texture.binder;

import lombok.Getter;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;

public abstract class StationTextureBinder extends TextureBinder implements StaticReferenceProvider {

    @Getter
    private final Atlas.Texture staticReference;

    public StationTextureBinder(Atlas.Texture staticReference) {
        super(staticReference.index);
        this.staticReference = staticReference;
    }

    @Override
    public void bindTexture(TextureManager manager) {
        staticReference.getAtlas().bindAtlas();
    }
}
