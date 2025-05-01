package net.modificationstation.stationapi.mixin.flattening;

import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.item.BucketItem;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BucketItem.class)
class BucketItemMixin {
    @Shadow private int field_842;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void stationapi_registerCallback(int i, int par2, CallbackInfo ci) {
        BlockRegistry.INSTANCE.getEventBus().register(Listener.<RegistryIdRemapEvent<Block>>simple()
                .listener(event -> field_842 = event.state.getRawIdChangeMap().getOrDefault(field_842, field_842))
                .phase(StationAPI.INTERNAL_PHASE)
                .build());
    }
}
