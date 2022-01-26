package net.modificationstation.stationapi.mixin.render.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_556;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.texture.plugin.OverlayRendererPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.RenderPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_556.class)
@Environment(EnvType.CLIENT)
public class Mixinclass_556 {

    private final OverlayRendererPlugin plugin = RenderPlugin.PLUGIN.createOverlayRenderer((class_556) (Object) this);

    @Inject(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderItem3D(Living entity, ItemInstance item, CallbackInfo ci) {
        plugin.renderItem3D(entity, item, ci);
    }
}
