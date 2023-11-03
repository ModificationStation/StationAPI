package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.class_167;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_167.class_514.class)
public interface class_514Accessor {
    @Invoker
    void invokeMethod_1756(BlockEntity tileEntity);
}
