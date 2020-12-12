package net.modificationstation.stationloader.impl.common.util;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class OreDict {

    public static final OreDict ORE_DICT = new OreDict();

    private HashMap<String, List<OreDictEntryObject>> oreDict = new HashMap<>();

    /**
     * Gets a list of item Identifiers that are assigned to the ore dict entry.
     * @param oreDictString The OreDict key.
     * @return A list of item Identifiers that can be used for.
     */
    public @Nullable List<OreDictEntryObject> getOreDictEntryObjects(@NotNull String oreDictString) {
        return oreDict.get(oreDictString);
    }

    // May be removed
    public void addID(@NotNull String oreDictString, @NotNull Identifier item) {
        addIDWithPredeicate(oreDictString, item, null);
    }

    // May be removed
    public void addIDWithPredeicate(@NotNull String oreDictString, @NotNull Identifier item, @Nullable Predicate<ItemInstance> identifierPredicate) {
        List<OreDictEntryObject> list = oreDict.computeIfAbsent(oreDictString, oDS -> new ArrayList<>());
        list.add(new OreDictEntryObject(item, identifierPredicate));
    }

    /**
     * Add contents of given list to the specified oreDict entry.
     * @param oreDictString The OreDict key.
     * @param items A List of Identifiers.
     */
    public void addIDsWithoutPredicate(@NotNull String oreDictString, @NotNull List<@NotNull Identifier> items) {
        addIDsWithPredicate(oreDictString, items, null);
    }

    /**
     * Add contents of given list to the specified oreDict entry.
     * @param oreDictString The OreDict key.
     * @param items A List of Identifiers.
     */
    public void addIDsWithPredicate(@NotNull String oreDictString, @NotNull List<Identifier> items, @Nullable Predicate<ItemInstance> itemInstancePredicate) {
        List<OreDictEntryObject> list = oreDict.computeIfAbsent(oreDictString, oDS -> new ArrayList<>());
        items.forEach((identifier -> list.add(new OreDictEntryObject(identifier, itemInstancePredicate))));
    }

    /**
     * Add contents of given list to the specified oreDict entry.
     * @param oreDictString
     * @param oreDictEntryObjects
     */
    public void addOreDictEntryObjects(@NotNull String oreDictString, @NotNull List<@NotNull OreDictEntryObject> oreDictEntryObjects) {
        List<OreDictEntryObject> list = oreDict.computeIfAbsent(oreDictString, oDS -> new ArrayList<>());
        list.addAll(oreDictEntryObjects);
    }

    /**
     * Get if oreDict exists. Useful for certain circumstances.
     * @param oreDictString The OreDict key.
     * @return
     */
    public boolean containsEntry(@NotNull String oreDictString) {
        return oreDict.containsKey(oreDictString);
    }

    /**
     *
     * @param oreDictString The OreDict key to check for.
     * @param identifier The Identifier to check for.
     * @return
     */
    public boolean contains(@NotNull String oreDictString, @NotNull Identifier identifier) {
        List<OreDictEntryObject> entryObjects = oreDict.get(oreDictString);
        return entryObjects != null && entryObjects.stream().allMatch(entry -> entry.identifier == identifier);
    }

}
