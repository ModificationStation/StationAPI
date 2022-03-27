package net.modificationstation.stationapi.api.client.registry;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.model.block.BlockModelPredicateProvider;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class BlockModelPredicateProviderRegistry extends Registry<BlockModelPredicateProvider> {

    public static final BlockModelPredicateProviderRegistry INSTANCE = new BlockModelPredicateProviderRegistry();

    private static final Identifier META_ID = Identifier.of("meta");
    private static final BlockModelPredicateProvider META_PROVIDER = (state, clientWorld, pos, seed) -> clientWorld == null || pos == null ? 0 : MathHelper.clamp(clientWorld.getTileMeta(pos.x, pos.y, pos.z), 0, 15);
    private final Map<BlockBase, Map<Identifier, BlockModelPredicateProvider>> BLOCK_SPECIFIC = new IdentityHashMap<>();

    private BlockModelPredicateProviderRegistry() {
        super(MODID.id("block_model_predicate_providers"));
    }

    public BlockModelPredicateProvider get(BlockBase block, Identifier identifier) {
        if (identifier == META_ID)
            return META_PROVIDER;

        Optional<BlockModelPredicateProvider> modelPredicateProvider = get(identifier);
        if (modelPredicateProvider.isPresent())
            return modelPredicateProvider.get();
        else {
            Map<Identifier, BlockModelPredicateProvider> map = BLOCK_SPECIFIC.get(block);
            return map == null ? null : map.get(identifier);
        }
    }

    public void register(BlockBase block, Identifier id, BlockModelPredicateProvider provider) {
        BLOCK_SPECIFIC.computeIfAbsent(block, blockx -> new IdentityHashMap<>()).put(id, provider);
    }
}
