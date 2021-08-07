package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.event.texture.AfterTexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.binder.TexturePackDependent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class TextureRefresherImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void texturePackApplied(AfterTexturePackLoadedEvent event) {
        Atlas.getAtlases().forEach(Atlas::refreshTextures);
        //noinspection deprecation
        ((TextureManagerAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager).getTextureBinders().stream().filter(textureBinder -> textureBinder instanceof TexturePackDependent).forEach(textureBinder -> ((TexturePackDependent) textureBinder).refreshTextures());
    }
}
