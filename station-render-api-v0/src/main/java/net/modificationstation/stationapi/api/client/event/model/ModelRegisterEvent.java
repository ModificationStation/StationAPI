package net.modificationstation.stationapi.api.client.event.model;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.model.BlockInventoryModelProvider;

// TODO: Item and Entity model documentation.

/**
 * Used to set a custom model for your block.
 * Implement {@link BlockInventoryModelProvider} in your block class to use a custom model.
 *
 * @author mine_diver
 * @see BlockInventoryModelProvider
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
