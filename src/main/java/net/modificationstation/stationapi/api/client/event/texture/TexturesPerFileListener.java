package net.modificationstation.stationapi.api.client.event.texture;

import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.common.event.Event;
import net.modificationstation.stationapi.api.common.event.GameEventOld;
import net.modificationstation.stationapi.impl.client.texture.TextureRegistry;

import java.util.function.Consumer;

/**
 * Event called when TexturesPerFile of a texture registry got changed, so mods can perform actions on change
 * <p>
 * args: TextureRegistry
 * return: void
 *
 * @author mine_diver
 */
@RequiredArgsConstructor
public class TexturesPerFileListener extends Event {
    public final TextureRegistry registry;
}

