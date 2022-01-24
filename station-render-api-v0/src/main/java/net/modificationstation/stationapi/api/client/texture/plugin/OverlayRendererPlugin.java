package net.modificationstation.stationapi.api.client.texture.plugin;

import net.minecraft.class_556;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class OverlayRendererPlugin {

    protected final class_556 overlayRenderer;

    public OverlayRendererPlugin(class_556 overlayRenderer) {
        this.overlayRenderer = overlayRenderer;
    }

    public void renderItem3D(Living entity, ItemInstance item, CallbackInfo ci) {}
}
