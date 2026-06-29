package net.modificationstation.sltest.render;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.client.event.render.model.BlockStateReloadEvent;
import net.modificationstation.stationapi.api.client.event.render.model.UnbakedModelLoadingFinishedEvent;
import net.modificationstation.stationapi.api.util.Identifier;

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
}
