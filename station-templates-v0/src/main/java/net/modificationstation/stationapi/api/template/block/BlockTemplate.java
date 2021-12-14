package net.modificationstation.stationapi.api.template.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;

@EnvironmentInterface(value = EnvType.CLIENT, itf = CustomAtlasProvider.class)
public interface BlockTemplate<T extends BlockBase> extends CustomAtlasProvider {

    default T setTranslationKey(ModID modID, String translationKey) {
        //noinspection unchecked
        return (T) ((BlockBase) this).setTranslationKey(Identifier.of(modID, translationKey).toString());
    }

    @Override
    @Environment(EnvType.CLIENT)
    default Atlas getAtlas() {
        return Atlases.getStationTerrain();
    }
}
