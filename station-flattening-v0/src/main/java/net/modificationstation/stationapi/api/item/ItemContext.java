package net.modificationstation.stationapi.api.item;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.context.Context;
import org.jetbrains.annotations.Nullable;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

/**
 * Context for item-related evaluation.
 */
@FunctionalInterface
public interface ItemContext extends Context {
    ItemContext EMPTY = id -> null;

    /**
     * The context key used to evaluate item-related conditions in an item context.
     */
    Key<ItemStack> ITEM_STACK = new Key<>(NAMESPACE.id("item_stack"));

    /**
     * The context key used to evaluate item damage conditions.
     */
    Key<Integer> ITEM_DAMAGE = new Key<>(NAMESPACE.id("item_damage"));

    /**
     * {@return whether this context has item damage data}
     */
    default boolean hasDamage() {
        return contains(ITEM_DAMAGE) || itemStack() != null;
    }

    @FunctionalInterface
    interface DataProvider extends ItemContext {
        @Override
        @Nullable ItemStack itemStack();

        @Override
        default int damage() {
            ItemStack stack = itemStack();
            return stack == null ? 0 : stack.getDamage();
        }

        @Override
        default Object getRaw(Identifier id) {
            if (ITEM_STACK.id() == id) return itemStack();
            if (ITEM_DAMAGE.id() == id) return itemStack() == null ? null : damage();
            return null;
        }

        @Override
        default int getIntRaw(Identifier id, int defaultValue) {
            if (ITEM_DAMAGE.id() == id) return damage();
            return ItemContext.super.getIntRaw(id, defaultValue);
        }
    }

    /**
     * {@return the item stack being evaluated}
     */
    default @Nullable ItemStack itemStack() { return get(ITEM_STACK); }

    /**
     * {@return the item damage}
     */
    default int damage() {
        Integer dmg = get(ITEM_DAMAGE);
        if (dmg != null) return dmg;
        ItemStack stack = itemStack();
        return stack == null ? 0 : stack.getDamage();
    }

    /**
     * Creates a new item context.
     */
    static ItemContext of(ItemStack stack) {
        return (DataProvider) () -> stack;
    }

    /**
     * Projects a generic context into an item context view.
     * 
     * @param context the context to project
     * @return the item context view
     */
    static ItemContext of(Context context) {
        if (context instanceof ItemContext i) return i;
        interface ItemContextDelegate extends ItemContext, Delegate {}
        return (ItemContextDelegate) () -> context;
    }
}
