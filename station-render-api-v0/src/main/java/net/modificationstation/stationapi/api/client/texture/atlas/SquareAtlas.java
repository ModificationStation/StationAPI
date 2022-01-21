package net.modificationstation.stationapi.api.client.texture.atlas;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.awt.image.*;

public class SquareAtlas extends Atlas {

    protected final Int2ObjectMap<Identifier> initIDs = new Int2ObjectOpenHashMap<>();

    public SquareAtlas(Identifier identifier, final String spritesheet, final int sizeSquareRoot) {
        super(identifier, spritesheet, sizeSquareRoot * sizeSquareRoot, true);
    }

    public SquareAtlas(Identifier identifier, final String spritesheet, final int sizeSquareRoot, final Atlas parent) {
        super(identifier, spritesheet, sizeSquareRoot * sizeSquareRoot, true, parent);
    }

    @Override
    protected void init() {
        BufferedImage image = getImage();
        final int
                sizeSquareRoot = (int) Math.sqrt(getUnitSize()),
                textureWidth = image.getWidth() / sizeSquareRoot,
                textureHeight = image.getHeight() / sizeSquareRoot;
        for (int y = 0; y < sizeSquareRoot; y++)
            for (int x = 0; x < sizeSquareRoot; x++) {
                int textureIndex = (parent == null ? 0 : parent.size) + y * sizeSquareRoot + x;
                Sprite sprite = new Sprite(
                        initIDs.containsKey(textureIndex) ? initIDs.get(textureIndex) : Identifier.of(id.modID, String.valueOf(textureIndex)), textureIndex,
                        textureWidth, textureHeight
                );
                sprite.x = x * textureWidth;
                sprite.y = y * textureHeight;
                textures.put(textureIndex, sprite);
            }
    }

//    @Override
//    public void reloadFromTexturePack(TexturePack newTexturePack) {
//        super.reloadFromTexturePack(newTexturePack);
//        BufferedImage image = getImage();
//        final int
//                sizeSquareRoot = (int) Math.sqrt(getUnitSize()),
//                textureWidth = image.getWidth() / sizeSquareRoot,
//                textureHeight = image.getHeight() / sizeSquareRoot;
//        textures..forEach(texture -> {
//            texture.x = (texture.index % sizeSquareRoot) * textureWidth;
//            texture.y = (texture.index / sizeSquareRoot) * textureHeight;
//            texture.width = textureWidth;
//            texture.height = textureHeight;
//        });
//        textures.forEach(Sprite::updateUVs);
//    }

    /* !==========================! */
    /* !--- DEPRECATED SECTION ---! */
    /* !==========================! */

    @Deprecated
    public SquareAtlas(final String spritesheet, final int sizeSquareRoot) {
        super(spritesheet, sizeSquareRoot * sizeSquareRoot, true);
    }

    @Deprecated
    public SquareAtlas(final String spritesheet, final int sizeSquareRoot, final Atlas parent) {
        super(spritesheet, sizeSquareRoot * sizeSquareRoot, true, parent);
    }
}
