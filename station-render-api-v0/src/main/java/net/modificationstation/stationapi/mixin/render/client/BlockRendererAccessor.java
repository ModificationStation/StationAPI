package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockRenderer.class)
public interface BlockRendererAccessor {

    @Accessor
    BlockView getBlockView();

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
    void setMirrorTexture(boolean mirrorTexture);

    @Accessor
    boolean getRenderAllSides();

    @Accessor
    int getEastFaceRotation();

    @Accessor
    void setEastFaceRotation(int eastFaceRotation);

    @Accessor
    int getWestFaceRotation();

    @Accessor
    void setWestFaceRotation(int westFaceRotation);

    @Accessor
    int getSouthFaceRotation();

    @Accessor
    void setSouthFaceRotation(int southFaceRotation);

    @Accessor
    int getNorthFaceRotation();

    @Accessor
    void setNorthFaceRotation(int northFaceRotation);

    @Accessor
    int getTopFaceRotation();

    @Accessor
    void setTopFaceRotation(int topFaceRotation);

    @Accessor
    int getBottomFaceRotation();

    @Accessor
    void setBottomFaceRotation(int bottomFaceRotation);

    @Accessor
    boolean getField_92();
}
