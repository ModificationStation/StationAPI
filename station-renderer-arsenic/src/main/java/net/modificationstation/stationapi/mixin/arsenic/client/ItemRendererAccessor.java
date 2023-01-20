package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Mixin(ItemRenderer.class)
public interface ItemRendererAccessor {

    @Accessor
    Random getRand();

    @Accessor
    BlockRenderer getField_1708();
}
