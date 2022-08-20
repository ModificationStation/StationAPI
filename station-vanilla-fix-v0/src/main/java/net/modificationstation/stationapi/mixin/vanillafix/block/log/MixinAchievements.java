package net.modificationstation.stationapi.mixin.vanillafix.block.log;

import net.minecraft.achievement.Achievements;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Achievements.class)
public class MixinAchievements {

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;LOG:Lnet/minecraft/block/BlockBase;"
            )
    )
    private static BlockBase redirect() {
        return Blocks.OAK_LOG;
    }
}
