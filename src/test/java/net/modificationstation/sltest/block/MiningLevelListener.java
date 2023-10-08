package net.modificationstation.sltest.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.block.MiningLevels;
import net.modificationstation.stationapi.api.event.block.MiningLevelRegisterEvent;

public class MiningLevelListener {
    @EventListener
    public void registerMiningLevels(MiningLevelRegisterEvent event){
        MiningLevels.addMiningLevel(SLTest.MODID.id("obsidian"), 4);
    }
}
