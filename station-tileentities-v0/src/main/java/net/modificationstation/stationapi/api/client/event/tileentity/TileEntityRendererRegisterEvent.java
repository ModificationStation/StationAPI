package net.modificationstation.stationapi.api.client.event.tileentity;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntityBase;

import java.util.*;

@RequiredArgsConstructor
public class TileEntityRendererRegisterEvent extends Event {

    public final Map<Class<? extends TileEntityBase>, TileEntityRenderer> renderers;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
