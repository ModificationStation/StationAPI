package net.modificationstation.stationloader.api.client.event.texture;

import net.modificationstation.stationloader.api.client.texture.TextureFactory;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

/**
 * Used to register your mod textures.
 * Implement this in the class you plan to use to load your textures, and then override registerTextures().
 * All events need to be registered in your mod's preInit method using TextureRegister.EVENT.register(yourInstantiatedClass).
 *
 * @see TextureFactory
 *
 * @author mine_diver
 */
public interface TextureRegister {

    SimpleEvent<TextureRegister> EVENT = new SimpleEvent<>(TextureRegister.class, (listeners) ->
            () -> {
                for (TextureRegister event : listeners)
                    event.registerTextures();
            });

    /**
     * Override this and put your texture registering code here.
     */
    void registerTextures();
}
