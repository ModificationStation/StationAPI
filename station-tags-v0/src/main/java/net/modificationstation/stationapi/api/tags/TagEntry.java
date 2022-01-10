package net.modificationstation.stationapi.api.tags;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.function.*;

public class TagEntry {

    public final Identifier id;
    public final Predicate<ItemInstance> predicate;

    public TagEntry(Identifier id, Predicate<ItemInstance> predicate) {
        this.id = id;
        this.predicate = predicate;
    }
}
