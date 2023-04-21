package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Fire;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.sync.trackers.IntArrayTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Fire.class)
public class MixinFire {

    @Shadow private int[] field_2307;

    @Shadow private int[] spreadDelayChance;

    @ModifyConstant(
            method = "<init>",
            constant = @Constant(intValue = 256)
    )
    private int blocksSize(int constant) {
        return BlockBase.BY_ID.length;
    }

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void setupTrackers(int j, int par2, CallbackInfo ci) {
        BlockRegistry registry = BlockRegistry.INSTANCE;
        IntArrayTracker.register(registry, () -> field_2307, array -> field_2307 = array);
        IntArrayTracker.register(registry, () -> spreadDelayChance, array -> spreadDelayChance = array);
    }
}
