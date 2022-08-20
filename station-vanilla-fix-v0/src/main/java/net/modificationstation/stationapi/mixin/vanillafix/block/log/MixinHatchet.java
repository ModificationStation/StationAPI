package net.modificationstation.stationapi.mixin.vanillafix.block.log;

import com.google.common.base.Predicates;
import com.google.common.collect.ObjectArrays;
import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.Hatchet;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(Hatchet.class)
public class MixinHatchet {

    @Shadow private static BlockBase[] effectiveBlocks;

    @Inject(
            method = "<clinit>()V",
            at = @At("RETURN")
    )
    private static void fixArray(CallbackInfo ci) {
        effectiveBlocks = ObjectArrays.concat(Arrays.stream(effectiveBlocks).filter(Predicates.notNull()).toArray(BlockBase[]::new), new BlockBase[] { Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG }, BlockBase.class);
    }
}
