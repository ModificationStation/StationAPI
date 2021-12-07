package net.modificationstation.sltest;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.oredict.OreDictRegisterEvent;
import net.modificationstation.stationapi.api.oredict.OreDict;
import net.modificationstation.stationapi.impl.oredict.OreDictEntryObject;

public class MainTest {

    @EventListener
    public void onInitialize(OreDictRegisterEvent event) {
        SLTest.LOGGER.info("==================================================================================================");
        String oreDictToTest = "pickaxe*";
        SLTest.LOGGER.info(oreDictToTest.substring(0, oreDictToTest.length()-1));
        for (OreDictEntryObject oreDictEntryObject : OreDict.INSTANCE.getOreDictEntryObjects(oreDictToTest)) {
            SLTest.LOGGER.info(oreDictEntryObject.identifier.toString());
        }
        SLTest.LOGGER.info("==================================================================================================");
        //block.mineableByTool("pickaxe*", 0);
    }


}
