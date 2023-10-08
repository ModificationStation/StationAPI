package net.modificationstation.stationapi.api.event.block;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.MiningLevels;
import net.modificationstation.stationapi.api.registry.Identifier;

/**
 * This event is fired when its suitable to register mining levels,
 * you can use the methods in the event or directly call MiningLevels class
 */
@EventPhases(StationAPI.INTERNAL_PHASE)
@SuperBuilder
public class MiningLevelRegisterEvent extends Event {
    public boolean addMiningLevel(Identifier identifier, int miningLevel){
        return MiningLevels.addMiningLevel(identifier, miningLevel);
    }
}
