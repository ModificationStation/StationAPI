package net.modificationstation.stationapi.api.client.event.render.entity;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;

import java.util.*;

@RequiredArgsConstructor
public class EntityRendererRegisterEvent extends Event {

    public final Map<Class<? extends EntityBase>, EntityRenderer> renderers;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
