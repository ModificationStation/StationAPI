package net.modificationstation.stationapi.mixin.block.server;

import net.minecraft.block.BlockBase;
import net.minecraft.class_70;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.server.level.ServerLevel;
import net.modificationstation.stationapi.api.block.BlockStrengthPerMeta;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_70.class)
public class Mixinclass_70 {

    @Shadow private ServerLevel field_2310;

    @Shadow private int field_2318;

    @Shadow private int field_2319;

    @Shadow private int field_2320;

    @Redirect(method = "method_1828()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"))
    private float getHardnessPerMeta(BlockBase blockBase, PlayerBase arg) {
        return ((BlockStrengthPerMeta) blockBase).getBlockStrength(arg, field_2310.getTileMeta(field_2318, field_2319, field_2320));
    }

    @Redirect(method = "method_1830(IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"))
    private float getHardnessPerMeta2(BlockBase blockBase, PlayerBase arg, int i, int j, int k, int i1) {
        return ((BlockStrengthPerMeta) blockBase).getBlockStrength(arg, field_2310.getTileMeta(i, j, k));
    }

    @Redirect(method = "method_1829(III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"))
    private float getHardnessPerMeta3(BlockBase blockBase, PlayerBase arg, int i, int j, int k) {
        return ((BlockStrengthPerMeta) blockBase).getBlockStrength(arg, field_2310.getTileMeta(i, j, k));
    }
}
