package net.modificationstation.stationapi.api.client.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.Util;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
@EnvironmentInterface(value = EnvType.CLIENT, itf = CustomAtlasProvider.class)
public interface StationRendererItem extends CustomAtlasProvider {

    @Override
    default Atlas getAtlas() {
        return Util.assertImpl();
    }

    default Atlas.Sprite setTexture(Identifier textureIdentifier) {
        return Util.assertImpl();
    }

    @Deprecated
    default Atlas.Sprite setTexture(String texturePath) {
        return Util.assertImpl();
    }

    default <E extends StationTextureBinder> E setTextureBinder(Identifier staticReference, Function<Atlas.Sprite, E> initializer) {
        return Util.assertImpl();
    }
}
