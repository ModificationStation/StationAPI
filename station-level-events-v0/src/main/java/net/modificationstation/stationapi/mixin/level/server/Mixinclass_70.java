package net.modificationstation.stationapi.mixin.level.server;
import net.minecraft.block.BlockBase;
import net.minecraft.class_70;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.BlockUseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_70.class)
public class Mixinclass_70 {

    @Redirect(method = "method_1832", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;canUse(Lnet/minecraft/level/Level;IIILnet/minecraft/entity/player/PlayerBase;)Z"))
    private boolean interceptBlockInteract(BlockBase blockBase, Level level, int x, int y, int z, PlayerBase player) {
        return !StationAPI.EVENT_BUS.post(new BlockUseEvent(level, x, y, z)).isCancelled() && blockBase.canUse(level, x, y, z, player);
    }
}
