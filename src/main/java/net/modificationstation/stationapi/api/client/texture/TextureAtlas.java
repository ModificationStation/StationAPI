package net.modificationstation.stationapi.api.client.texture;

public class TextureAtlas {

    private final TextureAtlas parent;
    public final String spritesheet;
    protected int size;

    public TextureAtlas(TextureAtlas parent, String spritesheet, int size) {
        if (parent != null)
            if (parent instanceof ExpandableTextureAtlas)
                throw new UnsupportedOperationException("Parent atlas can't be expandable!");
            else
                size += parent.size;
        this.parent = parent;
        this.spritesheet = spritesheet;
        this.size = size;
    }
}
