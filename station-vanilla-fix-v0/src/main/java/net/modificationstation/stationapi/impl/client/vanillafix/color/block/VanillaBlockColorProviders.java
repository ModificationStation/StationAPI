package net.modificationstation.stationapi.impl.client.vanillafix.color.block;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaBlockColorProviders {

//    @EventListener(priority = ListenerPriority.HIGH)
//    private static void registerBlockColors(BlockColorsRegisterEvent event) {
//        event.blockColors.registerColorProvider(
//                (state, world, pos, tintIndex) -> world == null || pos == null ? GrassColour.get(0.5, 1.0) : BiomeColors.getGrassColor(world, pos),
//                BlockBase.GRASS, BlockBase.TALLGRASS
//        );
//        event.blockColors.registerColorProvider(
//                (state, world, pos, tintIndex) -> world == null || pos == null ? FoliageColour.method_1083() : BiomeColors.getFoliageColor(world, pos),
//                Blocks.OAK_LEAVES
//        );
//        event.blockColors.registerColorProvider(
//                (state, world, pos, tintIndex) -> FoliageColour.method_1079(),
//                Blocks.SPRUCE_LEAVES
//        );
//        event.blockColors.registerColorProvider(
//                (state, world, pos, tintIndex) -> FoliageColour.method_1082(),
//                Blocks.BIRCH_LEAVES
//        );
//        event.blockColors.registerColorProvider(
//                (state, world, pos, tintIndex) -> world == null || pos == null ? -1 : BiomeColors.getWaterColor(world, pos),
//                BlockBase.FLOWING_WATER, BlockBase.STILL_WATER
//        );
//    }
}
