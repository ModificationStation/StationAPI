package net.modificationstation.stationapi.api.client.texture.atlas;

import net.minecraft.client.resource.TexturePack;

import java.awt.image.*;

public class SquareAtlas extends Atlas {

    public SquareAtlas(final String spritesheet, final int sizeSquareRoot) {
        super(spritesheet, sizeSquareRoot * sizeSquareRoot, true);
    }

    public SquareAtlas(final String spritesheet, final int sizeSquareRoot, final Atlas parent) {
        super(spritesheet, sizeSquareRoot * sizeSquareRoot, true, parent);
    }

    @Override
    protected void init() {
        BufferedImage image = getImage();
        final int
                sizeSquareRoot = (int) Math.sqrt(getUnitSize()),
                textureWidth = image.getWidth() / sizeSquareRoot,
                textureHeight = image.getHeight() / sizeSquareRoot;
        for (int y = 0; y < sizeSquareRoot; y++) for (int x = 0; x < sizeSquareRoot; x++)
            textures.add(new Sprite(
                    (parent == null ? 0 : parent.size) + y * sizeSquareRoot + x,
                    x * textureWidth, y * textureHeight,
                    textureWidth, textureHeight
            ));
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        super.reloadFromTexturePack(newTexturePack);
        BufferedImage image = getImage();
        final int
                sizeSquareRoot = (int) Math.sqrt(getUnitSize()),
                textureWidth = image.getWidth() / sizeSquareRoot,
                textureHeight = image.getHeight() / sizeSquareRoot;
        textures.forEach(texture -> {
            texture.x = (texture.index % sizeSquareRoot) * textureWidth;
            texture.y = (texture.index / sizeSquareRoot) * textureHeight;
            texture.width = textureWidth;
            texture.height = textureHeight;
        });
        textures.forEach(Sprite::updateUVs);
    }
}
