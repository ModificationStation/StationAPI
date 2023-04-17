package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;

public interface BlockTemplate {

    static void onConstructor(BlockBase block, Identifier id) {
        Registry.register(BlockRegistry.INSTANCE, id, block);
    }
}
