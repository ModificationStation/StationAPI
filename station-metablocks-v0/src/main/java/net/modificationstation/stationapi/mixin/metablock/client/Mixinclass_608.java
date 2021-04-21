package net.modificationstation.stationapi.mixin.metablock.client;

import net.minecraft.block.BlockBase;
import net.minecraft.class_608;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.block.PlayerBlockHardnessPerMeta;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_608.class)
public class Mixinclass_608 {

    @Redirect(method = {"method_1707(IIII)V", "method_1721(IIII)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"))
    private float getHardnessPerMeta(BlockBase blockBase, PlayerBase arg, int i, int j, int k, int i1) {
        return ((PlayerBlockHardnessPerMeta) blockBase).getHardness(arg, arg.level.getTileMeta(i, j, k));
    }
}