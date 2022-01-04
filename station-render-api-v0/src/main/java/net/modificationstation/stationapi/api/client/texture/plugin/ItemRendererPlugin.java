package net.modificationstation.stationapi.api.client.texture.plugin;

import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.entity.Item;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ItemRendererPlugin {

    protected final ItemRenderer itemRenderer;

    public ItemRendererPlugin(ItemRenderer itemRenderer) {
        this.itemRenderer = itemRenderer;
    }

    public void render(Item item, double x, double y, double z, float rotation, float delta, CallbackInfo ci) {}
}
