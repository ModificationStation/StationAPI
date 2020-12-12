package net.modificationstation.stationloader.api.common.event;

import net.modificationstation.stationloader.impl.common.util.OreDict;

public interface OreDictRegister {

    SimpleEvent<OreDictRegister> EVENT = new SimpleEvent<>(OreDictRegister.class, listeners ->
            oreDictMap -> {
                for (OreDictRegister event : listeners)
                    event.registerOreDict(oreDictMap);
            });

    void registerOreDict(OreDict oreDictMap);
}
