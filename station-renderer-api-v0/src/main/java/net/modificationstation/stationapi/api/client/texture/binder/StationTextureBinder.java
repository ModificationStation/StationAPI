package net.modificationstation.stationapi.api.client.texture.binder;

import lombok.Getter;
import net.minecraft.client.render.texture.DynamicTexture;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.TexturePackDependent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;

public abstract class StationTextureBinder extends DynamicTexture implements StaticReferenceProvider, TexturePackDependent {

    @Getter
    private final Atlas.Sprite staticReference;

    public StationTextureBinder(Atlas.Sprite staticReference) {
        super(staticReference.index);
        this.staticReference = staticReference;
    }

    @Override
    public void bind(TextureManager manager) {
        StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).bindTexture();
    }
}
