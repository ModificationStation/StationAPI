package net.modificationstation.stationapi.impl.vanillafix.client.color.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.GrassColour;
import net.modificationstation.stationapi.api.client.color.world.BiomeColors;
import net.modificationstation.stationapi.api.client.event.color.block.BlockColorsRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaBlockColorProviders {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerBlockColors(BlockColorsRegisterEvent event) {
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? GrassColour.get(0.5, 1.0) : BiomeColors.getGrassColor(world, pos),
                BlockBase.GRASS, BlockBase.TALLGRASS
        );
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? state.getBlock().getBaseColour(0) : state.getBlock().getColourMultiplier(world, pos.x, pos.y, pos.z),
                BlockBase.LEAVES
        );
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? -1 : BiomeColors.getWaterColor(world, pos),
                BlockBase.FLOWING_WATER, BlockBase.STILL_WATER
        );
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> 0xFFAADB74,
                BlockBase.SUGAR_CANES
        );
    }
}