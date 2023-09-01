package net.modificationstation.stationapi.api.event.mod;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;

/**
 * <s>PostInitialization event called for mods to do their setup when the other mods finished initializing.
 * Minecraft classes can not be referenced during this event.</s>
 *
 * @deprecated Use {@link InitEvent} with phases and priorities instead.
 *
 * @author mine_diver
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@SuperBuilder
@Deprecated
public class PostInitEvent extends Event {}
