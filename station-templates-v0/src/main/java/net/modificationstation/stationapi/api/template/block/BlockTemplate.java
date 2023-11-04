package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;

public interface BlockTemplate {

    static int getNextId() {
        return BlockRegistry.AUTO_ID;
    }

    static void onConstructor(Block block, Identifier id) {
        Registry.register(BlockRegistry.INSTANCE, block.id, id, block);
    }
}
