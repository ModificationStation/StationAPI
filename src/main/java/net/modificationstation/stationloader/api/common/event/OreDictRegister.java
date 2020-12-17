package net.modificationstation.stationloader.api.common.event;

import net.modificationstation.stationloader.impl.common.util.OreDict;

public interface OreDictRegister {

    GameEvent<OreDictRegister> EVENT = new GameEvent<>(OreDictRegister.class, listeners ->
            oreDictMap -> {
                for (OreDictRegister event : listeners)
                    event.registerOreDict(oreDictMap);
            });

    void registerOreDict(OreDict oreDictMap);
}
