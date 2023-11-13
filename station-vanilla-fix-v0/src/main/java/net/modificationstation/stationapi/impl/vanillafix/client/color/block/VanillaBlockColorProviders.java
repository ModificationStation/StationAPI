package net.modificationstation.stationapi.impl.vanillafix.client.color.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.class_287;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.color.world.BiomeColors;
import net.modificationstation.stationapi.api.client.event.color.block.BlockColorsRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaBlockColorProviders {
    @EventListener
    private static void registerBlockColors(BlockColorsRegisterEvent event) {
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? class_287.method_981(0.5, 1.0) : BiomeColors.getGrassColor(world, pos),
                Block.GRASS_BLOCK, Block.GRASS
        );
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? state.getBlock().getColor(0) : state.getBlock().getColorMultiplier(world, pos.x, pos.y, pos.z),
                Block.LEAVES
        );
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? -1 : BiomeColors.getWaterColor(world, pos),
                Block.FLOWING_WATER, Block.WATER
        );
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> 0xFFAADB74,
                Block.SUGAR_CANE
        );
    }
}