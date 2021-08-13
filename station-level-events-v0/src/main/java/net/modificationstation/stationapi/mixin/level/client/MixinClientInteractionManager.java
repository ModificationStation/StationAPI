package net.modificationstation.stationapi.mixin.level.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.BlockUseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientInteractionManager.class)
public class MixinClientInteractionManager {

    @Redirect(method = "method_1713", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;canUse(Lnet/minecraft/level/Level;IIILnet/minecraft/entity/player/PlayerBase;)Z"))
    private boolean interceptBlockInteract(BlockBase blockBase, Level level, int x, int y, int z, PlayerBase player) {
        return !StationAPI.EVENT_BUS.post(new BlockUseEvent(level, x, y, z)).isCancelled() && blockBase.canUse(level, x, y, z, player);
    }
}
