package net.modificationstation.stationapi.api.tags;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

import java.util.function.*;

public class TagEntry {

    public final Identifier identifier;
    public final ItemInstance displayItem;
    public final Predicate<ItemInstance> predicate;
    public final Identifier fullTag;

    public TagEntry(Identifier identifier, ItemInstance displayItem, Predicate<ItemInstance> predicate, Identifier fullTag) {
        this.identifier = identifier;
        this.displayItem = displayItem;
        this.predicate = predicate;
        this.fullTag = fullTag;
    }

    public TagEntry(ItemInstance displayItem, Predicate<ItemInstance> predicate, Identifier fullTag) {
        this.identifier = ItemRegistry.INSTANCE.getIdentifier(displayItem.getType());
        this.displayItem = displayItem;
        this.predicate = predicate;
        this.fullTag = fullTag;
    }

    public boolean tagMatches(Identifier otherTag) {
        return otherTag.id.endsWith("/")? fullTag.toString().startsWith(otherTag.toString()) || fullTag.toString().equals(otherTag.toString().substring(0, otherTag.toString().length()-1)) : fullTag.equals(otherTag);
    }
}
