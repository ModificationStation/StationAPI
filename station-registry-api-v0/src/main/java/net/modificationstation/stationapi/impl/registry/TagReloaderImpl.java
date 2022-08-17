package net.modificationstation.stationapi.impl.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.resource.ResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.event.resource.ResourcesReloadEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.DynamicRegistryManager;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.tag.TagManagerLoader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class TagReloaderImpl {

    private static DynamicRegistryManager dynamicRegistryManager;
    private static TagManagerLoader registryTagManager;

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerTagLoader(ResourceReloaderRegisterEvent event) {
        event.resourceManager.registerReloader(registryTagManager = new TagManagerLoader(dynamicRegistryManager = DynamicRegistryManager.createAndLoad()));
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4 - Integer.MAX_VALUE / 8)
    private static void refreshTagLoader(ResourcesReloadEvent event) {
        registryTagManager.getRegistryTags().forEach(tags -> repopulateTags(dynamicRegistryManager, tags));
    }

    private static <T> void repopulateTags(DynamicRegistryManager dynamicRegistryManager, TagManagerLoader.RegistryTags<T> tags) {
        RegistryKey<? extends Registry<T>> registryKey = tags.key();
        Map<TagKey<T>, List<RegistryEntry<T>>> map = tags.tags().entrySet().stream().collect(Collectors.toUnmodifiableMap(entry -> TagKey.of(registryKey, entry.getKey()), entry -> List.copyOf(entry.getValue())));
        dynamicRegistryManager.get(registryKey).populateTags(map);
    }
}
