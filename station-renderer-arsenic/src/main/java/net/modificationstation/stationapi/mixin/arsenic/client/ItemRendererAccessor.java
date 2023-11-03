package net.modificationstation.stationapi.mixin.arsenic.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.item.ItemRenderer;

@Mixin(ItemRenderer.class)
public interface ItemRendererAccessor {

    @Accessor
    Random getRand();

    @Accessor
    BlockRenderManager getField_1708();
}
