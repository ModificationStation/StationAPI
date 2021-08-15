package net.modificationstation.stationapi.api.client.event.texture;

import net.mine_diver.unsafeevents.Event;

/**
 * Used to register your mod textures.
 *
 * @author mine_diver
 */
public class TextureRegisterEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
