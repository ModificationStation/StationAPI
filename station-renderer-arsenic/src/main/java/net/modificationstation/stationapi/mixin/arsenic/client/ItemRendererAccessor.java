package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Mixin(ItemRenderer.class)
public interface ItemRendererAccessor {
    @Accessor
    Random getRandom();

    @Accessor
    BlockRenderManager getBlockRenderer();
}
