package net.modificationstation.stationapi.api.client.texture;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationFrameResourceMetadata;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class MissingSprite {

    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;
    private static final String MISSINGNO_ID = "missingno";
    private static final Identifier MISSINGNO = Identifier.of("missingno");
    private static final AnimationResourceMetadata METADATA = new AnimationResourceMetadata(ImmutableList.of(new AnimationFrameResourceMetadata(0, -1)), 16, 16, 1, false);
    @Nullable
    private static NativeImageBackedTexture texture;

    private static NativeImage createImage(int width, int height) {
        NativeImage nativeImage = new NativeImage(width, height, false);
        final int i = 0xff6b3f7f;
        final int j = 0xffd67fff;

        for (int k = 0; k < height; ++k) for (int l = 0; l < width; ++l) nativeImage.setColor(l, k, k == 0 || l == 0 ? i : j);

        nativeImage.untrack();
        return nativeImage;
    }

    public static SpriteContents createSpriteContents() {
        NativeImage nativeImage = MissingSprite.createImage(WIDTH, HEIGHT);
        return new SpriteContents(MISSINGNO, new SpriteDimensions(WIDTH, HEIGHT), nativeImage, METADATA);
    }

    public static Identifier getMissingSpriteId() {
        return MISSINGNO;
    }

    public static NativeImageBackedTexture getMissingSpriteTexture() {
        if (texture == null) {
            NativeImage nativeImage = MissingSprite.createImage(WIDTH, HEIGHT);
            nativeImage.untrack();
            texture = new NativeImageBackedTexture(nativeImage);
            //noinspection deprecation
            StationTextureManager.get(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager).registerTexture(MISSINGNO, texture);
        }
        return texture;
    }
}
