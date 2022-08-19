package net.modificationstation.stationapi.mixin.vanillafix.block.leaves;

import net.minecraft.block.BlockBase;
import net.minecraft.level.source.OverworldLevelSource;
import net.minecraft.level.structure.Deadbush;
import net.minecraft.level.structure.TallGrass;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.vanillafix.level.structure.FixedDeadbush;
import net.modificationstation.stationapi.api.vanillafix.level.structure.FixedTallGrass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OverworldLevelSource.class)
public class MixinOverworldLevelSource {

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "decorate(Lnet/minecraft/level/source/LevelSource;II)V",
            at = @At(
                    value = "NEW",
                    target = "(I)Lnet/minecraft/level/structure/Deadbush;"
            )
    )
    private Deadbush redirectDeadbush(int id) {
        return new FixedDeadbush(((BlockStateHolder) BlockBase.BY_ID[id]).getDefaultState());
    }

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "decorate(Lnet/minecraft/level/source/LevelSource;II)V",
            at = @At(
                    value = "NEW",
                    target = "(II)Lnet/minecraft/level/structure/TallGrass;"
            )
    )
    private TallGrass redirectTallGrass(int id, int meta) {
        return new FixedTallGrass(id, meta);
    }
}
