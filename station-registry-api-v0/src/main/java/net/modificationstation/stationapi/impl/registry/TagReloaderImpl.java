package net.modificationstation.stationapi.impl.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.resource.DataReloadEvent;
import net.modificationstation.stationapi.api.event.resource.DataResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.*;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.tag.TagManagerLoader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.mine_diver.unsafeevents.listener.ListenerPriority.LOW;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class TagReloaderImpl {

    private static DynamicRegistryManager dynamicRegistryManager;
    private static TagManagerLoader registryTagManager;

    @EventListener
    private static void registerTagLoader(DataResourceReloaderRegisterEvent event) {
        event.resourceManager.registerReloader(registryTagManager = new TagManagerLoader(dynamicRegistryManager = ServerDynamicRegistryType.createCombinedDynamicRegistries().getPrecedingRegistryManagers(ServerDynamicRegistryType.RELOADABLE)));
    }

    @EventListener(priority = LOW)
    private static void refreshTagLoader(DataReloadEvent event) {
        registryTagManager.getRegistryTags().forEach(tags -> repopulateTags(dynamicRegistryManager, tags));
    }

    private static <T> void repopulateTags(DynamicRegistryManager dynamicRegistryManager, TagManagerLoader.RegistryTags<T> tags) {
        RegistryKey<? extends Registry<T>> registryKey = tags.key();
        Map<TagKey<T>, List<RegistryEntry<T>>> map = tags.tags().entrySet().stream().collect(Collectors.toUnmodifiableMap(entry -> TagKey.of(registryKey, entry.getKey()), entry -> List.copyOf(entry.getValue())));
        dynamicRegistryManager.get(registryKey).populateTags(map);
    }
}
