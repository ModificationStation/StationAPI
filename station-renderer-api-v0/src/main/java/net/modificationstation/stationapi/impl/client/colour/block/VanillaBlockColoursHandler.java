package net.modificationstation.stationapi.impl.client.colour.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.FoliageColour;
import net.minecraft.client.render.block.GrassColour;
import net.modificationstation.stationapi.api.client.colour.block.BlockColours;
import net.modificationstation.stationapi.api.client.colour.world.BiomeColours;
import net.modificationstation.stationapi.api.client.event.colour.block.BlockColoursRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class VanillaBlockColoursHandler {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerBlockColours(BlockColoursRegisterEvent event) {
        BlockColours blockColours = event.getBlockColours();
        blockColours.registerColourProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? GrassColour.get(0.5, 1.0) : BiomeColours.getGrassColour(world, pos),
                BlockBase.GRASS, BlockBase.TALLGRASS
        );
        blockColours.registerColourProvider((state, world, pos, tintIndex) -> {
                    if (world == null || pos == null)
                        return FoliageColour.method_1083();
                    else {
                        int meta = world.getTileMeta(pos.x, pos.y, pos.z);
                        return (meta & 1) == 1 ? FoliageColour.method_1079() : (meta & 2) == 2 ? FoliageColour.method_1082() : BiomeColours.getFoliageColour(world, pos);
                    }
                },
                BlockBase.LEAVES
        );
        blockColours.registerColourProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? -1 : BiomeColours.getWaterColour(world, pos),
                BlockBase.FLOWING_WATER, BlockBase.STILL_WATER
        );
    }
}
