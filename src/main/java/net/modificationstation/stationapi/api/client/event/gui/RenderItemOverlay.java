package net.modificationstation.stationapi.api.client.event.gui;

import lombok.Getter;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.common.event.GameEvent;

import java.util.function.Consumer;

public interface RenderItemOverlay {

    GameEvent<RenderItemOverlay> EVENT = new GameEvent<>(RenderItemOverlay.class,
            (listeners) ->
                    (itemRenderer, itemX, itemY, itemInstance, textRenderer, textureManager) -> {
                        for (RenderItemOverlay listener : listeners)
                            listener.renderItemOverlay(itemRenderer, itemX, itemY, itemInstance, textRenderer, textureManager);
                    },
            (Consumer<GameEvent<RenderItemOverlay>>) keyPressed ->
                    keyPressed.register((itemRenderer, itemX, itemY, itemInstance, textRenderer, textureManager) -> GameEvent.EVENT_BUS.post(new Data(itemRenderer, itemX, itemY, itemInstance, textRenderer, textureManager)))
    );

    void renderItemOverlay(ItemRenderer itemRenderer, int itemX, int itemY, ItemInstance itemInstance, TextRenderer textRenderer, TextureManager textureManager);

    @Getter
    final class Data extends GameEvent.Data<RenderItemOverlay> {
        private final int itemX, itemY;
        private final ItemInstance itemInstance;
        private final TextRenderer textRenderer;
        private final TextureManager textureManager;
        private final ItemRenderer itemRenderer;

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
