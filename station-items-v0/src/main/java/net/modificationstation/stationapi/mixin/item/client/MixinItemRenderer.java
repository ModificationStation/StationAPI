package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.ItemOverlayRenderEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Inject(method = "method_1488(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/item/ItemInstance;II)V", at = @At(value = "RETURN"))
    private void fancyItemOverlays(TextRenderer arg, TextureManager arg1, ItemStack item, int i, int j, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                ItemOverlayRenderEvent.builder()
                        .itemX(i).itemY(j)
                        .itemInstance(item)
                        .textRenderer(arg)
                        .textureManager(arg1)
                        .itemRenderer((ItemRenderer) (Object) this)
                        .build()
        );
    }
}
