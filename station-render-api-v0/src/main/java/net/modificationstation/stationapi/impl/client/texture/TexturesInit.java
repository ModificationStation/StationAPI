package net.modificationstation.stationapi.impl.client.texture;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.mod.PreInitEvent;
import net.modificationstation.stationapi.api.factory.GeneralFactory;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.impl.client.model.CustomModelRenderer;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class TexturesInit {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void setupTextures(PreInitEvent event) {
        LOGGER.info("Setting up client GeneralFactory...");
        GeneralFactory.INSTANCE.addFactory(net.modificationstation.stationapi.api.client.model.CustomModelRenderer.class, (args) -> new CustomModelRenderer((String) args[0], (String) args[1]));
        LOGGER.info("Setting up TextureFactory...");
        net.modificationstation.stationapi.api.client.texture.TextureFactory.INSTANCE.setHandler(new TextureFactory());
        LOGGER.info("Setting up TextureRegistry...");
        TextureRegistry.RUNNABLES.put("unbind", net.modificationstation.stationapi.impl.client.texture.TextureRegistry::unbind);
        TextureRegistry.FUNCTIONS.put("getRegistry", net.modificationstation.stationapi.impl.client.texture.TextureRegistry::getRegistry);
        TextureRegistry.SUPPLIERS.put("currentRegistry", net.modificationstation.stationapi.impl.client.texture.TextureRegistry::currentRegistry);
        TextureRegistry.SUPPLIERS.put("registries", net.modificationstation.stationapi.impl.client.texture.TextureRegistry::registries);
    }
}
