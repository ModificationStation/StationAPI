package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.server.ServerPlayerView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ServerPlayerView.class)
public interface ServerPlayerViewAccessor {

    @Accessor
    List<ServerPlayerView.class_514> getField_2131();
}
