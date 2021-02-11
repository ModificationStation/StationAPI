package net.modificationstation.stationapi.api.client.event.model;

import net.modificationstation.stationapi.api.client.model.BlockModelProvider;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

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

    GameEventOld<ModelRegister> EVENT = new GameEventOld<>(ModelRegister.class,
            listeners ->
                    type -> {
                        for (ModelRegister listener : listeners)
                            listener.registerModels(type);
                    },
            (Consumer<GameEventOld<ModelRegister>>) modelRegister ->
                    modelRegister.register(type -> GameEventOld.EVENT_BUS.post(new Data(type)))
    );

    void registerModels(Type type);

    enum Type {

        BLOCKS,
        ITEMS,
        ENTITIES
    }

    final class Data extends GameEventOld.Data<ModelRegister> {

        public final Type type;

        private Data(Type type) {
            super(EVENT);
            this.type = type;
        }
    }
}
