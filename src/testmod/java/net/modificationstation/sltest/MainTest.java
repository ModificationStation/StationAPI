package net.modificationstation.sltest;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.event.oredict.OreDictRegisterEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagRegistry;

import java.util.*;
import java.util.function.*;

public class MainTest {

    @EventListener
    public void onInitialize(OreDictRegisterEvent event) {
        SLTest.LOGGER.info("==================================================================================================");
        Identifier oreDictToTest = Identifier.of("items/tools/pickaxes/");
        SLTest.LOGGER.info(oreDictToTest);
        Optional<Map<Identifier, List<Predicate<ItemInstance>>>> predicates = TagRegistry.INSTANCE.getWithIdentifiers(oreDictToTest);
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
