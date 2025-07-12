package net.modificationstation.stationapi.api.client.event.render.model;

import net.modificationstation.stationapi.api.client.model.item.ItemModelPredicateProvider;
import net.modificationstation.stationapi.api.client.registry.ItemModelPredicateProviderRegistry;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;

public class ItemModelPredicateProviderRegistryEvent extends RegistryEvent.EntryTypeBound<ItemModelPredicateProvider, ItemModelPredicateProviderRegistry> {
    public ItemModelPredicateProviderRegistryEvent() {
        super(ItemModelPredicateProviderRegistry.INSTANCE);
    }
}
