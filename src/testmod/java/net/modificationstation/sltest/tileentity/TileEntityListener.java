package net.modificationstation.sltest.tileentity;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.tileentity.TileEntityRegisterEvent;

public class TileEntityListener {

    public TileEntityListener() {
        SLTest.LOGGER.info("tile entities?");
    }

    @EventListener
    public void registerTileEntities(TileEntityRegisterEvent event) {
        SLTest.LOGGER.info("reeee tile entiites");
        new Exception().printStackTrace();
        event.register(TileEntityFreezer.class, "sltest:freezer");
    }
}
