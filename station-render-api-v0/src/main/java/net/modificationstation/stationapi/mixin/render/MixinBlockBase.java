package net.modificationstation.stationapi.mixin.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockBase.class)
@EnvironmentInterface(value = EnvType.CLIENT, itf = CustomAtlasProvider.class)
public class MixinBlockBase implements CustomAtlasProvider {

    @Override
    @Environment(EnvType.CLIENT)
    public Atlas getAtlas() {
        return Atlases.getTerrain();
    }
}
