package net.modificationstation.stationapi.api.common.event;

import net.modificationstation.stationapi.api.common.util.OreDict;

public interface OreDictRegister {

    GameEventOld<OreDictRegister> EVENT = new GameEventOld<>(OreDictRegister.class, listeners ->
            oreDictMap -> {
                for (OreDictRegister event : listeners)
                    event.registerOreDict(oreDictMap);
            });

    void registerOreDict(OreDict oreDictMap);
}
