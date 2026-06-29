package net.modificationstation.stationapi.api.client.event.render.model;

import com.mojang.datafixers.util.Pair;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.JsonHelper;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

/**
 * An event which is fired when block states are being reloaded
 */
@EventPhases(StationAPI.INTERNAL_PHASE)
public class BlockStateReloadEvent extends Event {
    public final ResourceManager resourceManager;
    public final Executor executor;
    public final List<CompletableFuture<Pair<Identifier, List<ModelLoader.SourceTrackedData>>>> states;

    public BlockStateReloadEvent(ResourceManager resourceManager, Executor executor, List<CompletableFuture<Pair<Identifier, List<ModelLoader.SourceTrackedData>>>> states) {
        this.resourceManager = resourceManager;
        this.executor = executor;
        this.states = states;
    }

    /**
     * Adds a blockstate json to the list of blockstates to be loaded
     *
     * @param blockIdentifier The {@link Identifier} of the block
     * @param jsons           A string representation of the blockstate json to be deserialized
     */
    public void addBlockstateJsonString(Identifier blockIdentifier, ArrayList<String> jsons) {
        Identifier id = Identifier.of(blockIdentifier.namespace + ":stationapi/blockstates/" + blockIdentifier.path + ".json");
        
        states.add(CompletableFuture.supplyAsync(() -> {
            List<ModelLoader.SourceTrackedData> statesList = new ArrayList<>(jsons.size());
            for (String jsonString : jsons)
                try {
                    statesList.add(new ModelLoader.SourceTrackedData("Default", JsonHelper.deserialize(jsonString)));
                } catch (Exception exception) {
                    LOGGER.error("Failed to load blockstate {} from pack {}", blockIdentifier, "Default", exception);
                }
            return Pair.of(id, statesList);
        }, executor));
    }

    /**
     * Adds a blockstate json to the list of blockstates to be loaded
     *
     * @param blockIdentifier The {@link Identifier} of the block
     * @param json        Resource to be read and deserialized
     */
    public void addBlockstateJsonString(Identifier blockIdentifier, String json) {
        addBlockstateJsonString(blockIdentifier, new ArrayList<>(List.of(json)));
    }

    /**
     * Adds a resource to the list of blockstates to be loaded
     *
     * @param blockIdentifier The {@link Identifier} of the block
     * @param resources       Resources to be read and deserialized
     */
    public void addBlockstateJsonResource(Identifier blockIdentifier, ArrayList<Resource> resources) {
        Identifier id = Identifier.of(blockIdentifier.namespace + ":stationapi/blockstates/" + blockIdentifier.path + ".json");
        
        states.add(CompletableFuture.supplyAsync(() -> {
            List<ModelLoader.SourceTrackedData> list2 = new ArrayList<>(resources.size());
            for (Resource resource : resources)
                try (BufferedReader reader = resource.getReader()) {
                    list2.add(new ModelLoader.SourceTrackedData("Default", JsonHelper.deserialize(reader)));
                } catch (Exception exception) {
                    LOGGER.error("Failed to load blockstate {} from pack {}", blockIdentifier, "Default", exception);
                }
            return Pair.of(id, list2);
        }, executor));
    }

    /**
     * Adds a resource to the list of blockstates to be loaded
     *
     * @param blockIdentifier The {@link Identifier} of the block
     * @param resource        Resource to be read and deserialized
     */
    public void addBlockStateJsonResource(Identifier blockIdentifier, Resource resource) {
        addBlockstateJsonResource(blockIdentifier, new ArrayList<>(List.of(resource)));
    }
}
