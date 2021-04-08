package net.modificationstation.stationapi.impl.oredict;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.*;

public class OreDictEntryObject {

    public final Identifier identifier;
    public Predicate<ItemInstance> itemInstancePredicate;

    public OreDictEntryObject(@NotNull Identifier identifier, @Nullable Predicate<ItemInstance> itemInstancePredicate) {
        this.identifier = identifier;
        this.itemInstancePredicate = itemInstancePredicate;
    }
}
