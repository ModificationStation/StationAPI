package net.modificationstation.stationapi.api.client.event.render.model;

import net.modificationstation.stationapi.api.client.registry.ItemModelPredicateProviderRegistry;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;

public class ItemModelPredicateProviderRegistryEvent extends RegistryEvent<ItemModelPredicateProviderRegistry> {
    public ItemModelPredicateProviderRegistryEvent() {
        super(ItemModelPredicateProviderRegistry.INSTANCE);
    }
}
