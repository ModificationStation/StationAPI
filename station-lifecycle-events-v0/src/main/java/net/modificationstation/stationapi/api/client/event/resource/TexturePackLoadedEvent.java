package net.modificationstation.stationapi.api.client.event.resource;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.class_285;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.StationAPI;

@SuperBuilder
public abstract class TexturePackLoadedEvent extends Event {
    public final TextureManager textureManager;
    public final class_285 newTexturePack;

    @SuperBuilder
    public static class Before extends TexturePackLoadedEvent {}

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class After extends TexturePackLoadedEvent {}
}
