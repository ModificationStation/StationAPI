package net.modificationstation.stationloader.mixin.common;

import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationloader.api.common.event.block.TileEntityRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityBase.class)
public class MixinTileEntityBase {

    @Shadow
    private static void register(Class<?> arg, String string) {
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method="<clinit>", at=@At(value = "TAIL"))
    private static void registerModdedTileEntities(CallbackInfo ci) {
        TileEntityRegister.EVENT.getInvoker().registerTileEntities(MixinTileEntityBase::register);
    }
}
