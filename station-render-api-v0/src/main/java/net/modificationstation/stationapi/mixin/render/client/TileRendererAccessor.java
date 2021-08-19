package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.render.block.BlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockRenderer.class)
public interface TileRendererAccessor {

    @Accessor
    float getField_56();

    @Accessor
    float getField_57();

    @Accessor
    float getField_58();

    @Accessor
    float getField_59();

    @Accessor
    float getField_60();

    @Accessor
    float getField_61();

    @Accessor
    float getField_62();

    @Accessor
    float getField_63();

    @Accessor
    float getField_64();

    @Accessor
    float getField_65();

    @Accessor
    float getField_66();

    @Accessor
    float getField_68();

    @Accessor
    int getTextureOverride();

    @Accessor
    boolean getMirrorTexture();

    @Accessor
    int getEastFaceRotation();

    @Accessor
    int getWestFaceRotation();

    @Accessor
    int getSouthFaceRotation();

    @Accessor
    int getNorthFaceRotation();

    @Accessor
    int getTopFaceRotation();

    @Accessor
    int getBottomFaceRotation();

    @Accessor
    boolean getField_92();
}
