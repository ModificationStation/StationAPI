package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.FarmlandBlock;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.CustomFarmlandBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FarmlandBlock.class)
public class FarmlandBlockMixin implements CustomFarmlandBlock {

    @Override
    public boolean isWet(World world, int x, int y, int z) {
        return world.getBlockMeta(x, y, z) > 0;
    }
}
