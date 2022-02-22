package net.modificationstation.stationapi.api.client.registry;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.client.model.item.ModelPredicateProvider;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class ModelPredicateProviderRegistry extends Registry<ModelPredicateProvider> {

    public static final ModelPredicateProviderRegistry INSTANCE = new ModelPredicateProviderRegistry();

    private static final Identifier DAMAGED_ID = Identifier.of("damaged");
    private static final Identifier DAMAGE_ID = Identifier.of("damage");
    private static final ModelPredicateProvider DAMAGED_PROVIDER = (itemInstance, clientWorld, livingEntity) -> itemInstance.isDamaged() ? 1.0F : 0.0F;
    private static final ModelPredicateProvider DAMAGE_PROVIDER = (itemInstance, clientWorld, livingEntity) -> MathHelper.clamp((float)itemInstance.getDamage() / (float)itemInstance.getDurability(), 0.0F, 1.0F);
    private final Map<ItemBase, Map<Identifier, ModelPredicateProvider>> ITEM_SPECIFIC = new HashMap<>();

    private ModelPredicateProviderRegistry() {
        super(Identifier.of(MODID, "model_predicate_providers"));
    }

    public ModelPredicateProvider get(ItemBase item, Identifier identifier) {
        if (item.getDurability() > 0) {
            if (identifier == DAMAGE_ID)
                return DAMAGE_PROVIDER;

            if (identifier == DAMAGED_ID)
                return DAMAGED_PROVIDER;
        }

        Optional<ModelPredicateProvider> modelPredicateProvider = get(identifier);
        if (modelPredicateProvider.isPresent())
            return modelPredicateProvider.get();
        else {
            Map<Identifier, ModelPredicateProvider> map = ITEM_SPECIFIC.get(item);
            return map == null ? null : map.get(identifier);
        }
    }

    public void register(ItemBase item, Identifier id, ModelPredicateProvider provider) {
        ITEM_SPECIFIC.computeIfAbsent(item, itemx -> new IdentityHashMap<>()).put(id, provider);
    }
}
