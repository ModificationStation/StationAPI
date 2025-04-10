package net.modificationstation.stationapi.mixin.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.client.block.StationRendererBlock;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Block.class)
@EnvironmentInterface(value = EnvType.CLIENT, itf = StationRendererBlock.class)
class BlockMixin implements StationRendererBlock {
    @Override
    @Environment(EnvType.CLIENT)
    @Unique
    public Atlas getAtlas() {
        return Atlases.getTerrain();
    }
}
