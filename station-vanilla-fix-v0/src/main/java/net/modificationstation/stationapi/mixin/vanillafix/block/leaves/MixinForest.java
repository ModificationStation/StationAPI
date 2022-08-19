package net.modificationstation.stationapi.mixin.vanillafix.block.leaves;

import net.minecraft.level.biome.Forest;
import net.minecraft.level.structure.BirchTree;
import net.minecraft.level.structure.LargeOak;
import net.minecraft.level.structure.OakTree;
import net.modificationstation.stationapi.api.vanillafix.level.structure.FixedBirchTree;
import net.modificationstation.stationapi.api.vanillafix.level.structure.FixedLargeOak;
import net.modificationstation.stationapi.api.vanillafix.level.structure.FixedOakTree;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Forest.class)
public class MixinForest {

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "getTree(Ljava/util/Random;)Lnet/minecraft/level/structure/Structure;",
            at = @At(
                    value = "NEW",
                    target = "()Lnet/minecraft/level/structure/BirchTree;"
            )
    )
    private BirchTree redirectBirch() {
        return new FixedBirchTree();
    }

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "getTree(Ljava/util/Random;)Lnet/minecraft/level/structure/Structure;",
            at = @At(
                    value = "NEW",
                    target = "()Lnet/minecraft/level/structure/LargeOak;"
            )
    )
    private LargeOak redirectLargeOak() {
        return new FixedLargeOak();
    }

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "getTree(Ljava/util/Random;)Lnet/minecraft/level/structure/Structure;",
            at = @At(
                    value = "NEW",
                    target = "()Lnet/minecraft/level/structure/OakTree;"
            )
    )
    private OakTree redirectOakTree() {
        return new FixedOakTree();
    }
}
