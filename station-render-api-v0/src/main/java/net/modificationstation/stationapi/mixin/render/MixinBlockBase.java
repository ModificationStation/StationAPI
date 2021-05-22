package net.modificationstation.stationapi.mixin.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.model.ModelRegisterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBase.class)
public class MixinBlockBase {

    @SuppressWarnings("UnresolvedMixinReference")
    @Environment(EnvType.CLIENT)
    @Inject(method = "<clinit>", at = @At(value = "NEW", target = "(II)Lnet/minecraft/block/Stone;", ordinal = 0, shift = At.Shift.BEFORE))
    private static void beforeBlockRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new ModelRegisterEvent(ModelRegisterEvent.Type.BLOCKS));
    }
}
