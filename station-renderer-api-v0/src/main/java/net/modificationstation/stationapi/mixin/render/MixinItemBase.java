package net.modificationstation.stationapi.mixin.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.client.item.StationRendererItem;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Function;

@Mixin(ItemBase.class)
@EnvironmentInterface(value = EnvType.CLIENT, itf = StationRendererItem.class)
public abstract class MixinItemBase implements StationRendererItem {

    @Shadow public abstract ItemBase setTexturePosition(int i);

    @Shadow public abstract ItemBase setTexturePosition(int i, int j);

    @Override
    @Environment(EnvType.CLIENT)
    public Atlas getAtlas() {
        return Atlases.getGuiItems();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Atlas.Sprite setTexture(Identifier textureIdentifier) {
        Atlas.Sprite texture = ((ExpandableAtlas) getAtlas()).addTexture(textureIdentifier);
        setTexturePosition(texture.index);
        return texture;
    }

    @Override
    @Deprecated
    @Environment(EnvType.CLIENT)
    public Atlas.Sprite setTexture(String texturePath) {
        Atlas.Sprite texture = ((ExpandableAtlas) getAtlas()).addTexture(texturePath);
        setTexturePosition(texture.index);
        return texture;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public <E extends StationTextureBinder> E setTextureBinder(Identifier staticReference, Function<Atlas.Sprite, E> initializer) {
        E textureBinder = ((ExpandableAtlas) getAtlas()).addTextureBinder(staticReference, initializer);
        setTexturePosition(textureBinder.index);
        return textureBinder;
    }
}
