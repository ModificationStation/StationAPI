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
    int getTextureOverride();

    @Accessor
    void setEastFaceRotation(int eastFaceRotation);

    @Accessor
    void setWestFaceRotation(int westFaceRotation);

    @Accessor
    void setSouthFaceRotation(int southFaceRotation);

    @Accessor
    void setNorthFaceRotation(int northFaceRotation);

    @Accessor
    void setTopFaceRotation(int topFaceRotation);

    @Accessor
    void setBottomFaceRotation(int bottomFaceRotation);
}
