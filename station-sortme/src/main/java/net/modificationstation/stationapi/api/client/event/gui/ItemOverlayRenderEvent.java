package net.modificationstation.stationapi.api.client.event.gui;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;

@RequiredArgsConstructor
public class ItemOverlayRenderEvent extends Event {

    public final int itemX, itemY;
    public final ItemInstance itemInstance;
    public final TextRenderer textRenderer;
    public final TextureManager textureManager;
    public final ItemRenderer itemRenderer;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
