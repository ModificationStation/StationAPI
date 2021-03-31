package net.modificationstation.stationapi.api.client.event.model;

import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.client.model.BlockModelProvider;
import net.modificationstation.stationapi.api.common.event.Event;

// TODO: Item and Entity model documentation.

/**
 * Used to set a custom model for your block.
 * Implement {@link BlockModelProvider} in your block class to use a custom model.
 *
 * @author mine_diver
 * @see BlockModelProvider
 */
@RequiredArgsConstructor
public class ModelRegisterEvent extends Event {

    public final Type type;

    public enum Type {
        BLOCKS,
        ITEMS,
        ENTITIES
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
