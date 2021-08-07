package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.client.texture.binder.AnimatedTextureBinder;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;

import java.util.function.*;

public interface ItemTemplate<T extends ItemBase> extends CustomAtlasProvider {

    default T setTranslationKey(ModID modID, String translationKey) {
        //noinspection unchecked
        return (T) ((ItemBase) this).setTranslationKey(Identifier.of(modID, translationKey).toString());
    }

    @Override
    default Atlas getAtlas() {
        return ExpandableAtlas.STATION_GUI_ITEMS;
    }

    default Atlas.Texture setTexture(String path) {
        Atlas.Texture texture = ((ExpandableAtlas) getAtlas()).addTexture(path);
        ((ItemBase) this).setTexturePosition(texture.index);
        return texture;
    }

    default <E extends StationTextureBinder> E setTextureBinder(String staticReference, BiFunction<Atlas, Atlas.Texture, E> initializer) {
        E textureBinder = ((ExpandableAtlas) getAtlas()).addTextureBinder(staticReference, initializer);
        ((ItemBase) this).setTexturePosition(textureBinder.index);
        return textureBinder;
    }

    default AnimatedTextureBinder setAnimationBinder(String animationPath, int animationRate, String staticReference) {
        AnimatedTextureBinder textureBinder = ((ExpandableAtlas) getAtlas()).addAnimationBinder(animationPath, animationRate, staticReference);
        ((ItemBase) this).setTexturePosition(textureBinder.index);
        return textureBinder;
    }
}
