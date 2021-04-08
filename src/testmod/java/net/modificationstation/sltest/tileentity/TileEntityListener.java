package net.modificationstation.sltest.tileentity;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.tileentity.TileEntityRegisterEvent;

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
