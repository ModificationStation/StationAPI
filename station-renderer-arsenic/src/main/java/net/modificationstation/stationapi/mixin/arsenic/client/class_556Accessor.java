package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.class_556;
import net.minecraft.client.render.block.BlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_556.class)
public interface class_556Accessor {

    @Accessor("field_2405")
    BlockRenderer stationapi$getField_2405();
}
