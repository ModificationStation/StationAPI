package net.modificationstation.stationapi.api.client.event.render.model;

import net.modificationstation.stationapi.api.client.model.block.BlockModelPredicateProvider;
import net.modificationstation.stationapi.api.client.registry.BlockModelPredicateProviderRegistry;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;

public final class BlockModelPredicateProviderRegistryEvent extends RegistryEvent.EntryTypeBound<BlockModelPredicateProvider, BlockModelPredicateProviderRegistry> {
    public BlockModelPredicateProviderRegistryEvent() {
        super(BlockModelPredicateProviderRegistry.INSTANCE);
    }
}
