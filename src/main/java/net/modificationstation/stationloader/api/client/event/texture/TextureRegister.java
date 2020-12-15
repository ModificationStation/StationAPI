package net.modificationstation.stationloader.api.client.event.texture;

import net.modificationstation.stationloader.api.client.texture.TextureFactory;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.function.Consumer;

/**
 * Used to register your mod textures.
 * Implement this in the class you plan to use to load your textures, and then override registerTextures().
 * All events need to be registered in your mod's preInit method using TextureRegister.EVENT.register(yourInstantiatedClass).
 *
 * @author mine_diver
 * @see TextureFactory
 */
public interface TextureRegister {

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<TextureRegister> EVENT = new SimpleEvent<>(TextureRegister.class,
            listeners ->
                    () -> {
                        for (TextureRegister listener : listeners)
                            listener.registerTextures();
                    },
            (Consumer<SimpleEvent<TextureRegister>>) textureRegister ->
                    textureRegister.register(() -> SimpleEvent.EVENT_BUS.post(new Data()))
    );

    /**
     * Override this and put your texture registering code here.
     */
    void registerTextures();

    final class Data extends SimpleEvent.Data<TextureRegister> {

        private Data() {
            super(EVENT);
        }
    }
}
