package net.modificationstation.stationapi.api.event.worldgen.biome;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.StationAPI;

/**
 * A general event for modifying biomes in a dimension.
 * <br><br>
 * <b>BE AWARE THAT BIOME OBJECTS MAY BE SHARED BETWEEN DIMENSIONS.
 * <br>
 * TRACK THIS YOURSELF TO AVOID DUPLICATE MODIFICATIONS.</b>
 * @see it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap
 */
@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class BiomeModificationEvent extends Event {
    public final World world;
    public final Biome biome;
}