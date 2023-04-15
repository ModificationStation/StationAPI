package net.modificationstation.stationapi.impl.resource;

import net.modificationstation.stationapi.api.resource.ResourceType;

import java.util.function.Consumer;

public class DefaultResourcePackProvider implements ResourcePackProvider {
    @Override
    public void register(Consumer<ResourcePackProfile> consumer) {
        consumer.accept(
                ResourcePackProfile.create(
                        "vanilla",
                        "fixText",
                        true,
                        name -> new DefaultResourcePack(),
                        ResourceType.CLIENT_RESOURCES,
                        ResourcePackProfile.InsertionPosition.BOTTOM,
                        ResourcePackSource.BUILTIN
                )
        );
    }
}
