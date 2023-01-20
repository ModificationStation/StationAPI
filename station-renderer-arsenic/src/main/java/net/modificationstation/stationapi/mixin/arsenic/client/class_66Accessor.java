package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.class_66;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_66.class)
public interface class_66Accessor {

    @Invoker("method_306")
    void stationapi$method_306();
}
