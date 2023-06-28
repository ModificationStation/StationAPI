package net.modificationstation.stationapi.api.client.event.gui;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.StationAPI;

@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class ItemOverlayRenderEvent extends Event {
    public final int itemX, itemY;
    public final ItemInstance itemInstance;
    public final TextRenderer textRenderer;
    public final TextureManager textureManager;
    public final ItemRenderer itemRenderer;
}
