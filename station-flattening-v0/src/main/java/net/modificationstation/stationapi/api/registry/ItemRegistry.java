package net.modificationstation.stationapi.api.registry;

import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.event.registry.RegistryAttribute;
import net.modificationstation.stationapi.api.event.registry.RegistryAttributeHolder;
import net.modificationstation.stationapi.api.item.context.ItemContext;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.context.ConditionType;
import net.modificationstation.stationapi.api.util.context.Context;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public final class ItemRegistry extends SimpleRegistry<Item> {
    public static final RegistryKey<Registry<Item>> KEY = RegistryKey.ofRegistry(NAMESPACE.id("items"));
    public static final ItemRegistry INSTANCE = Registries.create(KEY, new ItemRegistry(), Lifecycle.experimental());
    public static final int ID_SHIFT = 256;
    public static final Int2IntFunction SHIFTED_ID = id -> id - ID_SHIFT;
    public static final int AUTO_ID = SHIFTED_ID.get(Registry.AUTO_ID);

    private ItemRegistry() {
        super(KEY, Lifecycle.experimental(), true);
        RegistryAttributeHolder.get(this).addAttribute(RegistryAttribute.SYNCED);
    }

    /**
     * Creates a builder for a data-less tag condition that operates on an {@link ItemContext}.
     * <p>
     * This is a convenience overload that automatically projects the raw context
     * via {@link ItemContext#of(Context)}.
     *
     * @see #buildTagCondition(Identifier, Function, Predicate)
     */
    public ConditionType.Builder<Unit, ItemContext> buildItemTagCondition(Identifier id, Predicate<ItemContext> condition) {
        return buildTagCondition(id, ItemContext::of, condition);
    }

    /**
     * Creates a builder for a data-backed tag condition that operates on an {@link ItemContext}.
     * <p>
     * This is a convenience overload that automatically projects the raw context
     * via {@link ItemContext#of(Context)}.
     *
     * @see #buildTagCondition(Identifier, MapCodec, Function, BiPredicate)
     */
    public <DATA> ConditionType.Builder<DATA, ItemContext> buildItemTagCondition(Identifier id, MapCodec<DATA> codec, BiPredicate<DATA, ItemContext> condition) {
        return buildTagCondition(id, codec, ItemContext::of, condition);
    }
}
