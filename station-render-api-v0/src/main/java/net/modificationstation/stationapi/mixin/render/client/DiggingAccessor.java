package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.particle.Digging;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Digging.class)
public interface DiggingAccessor {

    @Accessor
    BlockBase getField_2383();
}
