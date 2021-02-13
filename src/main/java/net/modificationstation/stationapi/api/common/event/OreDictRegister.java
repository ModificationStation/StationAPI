package net.modificationstation.stationapi.api.common.event;

import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.common.util.OreDict;

@RequiredArgsConstructor
public class OreDictRegister extends Event {

    public final OreDict oreDictMap;
}
