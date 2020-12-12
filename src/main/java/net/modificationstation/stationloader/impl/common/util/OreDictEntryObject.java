package net.modificationstation.stationloader.impl.common.util;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class OreDictEntryObject {

    Identifier identifier;
    Predicate<ItemInstance> itemInstancePredicate;

    public OreDictEntryObject(@NotNull Identifier identifier, @Nullable Predicate<ItemInstance> itemInstancePredicate) {
        this.identifier = identifier;
        this.itemInstancePredicate = itemInstancePredicate;
    }
}
