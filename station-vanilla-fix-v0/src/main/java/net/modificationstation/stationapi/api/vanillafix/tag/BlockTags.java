package net.modificationstation.stationapi.api.vanillafix.tag;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tag.TagKey;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BlockTags {

    public static final TagKey<BlockBase> LOGS = of("logs");

    private static TagKey<BlockBase> of(String id) {
        return TagKey.of(BlockRegistry.KEY, Identifier.of(id));
    }
}
