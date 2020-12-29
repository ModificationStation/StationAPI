package net.modificationstation.stationapi.api.common.event;

import net.modificationstation.stationapi.api.common.util.OreDict;

public interface OreDictRegister {

    GameEvent<OreDictRegister> EVENT = new GameEvent<>(OreDictRegister.class, listeners ->
            oreDictMap -> {
                for (OreDictRegister event : listeners)
                    event.registerOreDict(oreDictMap);
            });

    void registerOreDict(OreDict oreDictMap);
}
