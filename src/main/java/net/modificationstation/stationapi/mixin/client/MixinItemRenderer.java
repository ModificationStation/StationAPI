package net.modificationstation.stationapi.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.event.gui.ItemOverlayRenderEvent;
import net.modificationstation.stationapi.api.client.texture.TextureRegistry;
import net.modificationstation.stationapi.api.common.StationAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class MixinItemRenderer extends EntityRenderer {

    @Shadow protected abstract void method_1485(Tessellator arg, int i, int j, int k, int i1, int j1);

    private int atlasToBind;

    @Redirect(method = {
            "render(Lnet/minecraft/entity/Item;DDDFF)V",
            "method_1487(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/item/ItemInstance;II)V"
    }, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemInstance;getTexturePosition()I"))
    private int itemTexture(ItemInstance itemInstance) {
        TextureRegistry gui_items = TextureRegistry.getRegistry(TextureRegistry.Vanilla.GUI_ITEMS);
        int texID = itemInstance.getTexturePosition();
        atlasToBind = texID / gui_items.texturesPerFile();
        return texID % gui_items.texturesPerFile();
    }

    @Redirect(method = "render(Lnet/minecraft/entity/Item;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/ItemRenderer;bindTexture(Ljava/lang/String;)V", ordinal = 0))
    private void rebindBlockTexture1(ItemRenderer itemRenderer, String string) {
        TextureRegistry.getRegistry(TextureRegistry.Vanilla.TERRAIN).bindAtlas(dispatcher.textureManager, atlasToBind);
    }

    @Redirect(method = "render(Lnet/minecraft/entity/Item;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/ItemRenderer;bindTexture(Ljava/lang/String;)V", ordinal = 1))
    private void rebindBlockTexture2(ItemRenderer itemRenderer, String string) {
        TextureRegistry.getRegistry(TextureRegistry.Vanilla.TERRAIN).bindAtlas(dispatcher.textureManager, atlasToBind);
    }

    @Redirect(method = "render(Lnet/minecraft/entity/Item;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/ItemRenderer;bindTexture(Ljava/lang/String;)V", ordinal = 2))
    private void rebindItemTexture(ItemRenderer itemRenderer, String string) {
        TextureRegistry.getRegistry(TextureRegistry.Vanilla.GUI_ITEMS).bindAtlas(dispatcher.textureManager, atlasToBind);
    }

    @Redirect(method = "renderItemOnGui(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;IIIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(I)V", ordinal = 1))
    private void rebindBlockTexture(TextureManager textureManager, int i) {
        TextureRegistry.getRegistry(TextureRegistry.Vanilla.TERRAIN).bindAtlas(textureManager, atlasToBind);
    }

    @Redirect(method = "renderItemOnGui(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;IIIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(I)V", ordinal = 2))
    private void rebindItemTexture(TextureManager textureManager, int i) {
        TextureRegistry.getRegistry(TextureRegistry.Vanilla.GUI_ITEMS).bindAtlas(textureManager, atlasToBind);
    }

    @Inject(method = "method_1488(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/item/ItemInstance;II)V", at = @At(value = "RETURN"))
    private void fancyItemOverlays(TextRenderer arg, TextureManager arg1, ItemInstance item, int i, int j, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new ItemOverlayRenderEvent(i, j, item, arg, arg1, (ItemRenderer) (Object) this));
    }
}
