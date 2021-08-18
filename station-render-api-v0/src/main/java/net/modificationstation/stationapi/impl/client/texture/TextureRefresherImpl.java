package net.modificationstation.stationapi.impl.client.texture;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.client.event.texture.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.binder.TexturePackDependent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.stream.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class TextureRefresherImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void beforeTexturePackApplied(TexturePackLoadedEvent.Before event) {
        Map<String, Integer> textureMap = ((TextureManagerAccessor) event.textureManager).getField_1246();
        textureMap.keySet().stream().filter(s -> event.newTexturePack.getResourceAsStream(s) == null).collect(Collectors.toList()).forEach(s -> GL11.glDeleteTextures(textureMap.remove(s)));
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void texturePackApplied(TexturePackLoadedEvent.After event) {
        Atlas.getAtlases().forEach(Atlas::refreshTextures);
        ((TextureManagerAccessor) event.textureManager).getTextureBinders().stream().filter(textureBinder -> textureBinder instanceof TexturePackDependent).forEach(textureBinder -> ((TexturePackDependent) textureBinder).refreshTextures(event.newTexturePack));
    }
}
