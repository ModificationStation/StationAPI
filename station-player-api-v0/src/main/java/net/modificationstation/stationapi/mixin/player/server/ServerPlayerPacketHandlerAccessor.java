package net.modificationstation.stationapi.mixin.player.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.SERVER)
@Mixin(ServerPlayerPacketHandler.class)
public interface ServerPlayerPacketHandlerAccessor {

    @Accessor
    ServerPlayer getServerPlayer();
}
