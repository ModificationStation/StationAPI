package net.modificationstation.sltest.render;

import com.mojang.datafixers.util.Pair;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.client.event.render.model.BeforeModelLoaderInitEvent;
import net.modificationstation.stationapi.api.client.event.render.model.BlockStateReloadEvent;
import net.modificationstation.stationapi.api.client.event.render.model.ModelVariantMapOverrideEvent;
import net.modificationstation.stationapi.api.client.event.render.model.UnbakedModelLoadingFinishedEvent;
import net.modificationstation.stationapi.api.client.render.model.json.ModelVariantMap;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.BufferedReader;
import java.io.StringReader;

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
    
    @EventListener
    public void injectBlockState(BlockStateReloadEvent event) {
        event.addBlockstateJsonString(Identifier.of("minecraft:sponge"), blockstateJson);
    }
    
    @EventListener
    public void beforeLoader(BeforeModelLoaderInitEvent event) {
        SLTest.LOGGER.info("Model Loader Init");
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
