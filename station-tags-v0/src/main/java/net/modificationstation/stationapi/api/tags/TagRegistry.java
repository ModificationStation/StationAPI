package net.modificationstation.stationapi.api.tags;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Not actually a registry.
 */
public class TagRegistry {

    private final Map<Identifier, TagEntryList> entries = new IdentityHashMap<>();

    public static TagRegistry INSTANCE = new TagRegistry();

    public void register(Identifier identifier, ItemInstance displayItem, Predicate<ItemInstance> predicate) {
        entries.computeIfAbsent(identifier, TagEntryList::new).add(new TagEntry(ItemRegistry.INSTANCE.getIdentifier(displayItem.getType()), displayItem, predicate, identifier));
    }

    public void register(TagEntry tagEntry) {
        entries.computeIfAbsent(tagEntry.fullTag, TagEntryList::new).add(tagEntry);
    }

    public void registerAll(Identifier identifier, Collection<TagEntry> predicateCollection) {
        entries.computeIfAbsent(identifier, (TagEntryList::new)).addAll(predicateCollection);
    }

    public @NotNull Optional<List<TagEntry>> get(@NotNull Identifier identifier) {
        if (identifier.id.endsWith("/")) {
            return Optional.of(entries.entrySet().stream().filter(id -> id.getKey().id.startsWith(identifier.id) || id.getKey().id.equals(identifier.id.substring(0, identifier.id.length()-1))).map(Map.Entry::getValue).flatMap(Collection::stream).collect(Collectors.toList()));
        }
        List<TagEntry> tagEntries = entries.get(identifier);
        return tagEntries == null? Optional.empty() : Optional.of(tagEntries);
    }

    public @NotNull Optional<Map<Identifier, List<TagEntry>>> getWithIdentifiers(@NotNull Identifier identifier) {
        if (identifier.id.endsWith("/")) {
            return Optional.of(entries.entrySet().stream().filter(id -> id.getKey().id.startsWith(identifier.id) || id.getKey().id.equals(identifier.id.substring(0, identifier.id.length()-1))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        }
        return Optional.of(entries.entrySet().stream().filter(id -> id.getKey().equals(identifier)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    /**
     * Checks if given tag Identifier is associated with the provided ItemInstance.
     * @param identifier Identifier to check.
     * @param itemInstance ItemInstance to verify.
     * @return true if ItemInstance matches Identifier.
     */
    public boolean tagMatches(Identifier identifier, ItemInstance itemInstance) {
        return get(identifier).map(tagEntries -> tagEntries.stream().anyMatch(e -> e.predicate.test(itemInstance))).orElse(false);
    }
}
