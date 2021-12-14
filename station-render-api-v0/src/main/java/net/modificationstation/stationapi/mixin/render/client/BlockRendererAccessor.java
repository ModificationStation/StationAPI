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
    float getColourRed00();

    @Accessor
    float getColourGreen00();

    @Accessor
    float getColourBlue00();

    @Accessor
    float getColourRed01();

    @Accessor
    float getColourGreen01();

    @Accessor
    float getColourBlue01();

    @Accessor
    float getColurRed11();

    @Accessor
    float getColourGreen11();

    @Accessor
    float getColourBlue11();

    @Accessor
    float getColourRed10();

    @Accessor
    float getColourGreen10();

    @Accessor
    float getColourBlue10();

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
    boolean getShadeTopFace();

    @Accessor
    void setShadeTopFace(boolean shadeTopFace);
}
