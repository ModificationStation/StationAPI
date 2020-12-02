package net.modificationstation.stationloader.api.client.event.model;

import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

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

    SimpleEvent<ModelRegister> EVENT = new SimpleEvent<>(ModelRegister.class, (listeners) ->
            (type) -> {
                for (ModelRegister event : listeners)
                    event.registerModels(type);
            });

    void registerModels(Type type);
}
