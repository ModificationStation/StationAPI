package net.modificationstation.stationapi.impl.client.vanillafix.color.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.FoliageColour;
import net.minecraft.client.render.block.GrassColour;
import net.modificationstation.stationapi.api.client.colour.world.BiomeColors;
import net.modificationstation.stationapi.api.client.event.colour.block.BlockColorsRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaBlockColorProviders {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerBlockColors(BlockColorsRegisterEvent event) {
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? GrassColour.get(0.5, 1.0) : BiomeColors.getGrassColor(world, pos),
                BlockBase.GRASS, BlockBase.TALLGRASS
        );
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? FoliageColour.method_1083() : BiomeColors.getFoliageColor(world, pos),
                Blocks.OAK_LEAVES
        );
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> FoliageColour.method_1079(),
                Blocks.SPRUCE_LEAVES
        );
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> FoliageColour.method_1082(),
                Blocks.BIRCH_LEAVES
        );
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? -1 : BiomeColors.getWaterColor(world, pos),
                BlockBase.FLOWING_WATER, BlockBase.STILL_WATER
        );
    }
}
