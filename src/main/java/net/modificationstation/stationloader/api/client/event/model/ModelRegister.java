package net.modificationstation.stationloader.api.client.event.model;

import lombok.Getter;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.common.event.GameEvent;

import java.util.function.Consumer;

// TODO: Item and Entity model documentation.

/**
 * Used to set a custom model for your block.
 * Implement {@link BlockModelProvider} in your block class to use a custom model.
 *
 * @author mine_diver
 * @see BlockModelProvider
 */
public interface ModelRegister {

    @SuppressWarnings("UnstableApiUsage")
    GameEvent<ModelRegister> EVENT = new GameEvent<>(ModelRegister.class,
            listeners ->
                    (type) -> {
                        for (ModelRegister listener : listeners)
                            listener.registerModels(type);
                    },
            (Consumer<GameEvent<ModelRegister>>) modelRegister ->
                    modelRegister.register(type -> GameEvent.EVENT_BUS.post(new Data(type)))
    );

    void registerModels(Type type);

    enum Type {

        BLOCKS,
        ITEMS,
        ENTITIES
    }

    final class Data extends GameEvent.Data<ModelRegister> {

        @Getter
        private final Type type;

        private Data(Type type) {
            super(EVENT);
            this.type = type;
        }
    }
}
