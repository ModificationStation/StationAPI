package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.BlockBase;
import net.minecraft.item.SecondaryBlock;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapCallback;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SecondaryBlock.class)
public class MixinSecondaryBlock {

    @Shadow private int tileId;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void registerCallback(int id, BlockBase block, CallbackInfo ci) {
        RegistryIdRemapCallback.event(BlockRegistry.INSTANCE).register(
                StationAPI.INTERNAL_PHASE,
                state -> tileId = state.getRawIdChangeMap().getOrDefault(tileId, tileId)
        );
    }
}
