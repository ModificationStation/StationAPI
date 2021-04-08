package net.modificationstation.stationapi.api.client.texture;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.util.HasHandler;

import java.util.*;

/**
 * Used to add textures.
 * You want to reference this inside your registerTextures method.
 * <p>
 * Use TextureFactory.INSTANCE.add[Animated]Texture("GUI_ITEMS|TERRAIN", yourTexturePath) to add your texture.
 * Then use the returned int value for your return values inside your item/block.
 *
 * @see ItemBase#setTexturePosition(int)
 * @see BlockBase#getTextureForSide(int)
 * @see BlockBase#getTextureForSide(int, int)
 * @see Class#getResource(String) Class.getResource(String) for your texture path.
 */
public interface TextureFactory extends HasHandler<TextureFactory> {

    TextureFactory INSTANCE = new TextureFactory() {

        private TextureFactory handler;

        @Override
        public void setHandler(TextureFactory handler) {
            this.handler = handler;
        }

        /**
         * Adds a standard, non-animated texture into a custom spritesheet.
         * @param type The texture registry that should receive this texture. Use {@link TextureRegistry}.INSTANCE.getRegistry("GUI_ITEMS|TERRAIN") to get the object for this value.
         * @param pathToImage The path to the texture for use.
         * @return An int value that should be used when returning the texture index inside items/blocks.
         */
        @Override
        public int addTexture(TextureRegistry type, String pathToImage) {
            checkAccess(handler);
            return handler.addTexture(type, pathToImage);
        }

        /**
         * Adds an animated texture into a custom spritesheet.
         * @param type The texture registry that should receive this texture. Use TextureRegistry.INSTANCE.getRegistry("GUI_ITEMS|TERRAIN") to get the object for this value.
         * @param pathToImage The path to the texture for use.
         * @return An int value that should be used when returning the texture index inside items/blocks.
         */
        @Override
        public int addAnimatedTexture(TextureRegistry type, String pathToImage, int animationRate) {
            checkAccess(handler);
            return handler.addAnimatedTexture(type, pathToImage, animationRate);
        }

        @Override
        public int createNewAtlas(TextureRegistry type, String originalAtlas, String path) {
            checkAccess(handler);
            return handler.createNewAtlas(type, originalAtlas, path);
        }

        @Override
        public int createAtlasCopy(TextureRegistry type, String originalAtlas, int ID, String path) {
            checkAccess(handler);
            return handler.createAtlasCopy(type, originalAtlas, ID, path);
        }

        @Override
        public int nextSpriteID(TextureRegistry type) {
            checkAccess(handler);
            return handler.nextSpriteID(type);
        }

        @Override
        public String getOriginalStationAtlasFormat() {
            checkAccess(handler);
            return handler.getOriginalStationAtlasFormat();
        }

        @Override
        public String getCopiedStationAtlasFormat() {
            checkAccess(handler);
            return handler.getCopiedStationAtlasFormat();
        }

        @Override
        public Map<String, String> getFakedAtlases() {
            checkAccess(handler);
            return handler.getFakedAtlases();
        }
    };

    int addTexture(TextureRegistry type, String pathToImage);

    int addAnimatedTexture(TextureRegistry type, String pathToImage, int animationRate);

    int createNewAtlas(TextureRegistry type, String originalAtlas, String path);

    int createAtlasCopy(TextureRegistry type, String originalAtlas, int ID, String path);

    int nextSpriteID(TextureRegistry type);

    String getOriginalStationAtlasFormat();

    String getCopiedStationAtlasFormat();

    Map<String, String> getFakedAtlases();
}
