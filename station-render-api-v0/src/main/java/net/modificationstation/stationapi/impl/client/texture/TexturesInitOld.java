package net.modificationstation.stationapi.impl.client.texture;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.mod.PreInitEvent;
import net.modificationstation.stationapi.api.factory.GeneralFactory;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.impl.client.model.CustomModelRenderer;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

@Deprecated
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class TexturesInitOld {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void setupTextures(PreInitEvent event) {
        LOGGER.info("Setting up client GeneralFactory...");
        GeneralFactory.INSTANCE.addFactory(net.modificationstation.stationapi.api.client.model.CustomModelRenderer.class, (args) -> new CustomModelRenderer((String) args[0], (String) args[1]));
        LOGGER.info("Setting up TextureFactory...");
        net.modificationstation.stationapi.api.client.texture.TextureFactoryOld.INSTANCE.setHandler(new TextureFactoryOld());
        LOGGER.info("Setting up TextureRegistry...");
        TextureRegistryOld.RUNNABLES.put("unbind", TextureRegistryOld::unbind);
        TextureRegistryOld.FUNCTIONS.put("getRegistry", TextureRegistryOld::getRegistry);
        TextureRegistryOld.SUPPLIERS.put("currentRegistry", TextureRegistryOld::currentRegistry);
        TextureRegistryOld.SUPPLIERS.put("registries", TextureRegistryOld::registries);
    }
}
