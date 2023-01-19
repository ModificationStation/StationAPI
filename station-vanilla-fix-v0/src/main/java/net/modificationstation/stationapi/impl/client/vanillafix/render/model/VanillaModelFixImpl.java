package net.modificationstation.stationapi.impl.client.vanillafix.render.model;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaModelFixImpl {

//    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
//    private static void registerBlockModelPredicateProviders(BlockModelPredicateProviderRegistryEvent event) {
//        BlockModelPredicateProvider fastLeaves = (state, world, pos, seed) -> FixedLeaves.isTransparent ? 0 : 1;
//        event.registry.register(Blocks.OAK_LEAVES, of("fast_leaves"), fastLeaves);
//        event.registry.register(Blocks.SPRUCE_LEAVES, of("fast_leaves"), fastLeaves);
//        event.registry.register(Blocks.BIRCH_LEAVES, of("fast_leaves"), fastLeaves);
//    }
//
//    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
//    private static void registerItemModelPredicateProviders(ItemModelPredicateProviderRegistryEvent event) {
//        ItemModelPredicateProvider fastLeaves = (stack, world, entity, seed) -> FixedLeaves.isTransparent ? 0 : 1;
//        event.registry.register(Items.OAK_LEAVES, of("fast_leaves"), fastLeaves);
//        event.registry.register(Items.SPRUCE_LEAVES, of("fast_leaves"), fastLeaves);
//        event.registry.register(Items.BIRCH_LEAVES, of("fast_leaves"), fastLeaves);
//    }
}
