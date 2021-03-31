package net.modificationstation.stationapi.api.client.event.texture;

import net.modificationstation.stationapi.api.client.texture.TextureFactory;
import net.modificationstation.stationapi.api.common.event.Event;

/**
 * Used to register your mod textures.
 * Implement this in the class you plan to use to load your textures, and then override registerTextures().
 * All events need to be registered in your mod's preInit method using TextureRegister.EVENT.register(yourInstantiatedClass).
 *
 * @author mine_diver
 * @see TextureFactory
 */
public class TextureRegisterEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
