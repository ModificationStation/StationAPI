package net.modificationstation.stationapi.api.common.event;

import lombok.Getter;
import net.modificationstation.stationapi.api.common.registry.ModID;

public class ModEvent extends Event {

    @Getter
    ModID modID;
}
