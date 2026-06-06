package net.modificationstation.stationapi.api.item.context;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.tag.context.TagEvaluationContext;
import net.modificationstation.stationapi.api.util.context.Context;

public interface ItemTagContext extends ItemContext, TagEvaluationContext {
    ItemTagContext DEFAULT = of(TagEvaluationContext.DEFAULT);
    ItemTagContext BYPASSED = of(TagEvaluationContext.BYPASSED);

    static ItemTagContext of(ItemStack stack) {
        Context data = ItemContext.of(stack);
        interface ItemTagContextDelegate extends ItemTagContext, Delegate {}
        return (ItemTagContextDelegate) () -> data;
    }

    static ItemTagContext of(ItemStack stack, boolean ignoreTagConditions) {
        Context data = ItemContext.of(stack).with(TagEvaluationContext.of(ignoreTagConditions));
        interface ItemTagContextDelegate extends ItemTagContext, Delegate {}
        return (ItemTagContextDelegate) () -> data;
    }

    static ItemTagContext of(Context context) {
        if (context instanceof ItemTagContext i) return i;
        interface ItemTagContextDelegate extends ItemTagContext, Delegate {}
        return (ItemTagContextDelegate) () -> context;
    }

}
