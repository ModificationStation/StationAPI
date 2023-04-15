package net.modificationstation.sltest;

import net.fabricmc.api.ModInitializer;

public class MainTest implements ModInitializer {
    @Override
    public void onInitialize() {
//        new Exception().printStackTrace();
    }

//    @EventListener
//    public void onInitialize(TagRegisterEvent event) {
//        SLTest.LOGGER.info("==================================================================================================");
//        Identifier oreDictToTest = Identifier.of("items/tools/pickaxes/");
//        SLTest.LOGGER.info(oreDictToTest);
//        Optional<Map<Identifier, List<TagEntry>>> predicates = TagRegistry.INSTANCE.getWithIdentifiers(oreDictToTest);
//        if (predicates.isPresent()) {
//            for (Identifier oreDictEntryObject : predicates.get().keySet()) {
//                SLTest.LOGGER.info(oreDictEntryObject);
//            }
//        }
//        else {
//            throw new RuntimeException("Predicates are empty for " + oreDictToTest);
//        }
//        SLTest.LOGGER.info("==================================================================================================");
//        block.mineableByTool("pickaxe*", 0);
//    }


}
