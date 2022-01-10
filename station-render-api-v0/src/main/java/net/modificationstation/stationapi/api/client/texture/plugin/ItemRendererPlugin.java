package net.modificationstation.stationapi.api.client.texture.plugin;

import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Item;
import net.minecraft.item.ItemInstance;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ItemRendererPlugin {

    protected final ItemRenderer itemRenderer;

    public ItemRendererPlugin(ItemRenderer itemRenderer) {
        this.itemRenderer = itemRenderer;
    }

    public void render(Item item, double x, double y, double z, float rotation, float delta, CallbackInfo ci) {}

    public void renderItemOnGui(TextRenderer textRenderer, TextureManager textureManager, ItemInstance itemInstance, int x, int y, CallbackInfo ci) {}

    public void renderItemOnGui(TextRenderer textRenderer, TextureManager textureManager, int id, int damage, int texture, int x, int y, CallbackInfo ci) {}

    public void renderItemQuad(int x, int y, int textureX, int textureY, int width, int height, CallbackInfo ci) {}
}
