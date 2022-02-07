package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.render.block.WaterColour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WaterColour.class)
public interface WaterColourAccessor {

    @Accessor("map")
    static int[] stationapi$getMap() {
        throw new AssertionError("Mixin!");
    }
}
