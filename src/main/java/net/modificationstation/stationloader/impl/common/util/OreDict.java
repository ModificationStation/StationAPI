package net.modificationstation.stationloader.impl.common.util;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.item.ItemRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class OreDict {

    public static final OreDict ORE_DICT = new OreDict();

    private final HashMap<String, List<OreDictEntryObject>> oreDict = new HashMap<>();

    /**
     * Gets a list of item Identifiers that are assigned to the ore dict entry.
     * @param oreDictString The OreDict key.
     * @return A list of item Identifiers that can be used for functionality in recipe viewers.
     */
    public @Nullable List<OreDictEntryObject> getOreDictEntryObjects(@NotNull String oreDictString) {
        return oreDict.get(oreDictString);
    }

    /**
     * Adds given Identifier to the given OreDict entry.
     * @param oreDictString The OreDict key.
     * @param identifier The Identifier to add.
     */
    public void addID(@NotNull String oreDictString, @NotNull Identifier identifier) {
        addIDWithPredeicate(oreDictString, identifier, null);
    }

    // May be removed
    public void addIDWithPredeicate(@NotNull String oreDictString, @NotNull Identifier identifier, @Nullable Predicate<ItemInstance> identifierPredicate) {
        List<OreDictEntryObject> list = oreDict.computeIfAbsent(oreDictString, oDS -> new ArrayList<>());
        list.add(new OreDictEntryObject(identifier, identifierPredicate));
    }

    /**
     * Add contents of given list to the specified oreDict entry.
     * @param oreDictString The OreDict key.
     * @param identifiers A List of Identifiers.
     */
    public void addIDsWithoutPredicate(@NotNull String oreDictString, @NotNull List<@NotNull Identifier> identifiers) {
        addIDsWithPredicate(oreDictString, identifiers, null);
    }

    /**
     * Add contents of given list to the specified oreDict entry, all using the same predicate.
     * @param oreDictString The OreDict key.
     * @param identifiers A List of Identifiers.
     * @param itemInstancePredicate The Predicate used to determine more advanced parameters of your item.
     */
    public void addIDsWithPredicate(@NotNull String oreDictString, @NotNull List<Identifier> identifiers, @Nullable Predicate<ItemInstance> itemInstancePredicate) {
        List<OreDictEntryObject> list = oreDict.computeIfAbsent(oreDictString, oDS -> new ArrayList<>());
        identifiers.forEach((identifier -> list.add(new OreDictEntryObject(identifier, itemInstancePredicate))));
    }

    /**
     * Add contents of given list to the specified oreDict entry.
     * @param oreDictString The OreDict key.
     * @param oreDictEntryObjects The List of OreDictEntryObjects to add.
     */
    public void addOreDictEntryObjects(@NotNull String oreDictString, @NotNull List<@NotNull OreDictEntryObject> oreDictEntryObjects) {
        List<OreDictEntryObject> list = oreDict.computeIfAbsent(oreDictString, oDS -> new ArrayList<>());
        list.addAll(oreDictEntryObjects);
    }

    /**
     * Add provided itemInstance to the specified oreDict entry.
     * @param oreDictString The OreDict key.
     * @param itemInstance The itemInstance to add to the specified oreDict entry.
     */
    public void addItemInstance(String oreDictString, ItemInstance itemInstance) {
        List<OreDictEntryObject> list = oreDict.computeIfAbsent(oreDictString, oDS -> new ArrayList<>());
        list.add(new OreDictEntryObject(ItemRegistry.INSTANCE.getIdentifier(itemInstance.getType()), itemInstance::isEqualIgnoreFlags));
    }

    /**
     * Add the provided Block to the specified oreDict entry
     * @param oreDictString The OreDict key.
     * @param block The block to be added to the oreDict entry.
     */
    public void addItemInstanceIgnoreDamage(String oreDictString, BlockBase block) {
        List<OreDictEntryObject> list = oreDict.computeIfAbsent(oreDictString, oDS -> new ArrayList<>());
        list.add(new OreDictEntryObject(BlockRegistry.INSTANCE.getIdentifier(block), null));
    }

    /**
     * Add the provided Block to the specified oreDict entry
     * @param oreDictString The OreDict key.
     * @param item The item to be added to the oreDict entry.
     */
    public void addItemInstanceIgnoreDamage(String oreDictString, ItemBase item) {
        List<OreDictEntryObject> list = oreDict.computeIfAbsent(oreDictString, oDS -> new ArrayList<>());
        list.add(new OreDictEntryObject(ItemRegistry.INSTANCE.getIdentifier(item), null));
    }

    /**
     * Get if oreDict exists. Useful for certain circumstances.
     * @param oreDictString The OreDict key.
     * @return True if entry exists, false otherwise.
     */
    public boolean containsEntry(@NotNull String oreDictString) {
        return oreDict.containsKey(oreDictString);
    }

    /**
     * Get if Identifier is in given oreDict entry.
     * @param oreDictString The OreDict key to check for.
     * @param identifier The Identifier to check for.
     * @return True if entry AND Identifier exists inside said entry, false otherwise.
     */
    public boolean containsEntryAndIdentifier(@NotNull String oreDictString, @NotNull Identifier identifier) {
        List<OreDictEntryObject> entryObjects = oreDict.get(oreDictString);
        return entryObjects != null && entryObjects.stream().allMatch(entry -> entry.identifier == identifier);
    }

}
