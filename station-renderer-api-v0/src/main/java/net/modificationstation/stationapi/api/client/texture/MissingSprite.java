package net.modificationstation.stationapi.api.client.texture;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationFrameResourceMetadata;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.Lazy;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class MissingSprite extends Sprite {
    private static final Identifier MISSINGNO = Identifier.of("missingno");
    @Nullable
    private static NativeImageBackedTexture texture;
    private static final Lazy<NativeImage> IMAGE = new Lazy<>(() -> {
        NativeImage nativeImage = new NativeImage(16, 16, false);
        int i = 0xff6b3f7f;
        int j = 0xffd67fff;

        for (int k = 0; k < 16; ++k) {
            for (int l = 0; l < 16; ++l) {
                nativeImage.setColor(l, k, k == 0 || l == 0 ? i : j);
            }
        }

        nativeImage.untrack();
        return nativeImage;
    });
    private static final Sprite.Info INFO;

    private MissingSprite(SpriteAtlasTexture spriteAtlasTexture, int atlasWidth, int atlasHeight, int x, int y) {
        super(spriteAtlasTexture, INFO, atlasWidth, atlasHeight, x, y, IMAGE.get());
    }

    public static MissingSprite getMissingSprite(SpriteAtlasTexture spriteAtlasTexture, int atlasWidth, int atlasHeight, int x, int y) {
        return new MissingSprite(spriteAtlasTexture, atlasWidth, atlasHeight, x, y);
    }

    public static Identifier getMissingSpriteId() {
        return MISSINGNO;
    }

    public static Sprite.Info getMissingInfo() {
        return INFO;
    }

    public void close() {
        image.close();
    }

    public static NativeImageBackedTexture getMissingSpriteTexture() {
        if (texture == null)
            //noinspection deprecation
            StationTextureManager.get(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager).registerTexture(MISSINGNO, texture = new NativeImageBackedTexture(IMAGE.get()));

        return texture;
    }

    static {
        INFO = new Sprite.Info(MISSINGNO, 16, 16, new AnimationResourceMetadata(Lists.newArrayList(new AnimationFrameResourceMetadata(0, -1)), 16, 16, 1, false));
    }
}
