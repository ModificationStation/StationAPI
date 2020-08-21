package net.modificationstation.stationloader.impl.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationloader.impl.client.texture.TextureFactory;
import net.modificationstation.stationloader.impl.client.texture.TextureRegistry;

@Environment(EnvType.CLIENT)
public class StationLoader extends net.modificationstation.stationloader.impl.common.StationLoader {

    @Override
    public void setupAPI() {
        super.setupAPI();
        getLogger().info("Setting up TextureFactory...");
        net.modificationstation.stationloader.api.client.texture.TextureFactory.INSTANCE.setHandler(new TextureFactory());
        getLogger().info("Setting up TextureRegistry...");
        net.modificationstation.stationloader.api.client.texture.TextureRegistry.RUNNABLES.put("unbind", TextureRegistry::unbind);
        net.modificationstation.stationloader.api.client.texture.TextureRegistry.FUNCTIONS.put("getRegistry", TextureRegistry::getRegistry);
        net.modificationstation.stationloader.api.client.texture.TextureRegistry.SUPPLIERS.put("currentRegistry", TextureRegistry::currentRegistry);
        net.modificationstation.stationloader.api.client.texture.TextureRegistry.SUPPLIERS.put("registries", TextureRegistry::registries);
    }
}
