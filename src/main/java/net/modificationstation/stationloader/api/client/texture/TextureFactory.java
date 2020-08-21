package net.modificationstation.stationloader.api.client.texture;

import net.modificationstation.stationloader.api.common.util.HasHandler;

public interface TextureFactory extends HasHandler<TextureFactory> {

    TextureFactory INSTANCE = new TextureFactory() {

        private TextureFactory handler;

        @Override
        public void setHandler(TextureFactory handler) {
            this.handler = handler;
        }

        @Override
        public int addTexture(TextureRegistry type, String pathToImage) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.addTexture(type, pathToImage);
        }

        @Override
        public int createNewAtlas(TextureRegistry type, String originalAtlas, String path) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.createNewAtlas(type, originalAtlas, path);
        }

        @Override
        public int createAtlasCopy(TextureRegistry type, String originalAtlas, int ID, String path) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.createAtlasCopy(type, originalAtlas, ID, path);
        }

        @Override
        public int nextSpriteID(TextureRegistry type) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.nextSpriteID(type);
        }
    };

    int addTexture(TextureRegistry type, String pathToImage);

    int createNewAtlas(TextureRegistry type, String originalAtlas, String path);

    int createAtlasCopy(TextureRegistry type, String originalAtlas, int ID, String path);

    int nextSpriteID(TextureRegistry type);
}
