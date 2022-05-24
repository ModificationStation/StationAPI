package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.render.WorldRenderer;
import net.modificationstation.stationapi.impl.client.render.StationWorldRendererImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer implements StationWorldRendererImpl.StationWorldRendererAccess {

    @Unique
    private final StationWorldRendererImpl stationapi$stationWorldRenderer = new StationWorldRendererImpl((WorldRenderer) (Object) this);

    @Override
    public StationWorldRendererImpl stationapi$stationWorldRenderer() {
        return stationapi$stationWorldRenderer;
    }

    @Inject(
            method = "method_1537()V",
            at = @At("HEAD")
    )
    private void resetVboPool(CallbackInfo ci) {
        stationapi$stationWorldRenderer.resetVboPool();
    }
}
