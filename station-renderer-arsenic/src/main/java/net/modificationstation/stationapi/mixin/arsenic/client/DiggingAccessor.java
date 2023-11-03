package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.block.Block;
import net.minecraft.client.particle.BlockParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockParticle.class)
public interface DiggingAccessor {

    @Accessor
    Block getField_2383();
}
