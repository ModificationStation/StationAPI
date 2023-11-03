package net.modificationstation.stationapi.mixin.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.client.block.StationRendererBlock;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
@EnvironmentInterface(value = EnvType.CLIENT, itf = StationRendererBlock.class)
public class MixinBlockBase implements StationRendererBlock {

    @Override
    @Environment(EnvType.CLIENT)
    public Atlas getAtlas() {
        return Atlases.getTerrain();
    }
}
