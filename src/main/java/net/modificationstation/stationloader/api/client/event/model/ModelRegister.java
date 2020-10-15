package net.modificationstation.stationloader.api.client.event.model;

import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

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

    Event<ModelRegister> EVENT = EventFactory.INSTANCE.newEvent(ModelRegister.class, (listeners) ->
            (type) -> {
                for (ModelRegister event : listeners)
                    event.registerModels(type);
            });

    void registerModels(Type type);
}
