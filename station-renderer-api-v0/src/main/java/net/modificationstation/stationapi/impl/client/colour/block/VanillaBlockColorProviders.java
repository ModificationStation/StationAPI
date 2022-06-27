package net.modificationstation.stationapi.impl.client.colour.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.FoliageColour;
import net.minecraft.client.render.block.GrassColour;
import net.modificationstation.stationapi.api.client.colour.world.BiomeColors;
import net.modificationstation.stationapi.api.client.event.colour.block.BlockColorsRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class VanillaBlockColorProviders {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerBlockColors(BlockColorsRegisterEvent event) {
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? GrassColour.get(0.5, 1.0) : BiomeColors.getGrassColor(world, pos),
                BlockBase.GRASS, BlockBase.TALLGRASS
        );
        event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
                    if (world == null || pos == null)
                        return FoliageColour.method_1083();
                    else {
                        int meta = world.getTileMeta(pos.x, pos.y, pos.z);
                        return (meta & 1) == 1 ? FoliageColour.method_1079() : (meta & 2) == 2 ? FoliageColour.method_1082() : BiomeColors.getFoliageColor(world, pos);
                    }
                },
                BlockBase.LEAVES
        );
        event.blockColors.registerColorProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? -1 : BiomeColors.getWaterColor(world, pos),
                BlockBase.FLOWING_WATER, BlockBase.STILL_WATER
        );
    }
}
