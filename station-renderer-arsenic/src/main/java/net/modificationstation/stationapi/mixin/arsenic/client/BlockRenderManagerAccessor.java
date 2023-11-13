package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockRenderManager.class)
public interface BlockRenderManagerAccessor {
    @Accessor
    BlockView getBlockView();

    @Accessor
    int getTextureOverride();

    @Accessor
    boolean getSkipFaceCulling();
}
