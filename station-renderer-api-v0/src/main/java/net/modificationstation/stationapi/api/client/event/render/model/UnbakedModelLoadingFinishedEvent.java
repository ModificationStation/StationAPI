package net.modificationstation.stationapi.api.client.event.render.model;

import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.color.block.BlockColors;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Map;

@EventPhases(StationAPI.INTERNAL_PHASE)
public class UnbakedModelLoadingFinishedEvent extends Event {
    public ModelLoader modelLoader;
    public Map<Identifier, UnbakedModel> unbakedModels;
    public Map<Identifier, UnbakedModel> modelsToBake;
    public Map<Identifier, List<ModelLoader.SourceTrackedData>> blockStates;
    public BlockColors blockColors;
    
    public UnbakedModelLoadingFinishedEvent(ModelLoader modelLoader, Map<Identifier, UnbakedModel> unbakedModels, Map<Identifier, UnbakedModel> modelsToBake, Map<Identifier, List<ModelLoader.SourceTrackedData>> blockStates, BlockColors blockColors) {
        this.modelLoader = modelLoader;
        this.unbakedModels = unbakedModels;
        this.modelsToBake = modelsToBake;
        this.blockStates = blockStates;
        this.blockColors = blockColors;
    }
}
