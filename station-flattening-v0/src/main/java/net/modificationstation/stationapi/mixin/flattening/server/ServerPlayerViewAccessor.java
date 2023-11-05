package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.class_167;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(class_167.class)
public interface ServerPlayerViewAccessor {
    @Accessor
    List<class_167.class_514> getField_2131();
}
