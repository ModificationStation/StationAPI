package net.modificationstation.stationapi.api.client.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.render.model.ItemModelPredicateProviderRegistryEvent;
import net.modificationstation.stationapi.api.client.model.item.ItemModelPredicateProvider;
import net.modificationstation.stationapi.api.registry.Registries;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.IdentityHashMap;
import java.util.Map;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public final class ItemModelPredicateProviderRegistry extends SimpleRegistry<ItemModelPredicateProvider> {

    private static final ItemModelPredicateProvider EMPTY = (stack, world, entity, seed) -> 0;
    public static final RegistryKey<ItemModelPredicateProviderRegistry> KEY = RegistryKey.ofRegistry(NAMESPACE.id("item_model_predicate_providers"));
    public static final ItemModelPredicateProviderRegistry INSTANCE = Registries.create(KEY, new ItemModelPredicateProviderRegistry(), registry -> EMPTY, Lifecycle.experimental());

    private static final Identifier DAMAGED_ID = Identifier.of("damaged");
    private static final Identifier DAMAGE_ID = Identifier.of("damage");
    private static final Identifier META_ID = Identifier.of("meta");
    private static final ItemModelPredicateProvider DAMAGED_PROVIDER = (stack, clientWorld, livingEntity, seed) -> stack.isDamaged() ? 1.0F : 0.0F;
    private static final ItemModelPredicateProvider DAMAGE_PROVIDER = (stack, clientWorld, livingEntity, seed) -> MathHelper.clamp((float)stack.getDamage() / (float)stack.getMaxDamage(), 0.0F, 1.0F);
    private static final ItemModelPredicateProvider META_PROVIDER = (stack, clientWorld, livingEntity, seed) -> MathHelper.clamp((float)stack.getDamage(), 0, 65535);
    private final Map<Item, Map<Identifier, ItemModelPredicateProvider>> ITEM_SPECIFIC = new IdentityHashMap<>();

    private ItemModelPredicateProviderRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }

    public ItemModelPredicateProvider get(Item item, Identifier identifier) {
        if (item.getMaxDamage() > 0) {
            if (identifier == DAMAGE_ID)
                return DAMAGE_PROVIDER;

            if (identifier == DAMAGED_ID)
                return DAMAGED_PROVIDER;
        }
        if (item.hasSubtypes()) if (identifier == META_ID)
            return META_PROVIDER;

        ItemModelPredicateProvider modelPredicateProvider = get(identifier);
        if (modelPredicateProvider == null) {
            Map<Identifier, ItemModelPredicateProvider> map = ITEM_SPECIFIC.get(item);
            return map == null ? null : map.get(identifier);
        } else return modelPredicateProvider;
    }

    public void register(Item item, Identifier id, ItemModelPredicateProvider provider) {
        ITEM_SPECIFIC.computeIfAbsent(item, itemx -> new IdentityHashMap<>()).put(id, provider);
    }

    static {
        StationAPI.EVENT_BUS.post(new ItemModelPredicateProviderRegistryEvent());
    }
}
