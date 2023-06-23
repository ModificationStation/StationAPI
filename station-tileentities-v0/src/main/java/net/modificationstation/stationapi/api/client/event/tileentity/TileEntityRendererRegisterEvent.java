package net.modificationstation.stationapi.api.client.event.tileentity;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntityBase;

import java.util.Map;

@SuperBuilder
public class TileEntityRendererRegisterEvent extends Event {
    public final Map<Class<? extends TileEntityBase>, TileEntityRenderer> renderers;
}
