package net.modificationstation.stationapi.api.registry.tag;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BlockTags {
    public static final TagKey<Block>
            LOGS = of("logs"),
            LEAVES = of("leaves"),
            PLANKS = of("planks"),
            INFINIBURN = of("infiniburn");

    private static TagKey<Block> of(String id) {
        return TagKey.of(BlockRegistry.KEY, Identifier.of(id));
    }
}
