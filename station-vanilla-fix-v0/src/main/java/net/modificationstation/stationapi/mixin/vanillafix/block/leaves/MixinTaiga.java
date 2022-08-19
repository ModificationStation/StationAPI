package net.modificationstation.stationapi.mixin.vanillafix.block.leaves;

import net.minecraft.level.biome.Taiga;
import net.minecraft.level.structure.PineTree;
import net.minecraft.level.structure.SpruceTree;
import net.modificationstation.stationapi.api.vanillafix.level.structure.FixedPineTree;
import net.modificationstation.stationapi.api.vanillafix.level.structure.FixedSpruceTree;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Taiga.class)
public class MixinTaiga {



    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "getTree(Ljava/util/Random;)Lnet/minecraft/level/structure/Structure;",
            at = @At(
                    value = "NEW",
                    target = "()Lnet/minecraft/level/structure/PineTree;"
            )
    )
    private PineTree redirectPineTree() {
        return new FixedPineTree();
    }

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "getTree(Ljava/util/Random;)Lnet/minecraft/level/structure/Structure;",
            at = @At(
                    value = "NEW",
                    target = "()Lnet/minecraft/level/structure/SpruceTree;"
            )
    )
    private SpruceTree redirectSpruceTree() {
        return new FixedSpruceTree();
    }
}
