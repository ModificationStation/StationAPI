package net.modificationstation.stationloader.api.client.event.model;

import lombok.Getter;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.function.Consumer;

// TODO: Item and Entity model documentation.
/**
 * Used to set a custom model for your block.
 * Implement {@link BlockModelProvider} in your block class to use a custom model.
 *
 * @see BlockModelProvider
 *
 * @author mine_diver
 */
public interface ModelRegister {

    enum Type {

        BLOCKS,
        ITEMS,
        ENTITIES
    }

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<ModelRegister> EVENT = new SimpleEvent<>(ModelRegister.class,
            listeners ->
                    (type) -> {
        for (ModelRegister listener : listeners)
            listener.registerModels(type);
    }, (Consumer<SimpleEvent<ModelRegister>>) modelRegister ->
            modelRegister.register(type -> SimpleEvent.EVENT_BUS.post(new Data(type)))
    );

    void registerModels(Type type);

    final class Data extends SimpleEvent.Data<ModelRegister> {

        private Data(Type type) {
            super(EVENT);
            this.type = type;
        }

        @Getter
        private final Type type;
    }
}
