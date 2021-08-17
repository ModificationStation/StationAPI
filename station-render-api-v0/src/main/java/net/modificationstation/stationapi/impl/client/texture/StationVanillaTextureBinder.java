package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.binder.AnimatedTextureBinder;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.client.texture.binder.TexturePackDependent;

public class StationVanillaTextureBinder extends StationTextureBinder implements TexturePackDependent {

    private final String animationPath;

    private final TextureBinder originalBinder;
    private final AnimatedTextureBinder animatedTextureBinder;

    private boolean animationImageAbsent;

    public StationVanillaTextureBinder(Atlas.Texture staticReference, TextureBinder originalBinder, String animationPath) {
        super(staticReference);
        this.animationPath = animationPath;
        this.originalBinder = originalBinder;
        animatedTextureBinder = new AnimatedTextureBinder(getStaticReference(), animationPath, 0);
        refreshTextures();
    }

    @Override
    public void refreshTextures() {
        animationImageAbsent = TextureHelper.getTextureStream(animationPath) == null;
        if (animationImageAbsent) {
            if (originalBinder instanceof TexturePackDependent)
                ((TexturePackDependent) originalBinder).refreshTextures();
            grid = originalBinder.grid;
        }
        else {
            animatedTextureBinder.refreshTextures();
            grid = animatedTextureBinder.grid;
        }
    }

    @Override
    public void update() {
        TextureBinder textureBinder = animationImageAbsent ? originalBinder : animatedTextureBinder;
        textureBinder.update();
        grid = textureBinder.grid;
    }

    @Override
    public void bindTexture(TextureManager manager) {
        (animationImageAbsent ? originalBinder : animatedTextureBinder).bindTexture(manager);
    }
}
