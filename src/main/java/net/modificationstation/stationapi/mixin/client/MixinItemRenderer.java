package net.modificationstation.stationapi.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.texture.TextureRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class MixinItemRenderer extends EntityRenderer {

    private int atlasToBind;

    @Redirect(method = {
            "render(Lnet/minecraft/entity/Item;DDDFF)V",
            "method_1487(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/item/ItemInstance;II)V"
    }, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemInstance;method_725()I"))
    private int itemTexture(ItemInstance itemInstance) {
        TextureRegistry gui_items = TextureRegistry.getRegistry(TextureRegistry.Vanilla.GUI_ITEMS);
        int texID = itemInstance.method_725();
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

    @Redirect(method = "method_1486(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;IIIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(I)V", ordinal = 1))
    private void rebindBlockTexture(TextureManager textureManager, int i) {
        TextureRegistry.getRegistry(TextureRegistry.Vanilla.TERRAIN).bindAtlas(textureManager, atlasToBind);
    }

    @Redirect(method = "method_1486(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;IIIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(I)V", ordinal = 2))
    private void rebindItemTexture(TextureManager textureManager, int i) {
        TextureRegistry.getRegistry(TextureRegistry.Vanilla.GUI_ITEMS).bindAtlas(textureManager, atlasToBind);
    }
}
