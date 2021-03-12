package net.modificationstation.stationapi.api.client.texture;

import java.util.function.*;

public class TextureAtlas {

    public static final TextureAtlas GUI_ITEMS = new TextureAtlas("/gui/items.png", 256);

    public final String spritesheet;
    protected int size;

    public TextureAtlas(String spritesheet, final int size, TextureAtlas parent) {
        this(spritesheet, ((IntSupplier) () -> {
            if (parent instanceof ExpandableTextureAtlas)
                throw new UnsupportedOperationException("Parent atlas can't be expandable!");
            else
                return size + parent.size;
        }).getAsInt());
    }

    public TextureAtlas(String spritesheet, int size) {
        this.spritesheet = spritesheet;
        this.size = size;
    }
}
