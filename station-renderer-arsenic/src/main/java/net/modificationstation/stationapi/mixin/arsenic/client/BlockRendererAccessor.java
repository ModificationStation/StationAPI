package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockRenderer.class)
public interface BlockRendererAccessor {

    @Accessor
    BlockView getBlockView();

    @Accessor("blockView")
    void stationapi$setBlockView(BlockView blockView);

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

    @Accessor("textureOverride")
    void stationapi$setTextureOverride(int textureOverride);

    @Accessor
    boolean getMirrorTexture();

    @Accessor
    void setMirrorTexture(boolean mirrorTexture);

    @Accessor
    boolean getRenderAllSides();

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
    boolean getShadeTopFace();

    @Invoker("method_43")
    float stationapi$method_43(int x, int y, int z, Material material);
}
