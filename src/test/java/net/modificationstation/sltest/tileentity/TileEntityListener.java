package net.modificationstation.sltest.tileentity;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.event.tileentity.TileEntityRegisterEvent;

public class TileEntityListener {

    @EventListener
    public void registerTileEntities(TileEntityRegisterEvent event) {
        SLTest.LOGGER.info("reeee tile entiites");
        event.register(TileEntityFreezer.class, "sltest:freezer");
    }
}
