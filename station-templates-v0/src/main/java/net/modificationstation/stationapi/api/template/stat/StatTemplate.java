package net.modificationstation.stationapi.api.template.stat;

import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.StatRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

public interface StatTemplate {
    static int getNextId() {
        return StatRegistry.AUTO_ID;
    }

    static void onConstructor(Stat stat, Identifier id) {
        Registry.register(StatRegistry.INSTANCE, stat.id, id, stat);
    }
}
