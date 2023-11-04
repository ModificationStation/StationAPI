package net.modificationstation.stationapi.api.client.event.render.model;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.util.Identifier;

@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public final class LoadUnbakedModelEvent extends Event {
    public final Identifier identifier;
    public final ModelLoader modelLoader;
    public UnbakedModel model;
}
