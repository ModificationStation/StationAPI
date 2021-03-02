package net.modificationstation.stationapi.api.client.texture;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TextureAtlas {

    private final TextureAtlas parent;
    private final boolean expandable;
    private int size;
    public BufferedImage spritesheet;

    public TextureAtlas(boolean expandable, TextureAtlas parent, BufferedImage spritesheet) {
        if (parent != null)
            if (parent.expandable)
                throw new UnsupportedOperationException("Parent atlas can't be expendable!");
            else
                size = parent.size;
        this.parent = parent;
        this.expandable = expandable;
        this.spritesheet = spritesheet;
    }

    public int addTexture(BufferedImage texture) {
        if (expandable) {
            if (spritesheet == null) {
                spritesheet = texture;
            } else {
                int previousWidth = spritesheet.getWidth();
                int textureHeight = texture.getHeight();
                int spritesheetHeight = spritesheet.getHeight();
                resizeSpritesheet(spritesheet.getWidth() + texture.getWidth(), Math.max(textureHeight, spritesheetHeight));
                Graphics2D graphics = spritesheet.createGraphics();
                graphics.drawImage(texture, previousWidth, 0, null);
                graphics.dispose();
            }
            int lastIndex = size;
            size++;
            return lastIndex;
        } else
            throw new UnsupportedOperationException("Can't add textures to fixed-size atlases!");
    }

    private void resizeSpritesheet(int targetWidth, int targetHeight) {
        BufferedImage previousSpriteSheet = spritesheet;
        spritesheet = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = spritesheet.createGraphics();
        graphics.drawImage(previousSpriteSheet, 0, 0, null);
        graphics.dispose();
    }
}
