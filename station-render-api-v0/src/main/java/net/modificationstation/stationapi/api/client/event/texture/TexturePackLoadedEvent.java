package net.modificationstation.stationapi.api.client.event.texture;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.texture.TextureManager;

@RequiredArgsConstructor
public abstract class TexturePackLoadedEvent extends Event {

    public final TextureManager textureManager;
    public final TexturePack newTexturePack;

    public static class Before extends TexturePackLoadedEvent {

        public Before(TextureManager textureManager, TexturePack newTexturePack) {
            super(textureManager, newTexturePack);
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    public static class After extends TexturePackLoadedEvent {

        public After(TextureManager textureManager, TexturePack newTexturePack) {
            super(textureManager, newTexturePack);
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
