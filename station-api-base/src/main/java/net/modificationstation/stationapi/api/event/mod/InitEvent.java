package net.modificationstation.stationapi.api.event.mod;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import org.jetbrains.annotations.ApiStatus;

import static net.mine_diver.unsafeevents.event.EventPhases.DEFAULT_PHASE;
import static net.modificationstation.stationapi.api.event.mod.InitEvent.*;

/**
 * @author mine_diver
 */
@SuperBuilder
@EventPhases({
        INTERNAL_PRE_INIT_PHASE,
        PRE_INIT_PHASE,
        StationAPI.INTERNAL_PHASE,
        DEFAULT_PHASE,
        POST_INIT_PHASE
})
public class InitEvent extends Event {
    public static final String
            PRE_INIT_PHASE = "stationapi:pre_init",
            POST_INIT_PHASE = "stationapi:post_init";
    @ApiStatus.Internal
    public static final String
            INTERNAL_PRE_INIT_PHASE = "stationapi:internal_pre_init";
}
