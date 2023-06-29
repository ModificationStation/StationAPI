package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.server.ServerPlayerView;
import net.minecraft.tileentity.TileEntityBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayerView.class_514.class)
public interface class_514Accessor {
    @Invoker
    void invokeMethod_1756(TileEntityBase tileEntity);
}
