package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.color.world.WaterColors;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WaterColors.class)
public interface WaterColorAccessor {
    @Accessor("colorMap")
    static int[] stationapi$getMap() {
        return Util.assertMixin();
    }
}
