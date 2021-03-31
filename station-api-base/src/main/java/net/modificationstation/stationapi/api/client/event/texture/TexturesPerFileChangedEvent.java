package net.modificationstation.stationapi.api.client.event.texture;

import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.common.event.Event;
import net.modificationstation.stationapi.impl.client.texture.TextureRegistry;

/**
 * Event called when TexturesPerFile of a texture registry got changed, so mods can perform actions on change
 * <p>
 * args: TextureRegistry
 * return: void
 *
 * @author mine_diver
 */
@RequiredArgsConstructor
public class TexturesPerFileChangedEvent extends Event {

    public final TextureRegistry registry;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}

