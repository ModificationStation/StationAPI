package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.binder.AnimatedTextureBinder;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import org.jetbrains.annotations.NotNull;

public class StationVanillaTextureBinder extends StationTextureBinder {

    @NotNull
    private final String animationPath;
    @NotNull
    private final StationTextureBinder originalBinder;
    @NotNull
    private final AnimatedTextureBinder animatedTextureBinder;

    private boolean animationImageAbsent;

    public StationVanillaTextureBinder(@NotNull Atlas.Texture staticReference, @NotNull StationTextureBinder originalBinder, @NotNull String animationPath) {
        super(staticReference);
        this.animationPath = animationPath;
        this.originalBinder = originalBinder;
        animatedTextureBinder = new AnimatedTextureBinder(getStaticReference(), animationPath, 0);
        //noinspection deprecation
        refreshTextures(((Minecraft) FabricLoader.getInstance().getGameInstance()).texturePackManager.texturePack);
    }

    @Override
    public void refreshTextures(TexturePack newTexturePack) {
        animationImageAbsent = TextureHelper.getTextureStream(animationPath) == null;
        StationTextureBinder textureBinder = animationImageAbsent ? originalBinder : animatedTextureBinder;
        textureBinder.refreshTextures(newTexturePack);
        grid = textureBinder.grid;
    }

    @Override
    public void update() {
        StationTextureBinder textureBinder = animationImageAbsent ? originalBinder : animatedTextureBinder;
        textureBinder.update();
        grid = textureBinder.grid;
    }

    @Override
    public void bindTexture(TextureManager manager) {
        (animationImageAbsent ? originalBinder : animatedTextureBinder).bindTexture(manager);
    }
}
