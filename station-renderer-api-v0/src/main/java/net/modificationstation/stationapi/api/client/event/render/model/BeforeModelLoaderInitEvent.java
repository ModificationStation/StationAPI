package net.modificationstation.stationapi.api.client.event.render.model;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.color.block.BlockColors;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Map;

@SuperBuilder
public final class BeforeModelLoaderInitEvent extends Event {
    public final ModelLoader modelLoader;
    public final Map<Identifier, UnbakedModel> unbakedModels;
    public final Map<Identifier, UnbakedModel> modelsToBake;
    public final Map<Identifier, List<ModelLoader.SourceTrackedData>> blockStates;
    public final BlockColors blockColors;
}
