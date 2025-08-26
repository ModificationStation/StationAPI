package net.modificationstation.stationapi.mixin.blockentity;

import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
class BlockEntityMixin {
    @Shadow
    private static void create(Class<?> arg, String string) {}

    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void stationapi_registerModdedTileEntities(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(BlockEntityRegisterEvent.builder().register(BlockEntityMixin::create).build());
    }
}
