package net.modificationstation.sltest.tileentity;

import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.tileentity.TileEntityRegisterEvent;

public class TileEntityListener {

    public TileEntityListener() {
        System.out.println("tile entities?");
    }

    @EventListener
    public void registerTileEntities(TileEntityRegisterEvent event) {
        System.out.println("reeee tile entiites");
        new Exception().printStackTrace();
        event.register(TileEntityFreezer.class, "sltest:freezer");
    }
}
