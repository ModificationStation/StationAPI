package net.modificationstation.stationapi.mixin.level;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class MixinLevel {

    @Shadow public abstract int getTileId(int x, int y, int z);

    @Inject(
            method = {
                    "<init>(Lnet/minecraft/level/dimension/DimensionData;Ljava/lang/String;Lnet/minecraft/level/dimension/Dimension;J)V",
                    "<init>(Lnet/minecraft/level/Level;Lnet/minecraft/level/dimension/Dimension;)V",
                    "<init>(Lnet/minecraft/level/dimension/DimensionData;Ljava/lang/String;JLnet/minecraft/level/dimension/Dimension;)V"
            },
            at = @At("RETURN")
    )
    private void onCor1(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(LevelEvent.Init.builder().level((Level) (Object) this).build());
    }

    @Inject(
            method = "canPlaceTile(IIIIZI)Z",
            at = {
                    @At(
                            value = "RETURN",
                            ordinal = 1
                    ),
                    @At(
                            value = "RETURN",
                            ordinal = 2
                    )
            },
            cancellable = true
    )
    private void isBlockReplaceable(int id, int x, int y, int z, boolean noCollision, int meta, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(StationAPI.EVENT_BUS.post(
                LevelEvent.IsBlockReplaceable.builder()
                        .level((Level) (Object) this)
                        .x(x).y(y).z(z)
                        .block(BlockBase.BY_ID[getTileId(x, y, z)])
                        .replacedBy(BlockBase.BY_ID[id]).replacedByMeta(meta)
                        .replace(cir.getReturnValue())
                        .build()
        ).replace);
    }
}
