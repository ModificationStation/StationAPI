package net.modificationstation.stationapi.api.client.texture.atlas;

import com.google.common.collect.ImmutableList;
import net.modificationstation.stationapi.api.client.texture.SpritesheetHelper;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceHelper;

import java.util.function.Function;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class ExpandableAtlas extends Atlas {

    public ExpandableAtlas(final Identifier identifier) {
        super(identifier, "/assets/" + NAMESPACE + "/atlases/" + identifier, 0, false);
    }

    public ExpandableAtlas(final Identifier identifier, final Atlas parent) {
        super(identifier, "/assets/" + NAMESPACE + "/atlases/" + identifier, 0, false, parent);
    }

    public Sprite addTexture(Identifier identifier) {
        return addTexture(identifier, true);
    }

    private Sprite addTexture(Identifier identifier, boolean incrementSize) {
        if (idToTex.containsKey(identifier))
            return idToTex.get(identifier);
        else {
            Sprite texture = new Sprite(identifier, size);
            idToTex.put(identifier, texture);
            textures.put(size, texture);
            if (incrementSize)
                size++;
            return texture;
        }
    }

    @Deprecated
    public Sprite addTexture(String texturePath) {
        return addTexture(ResourceHelper.ASSETS.toId(texturePath, "/" + NAMESPACE + "/textures", "png"));
    }

    public ImmutableList<Sprite> addSpritesheet(int texturesPerLine, SpritesheetHelper spritesheetHelper) {
        ImmutableList.Builder<Sprite> builder = ImmutableList.builder();
        for (int y = 0; y < texturesPerLine; y++) for (int x = 0; x < texturesPerLine; x++) {
            Identifier identifier = spritesheetHelper.generateIdentifier(y * texturesPerLine + x);
            if (identifier != null) builder.add(addTexture(identifier, false));
            size++;
        }
        return builder.build();
    }

    public <T extends StationTextureBinder> T addTextureBinder(Identifier staticReference, Function<Sprite, T> initializer) {
        return addTextureBinder(addTexture(staticReference), initializer);
    }
}
