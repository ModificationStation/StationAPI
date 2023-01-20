package net.modificationstation.stationapi.api.client.event.render.model;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.registry.Identifier;

@SuperBuilder
public final class LoadUnbakedModelEvent extends Event {

    public final Identifier identifier;
    public UnbakedModel model;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
