package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import org.jetbrains.annotations.NotNull;

public class StationVanillaTextureBinder extends StationTextureBinder {

    @NotNull
    private final StationTextureBinder originalBinder;

    public StationVanillaTextureBinder(@NotNull Atlas.Sprite staticReference, @NotNull StationTextureBinder originalBinder) {
        super(staticReference);
        this.originalBinder = originalBinder;
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        originalBinder.textureSize = textureSize;
        originalBinder.reloadFromTexturePack(newTexturePack);
        grid = originalBinder.grid;
    }

    @Override
    public void update() {
        originalBinder.update();
        grid = originalBinder.grid;
    }

    @Override
    public void bindTexture(TextureManager manager) {
        originalBinder.bindTexture(manager);
    }
}
