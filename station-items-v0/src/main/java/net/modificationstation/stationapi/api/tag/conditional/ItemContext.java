package net.modificationstation.stationapi.api.tag.conditional;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.context.Context;
import org.jetbrains.annotations.Nullable;

public final class ItemContext implements Context {
    private static final ThreadLocal<ItemContext> INSTANCE = ThreadLocal.withInitial(ItemContext::new);

    public static ItemContext of(ItemStack stack) {
        ItemContext ctx = INSTANCE.get();
        ctx.damage = stack.getDamage();
        return ctx;
    }

    private Integer damage;

    private ItemContext() {}

    @Override
    public <VALUE> @Nullable VALUE get(Key<VALUE> key) {
        if (key == ItemTagConditions.ITEM_DAMAGE) {
            //noinspection unchecked
            return (VALUE) damage;
        }
        return null;
    }
}
