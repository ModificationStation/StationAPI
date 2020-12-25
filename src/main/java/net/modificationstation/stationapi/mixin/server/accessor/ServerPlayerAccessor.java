package net.modificationstation.stationapi.mixin.server.accessor;

import net.minecraft.entity.player.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayer.class)
public interface ServerPlayerAccessor {

    @Invoker
    void invokeMethod_314();

    @Accessor
    int getField_260();
}
