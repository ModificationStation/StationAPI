package net.modificationstation.sltest;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.tags.TagRegisterEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagEntry;
import net.modificationstation.stationapi.api.tags.TagRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MainTest {

    @EventListener
    public void onInitialize(TagRegisterEvent event) {
        SLTest.LOGGER.info("==================================================================================================");
        Identifier oreDictToTest = Identifier.of("items/tools/pickaxes/");
        SLTest.LOGGER.info(oreDictToTest);
        Optional<Map<Identifier, List<TagEntry>>> predicates = TagRegistry.INSTANCE.getWithIdentifiers(oreDictToTest);
        if (predicates.isPresent()) {
            for (Identifier oreDictEntryObject : predicates.get().keySet()) {
                SLTest.LOGGER.info(oreDictEntryObject);
            }
        }
        else {
            throw new RuntimeException("Predicates are empty for " + oreDictToTest);
        }
        SLTest.LOGGER.info("==================================================================================================");
        //block.mineableByTool("pickaxe*", 0);
    }


}
