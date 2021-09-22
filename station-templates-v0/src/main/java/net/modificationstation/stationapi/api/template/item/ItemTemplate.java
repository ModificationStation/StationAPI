package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
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
        return Atlases.getStationGuiItems();
    }

    default Atlas.Texture setTexture(Identifier textureIdentifier) {
        Atlas.Texture texture = ((ExpandableAtlas) getAtlas()).addTexture(textureIdentifier);
        ((ItemBase) this).setTexturePosition(texture.index);
        return texture;
    }

    default Atlas.Texture setTexture(String texturePath) {
        Atlas.Texture texture = ((ExpandableAtlas) getAtlas()).addTexture(texturePath);
        ((ItemBase) this).setTexturePosition(texture.index);
        return texture;
    }

    default <E extends StationTextureBinder> E setTextureBinder(Identifier staticReference, Function<Atlas.Texture, E> initializer) {
        E textureBinder = ((ExpandableAtlas) getAtlas()).addTextureBinder(staticReference, initializer);
        ((ItemBase) this).setTexturePosition(textureBinder.index);
        return textureBinder;
    }
}
