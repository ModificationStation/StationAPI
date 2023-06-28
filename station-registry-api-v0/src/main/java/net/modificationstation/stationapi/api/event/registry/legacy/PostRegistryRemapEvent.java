package net.modificationstation.stationapi.api.event.registry.legacy;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.registry.legacy.LevelLegacyRegistry;

/**
 * An event that's fired after all {@link LevelLegacyRegistry}
 * instances have been remapped.
 *
 * @author mine_diver
 * @see LevelLegacyRegistry
 */
@SuperBuilder
public class PostRegistryRemapEvent extends Event {}
