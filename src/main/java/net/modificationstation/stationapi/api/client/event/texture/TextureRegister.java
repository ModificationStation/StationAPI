package net.modificationstation.stationapi.api.client.event.texture;

import net.modificationstation.stationapi.api.client.texture.TextureFactory;
import net.modificationstation.stationapi.api.common.event.GameEvent;

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

    GameEvent<TextureRegister> EVENT = new GameEvent<>(TextureRegister.class,
            listeners ->
                    () -> {
                        for (TextureRegister listener : listeners)
                            listener.registerTextures();
                    },
            (Consumer<GameEvent<TextureRegister>>) textureRegister ->
                    textureRegister.register(() -> GameEvent.EVENT_BUS.post(new Data()))
    );

    /**
     * Override this and put your texture registering code here.
     */
    void registerTextures();

    final class Data extends GameEvent.Data<TextureRegister> {

        private Data() {
            super(EVENT);
        }
    }
}
