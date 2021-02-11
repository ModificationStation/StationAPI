package net.modificationstation.stationapi.api.client.event.gui;

import lombok.RequiredArgsConstructor;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.common.event.Event;

@RequiredArgsConstructor
public class RenderItemOverlay extends Event {

    public final int itemX, itemY;
    public final ItemInstance itemInstance;
    public final TextRenderer textRenderer;
    public final TextureManager textureManager;
    public final ItemRenderer itemRenderer;
}
