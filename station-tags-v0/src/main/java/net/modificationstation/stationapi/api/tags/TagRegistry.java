package net.modificationstation.stationapi.api.tags;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class TagRegistry extends Registry<List<Predicate<ItemInstance>>> {

    public static TagRegistry INSTANCE = new TagRegistry(Identifier.of(StationAPI.MODID, "tags"));

    /**
     * Default registry constructor.
     *
     * @param identifier registry's identifier.
     */
    public TagRegistry(@NotNull Identifier identifier) {
        super(identifier);
    }

    public void register(Identifier identifier, Predicate<ItemInstance> predicate) {
        computeIfAbsent(identifier, (identifier1) -> new ArrayList<>()).add(predicate);
    }

    public void registerAll(Identifier identifier, Collection<Predicate<ItemInstance>> predicateCollection) {
        computeIfAbsent(identifier, (identifier1 -> new ArrayList<>())).addAll(predicateCollection);
    }

    @Override
    @Deprecated
    public void register(@NotNull Identifier identifier, @NotNull List<Predicate<ItemInstance>> value) {
        super.register(identifier, value);
    }

    @Override
    public @NotNull Optional<List<Predicate<ItemInstance>>> get(@NotNull Identifier identifier) {
        if (identifier.id.endsWith("/")) {
            return Optional.of(values.entrySet().stream().filter(id -> id.getKey().id.startsWith(identifier.id) || id.getKey().id.equals(identifier.id.substring(0, identifier.id.length()-1))).map(Map.Entry::getValue).flatMap(Collection::stream).collect(Collectors.toList()));
        }
        return super.get(identifier);
    }

    public @NotNull Optional<Map<Identifier, List<Predicate<ItemInstance>>>> getWithIdentifiers(@NotNull Identifier identifier) {
        if (identifier.id.endsWith("/")) {
            return Optional.of(values.entrySet().stream().filter(id -> id.getKey().modID.equals(identifier.modID) && id.getKey().id.startsWith(identifier.id)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        }
        return Optional.of(values.entrySet().stream().filter(id -> id.getKey().equals(identifier)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

    }

    /**
     * Checks if given tag Identifier is associated with the provided ItemInstance.
     * @param identifier Identifier to check.
     * @param itemInstance ItemInstance to verify.
     * @return true if ItemInstance matches Identifier.
     */
    public boolean matches(Identifier identifier, ItemInstance itemInstance) {
        Optional<List<Predicate<ItemInstance>>> predicates = get(identifier);
        return predicates.isPresent() && predicates.get().stream().anyMatch(predicate -> predicate.test(itemInstance));
    }
}
