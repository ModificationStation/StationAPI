package net.modificationstation.stationloader.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationloader.client.texture.TextureRegistry.GUI_ITEMS;

@Mixin(ItemRenderer.class)
@Environment(EnvType.CLIENT)
public class MixinItemRenderer {

    @Redirect(method = {"render(Lnet/minecraft/entity/Item;DDDFF)V", "method_1487(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/item/ItemInstance;II)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemInstance;method_725()I"))
    private int itemTexture(ItemInstance itemInstance) {
        int texID = itemInstance.method_725();
        atlasToBind = texID / GUI_ITEMS.texturesPerFile();
        return texID % GUI_ITEMS.texturesPerFile();
    }

    @Redirect(method = "render(Lnet/minecraft/entity/Item;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/ItemRenderer;bindTexture(Ljava/lang/String;)V", ordinal = 2))
    private void rebindItemTexture(ItemRenderer itemRenderer, String string) {
        GUI_ITEMS.bindAtlas(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager, atlasToBind);
    }

    @Redirect(method = "method_1486(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;IIIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(I)V", ordinal = 2))
    private void rebindItemTexture(TextureManager textureManager, int i) {
        GUI_ITEMS.bindAtlas(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager, atlasToBind);
    }

    private int atlasToBind;
}
