package net.modificationstation.stationapi.api.client.event.gui;

import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface RenderItemOverlay {

    GameEventOld<RenderItemOverlay> EVENT = new GameEventOld<>(RenderItemOverlay.class,
            listeners ->
                    (itemRenderer, itemX, itemY, itemInstance, textRenderer, textureManager) -> {
                        for (RenderItemOverlay listener : listeners)
                            listener.renderItemOverlay(itemRenderer, itemX, itemY, itemInstance, textRenderer, textureManager);
                    },
            (Consumer<GameEventOld<RenderItemOverlay>>) keyPressed ->
                    keyPressed.register((itemRenderer, itemX, itemY, itemInstance, textRenderer, textureManager) -> GameEventOld.EVENT_BUS.post(new Data(itemRenderer, itemX, itemY, itemInstance, textRenderer, textureManager)))
    );

    void renderItemOverlay(ItemRenderer itemRenderer, int itemX, int itemY, ItemInstance itemInstance, TextRenderer textRenderer, TextureManager textureManager);

    final class Data extends GameEventOld.Data<RenderItemOverlay> {

        public final int itemX, itemY;
        public final ItemInstance itemInstance;
        public final TextRenderer textRenderer;
        public final TextureManager textureManager;
        public final ItemRenderer itemRenderer;

        private Data(ItemRenderer itemRenderer, int itemX, int itemY, ItemInstance itemInstance, TextRenderer textRenderer, TextureManager textureManager) {
            super(EVENT);
            this.itemRenderer = itemRenderer;
            this.itemX = itemX;
            this.itemY = itemY;
            this.itemInstance = itemInstance;
            this.textRenderer = textRenderer;
            this.textureManager = textureManager;
        }
    }

}
