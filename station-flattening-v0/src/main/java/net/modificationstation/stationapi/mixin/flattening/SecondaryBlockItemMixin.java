package net.modificationstation.stationapi.mixin.flattening;

import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.item.SecondaryBlockItem;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SecondaryBlockItem.class)
class SecondaryBlockItemMixin {
    @Shadow private int blockId;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void stationapi_registerCallback(int id, Block block, CallbackInfo ci) {
        BlockRegistry.INSTANCE.getEventBus().register(Listener.<RegistryIdRemapEvent<Block>>simple()
                .listener(event -> blockId = event.state.getRawIdChangeMap().getOrDefault(blockId, blockId))
                .phase(StationAPI.INTERNAL_PHASE)
                .build());
    }
}
