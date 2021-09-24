package net.modificationstation.stationapi.mixin.render;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockBase.class)
public class MixinBlockBase implements CustomAtlasProvider {

    @Override
    public Atlas getAtlas() {
        return Atlases.getTerrain();
    }
}
