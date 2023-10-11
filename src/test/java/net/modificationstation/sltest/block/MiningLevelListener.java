package net.modificationstation.sltest.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.block.MiningLevels;
import net.modificationstation.stationapi.api.event.block.MiningLevelRegisterEvent;

public class MiningLevelListener {
    @EventListener
    public void registerMiningLevels(MiningLevelRegisterEvent event){
        event.insertMiningLevelAfter(SLTest.MODID.id("bronze"), MiningLevels.STONE);
        event.insertMiningLevelAfter(SLTest.MODID.id("cobalt"), MiningLevels.DIAMOND);
        event.insertMiningLevelAfter(SLTest.MODID.id("obsidian"), MiningLevels.DIAMOND);
        event.addMiningLevel(SLTest.MODID.id("ultra"), 9000);
        event.insertMiningLevelAfter(SLTest.MODID.id("ultraplus"), SLTest.MODID.id("ultra"));
        MiningLevels.addMiningLevelBefore(SLTest.MODID.id("negative_wood"), MiningLevels.WOOD);
        MiningLevels.insertMiningLevelBefore(SLTest.MODID.id("pre-wood"), MiningLevels.WOOD);
    }
}
