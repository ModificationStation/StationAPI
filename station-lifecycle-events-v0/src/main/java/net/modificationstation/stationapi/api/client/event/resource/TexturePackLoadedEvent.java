package net.modificationstation.stationapi.api.client.event.resource;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.texture.TextureManager;

@SuperBuilder
public abstract class TexturePackLoadedEvent extends Event {

    public final TextureManager textureManager;
    public final TexturePack newTexturePack;

    @SuperBuilder
    public static class Before extends TexturePackLoadedEvent {

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    @SuperBuilder
    public static class After extends TexturePackLoadedEvent {

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
