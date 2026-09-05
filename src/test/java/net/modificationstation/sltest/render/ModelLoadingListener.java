package net.modificationstation.sltest.render;

import com.mojang.datafixers.util.Pair;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.client.event.render.model.BeforeModelLoaderInitEvent;
import net.modificationstation.stationapi.api.client.event.render.model.ModelVariantMapOverrideEvent;
import net.modificationstation.stationapi.api.client.event.render.model.UnbakedModelLoadingFinishedEvent;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.render.model.json.ModelVariantMap;
import net.modificationstation.stationapi.api.event.resource.RuntimeResourcesEvent;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

public class ModelLoadingListener {
    @EventListener
    public void modelsLoaded(UnbakedModelLoadingFinishedEvent event) {
        SLTest.LOGGER.info(event.unbakedModels.size() + " models loaded");
    }

    String blockstateJson = ("""
            {
              "variants": {
                "": {"model": "sltest:block/test_block"}
              }
            }"""
    );

    String dirtBlockstateJson = ("""
            {
              "variants": {
                "": {"model": "sltest:block/dirt"}
              }
            }"""
    );

    @EventListener
    public void injectBlockStates(RuntimeResourcesEvent.Assets event) {
        event.with(ModelLoader.BLOCK_STATES_FINDER)
                .add(Identifier.of("minecraft:sponge"), blockstateJson)
                .add(Identifier.of("minecraft:dirt"), dirtBlockstateJson);
    }

    @EventListener
    public void beforeLoader(BeforeModelLoaderInitEvent event) {
        SLTest.LOGGER.info("Model Loader Init");
        logBlockStateSources(event, Identifier.of("minecraft:sponge"));
        logBlockStateSources(event, Identifier.of("minecraft:dirt"));
    }

    private static void logBlockStateSources(BeforeModelLoaderInitEvent event, Identifier block) {
        List<ModelLoader.SourceTrackedData> sources = event.blockStates.get(ModelLoader.BLOCK_STATES_FINDER.toResourcePath(block));
        SLTest.LOGGER.info("blockstate {} has {} definition(s) layered", block, sources == null ? 0 : sources.size());
        if (sources != null) for (int i = 0; i < sources.size(); i++)
            SLTest.LOGGER.info("  layer {}: {}", i, sources.get(i).data());
    }

    String blockstateVariantJson = ("""
            {
              "variants": {
                "": {"model": "sltest:block/test_block"}
              }
            }"""
    );

    @EventListener
    public void mineLdiver(ModelVariantMapOverrideEvent event) {
        if (event.id.equals(Identifier.of("minecraft:note_block"))) {
            BufferedReader reader = new BufferedReader(new StringReader(blockstateVariantJson));
            event.addModelVariantMap(SLTest.NAMESPACE, ModelVariantMap.fromJson(event.deserializationContext, reader));
        }

        System.err.println(event.id);
        for (Pair<String, ModelVariantMap> variantMap : event.variantMaps) {
            System.err.println(" - " + variantMap.getFirst() + " | " + variantMap.getSecond().getVariantMap().toString());
        }
    }
}
