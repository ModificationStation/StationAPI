package net.modificationstation.stationapi.api.common.util;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.impl.common.util.OreDictEntryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;

public class OreDict {

    public static final OreDict INSTANCE = new OreDict();

    private final HashMap<String, List<OreDictEntryObject>> oreDict = new HashMap<>();
    private final HashMap<Identifier, List<String>> identifierToOreDictString = new HashMap<>();

    /**
     * Gets a list of item Identifiers that are assigned to the ore dict entry.
     *
     * @param oreDictString The OreDict key.
     * @return A list of item Identifiers that can be used for functionality in recipe viewers.
     */
    public @Nullable List<@NotNull OreDictEntryObject> getOreDictEntryObjects(@NotNull String oreDictString) {
        return oreDict.get(oreDictString);
    }

    public @Nullable List<@NotNull String> getOreDictStringsFromIdentifier(@NotNull Identifier identifier) {
        return identifierToOreDictString.get(identifier);
    }

    /**
     * Add provided itemInstance to the specified oreDict entry.
     *
     * @param oreDictString The OreDict key.
     * @param itemInstance  The itemInstance to add to the specified oreDict entry.
     */
    public void addItemInstanceWithPredicate(@NotNull String oreDictString, @NotNull ItemInstance itemInstance, @Nullable Predicate<ItemInstance> itemInstancePredicate) {
        Identifier identifier = ItemRegistry.INSTANCE.getIdentifier(itemInstance.getType());
        List<String> identifierList = identifierToOreDictString.computeIfAbsent(identifier, identifier1 -> new ArrayList<>());
        identifierList.add(oreDictString);
        List<OreDictEntryObject> list = oreDict.computeIfAbsent(oreDictString, oDS -> new ArrayList<>());
        list.add(new OreDictEntryObject(ItemRegistry.INSTANCE.getIdentifier(itemInstance.getType()), itemInstancePredicate));
    }

    /**
     * Add provided itemInstance to the specified oreDict entry.
     *
     * @param oreDictString The OreDict key.
     * @param itemInstance  The itemInstance to add to the specified oreDict entry.
     */
    public void addItemInstance(@NotNull String oreDictString, @NotNull ItemInstance itemInstance) {
        addItemInstanceWithPredicate(oreDictString, itemInstance, itemInstance::isDamageAndIDIdentical);
    }

    /**
     * Add provided itemInstance to the specified oreDict entry.
     *
     * @param oreDictString The OreDict key.
     * @param itemInstance  The itemInstance to add to the specified oreDict entry.
     */
    public void addItemInstanceIgnoreDamage(@NotNull String oreDictString, @NotNull ItemInstance itemInstance) {
        addItemInstanceWithPredicate(oreDictString, itemInstance, itemInstance1 -> itemInstance.itemId == itemInstance1.itemId);
    }

    /**
     * Add the provided Block to the specified oreDict entry
     *
     * @param oreDictString The OreDict key.
     * @param block         The block to be added to the oreDict entry.
     */
    public void addBlockIgnoreDamage(@NotNull String oreDictString, @NotNull BlockBase block) {
        ItemInstance itemInstance = new ItemInstance(block);
        addItemInstanceWithPredicate(oreDictString, itemInstance, itemInstance1 -> itemInstance.itemId == itemInstance1.itemId);
    }

    /**
     * Add the provided Block to the specified oreDict entry
     *
     * @param oreDictString The OreDict key.
     * @param item          The item to be added to the oreDict entry.
     */
    public void addItemIgnoreDamage(@NotNull String oreDictString, @NotNull ItemBase item) {
        ItemInstance itemInstance = new ItemInstance(item);
        addItemInstanceWithPredicate(oreDictString, itemInstance, itemInstance1 -> itemInstance.itemId == itemInstance1.itemId);
    }

    /**
     * Get if oreDict exists. Useful for certain circumstances.
     *
     * @param oreDictString The OreDict key.
     * @return True if entry exists, false otherwise.
     */
    public boolean containsEntry(@NotNull String oreDictString) {
        return oreDict.containsKey(oreDictString);
    }

    /**
     * Get if Identifier is in given oreDict entry.
     *
     * @param oreDictString The OreDict key to check for.
     * @param identifier    The Identifier to check for.
     * @return True if entry AND Identifier exists inside said entry, false otherwise.
     */
    public boolean containsEntryAndIdentifier(@NotNull String oreDictString, @NotNull Identifier identifier) {
        List<OreDictEntryObject> entryObjects = oreDict.get(oreDictString);
        return entryObjects != null && entryObjects.stream().allMatch(entry -> entry.identifier == identifier);
    }

    /**
     * Checks the provided item is valid for the provided OreDict string.
     * @param oreDictString The OreDict string to check.
     * @param itemInstance The ItemInstance to be checked.
     * @return True if item is valid for given OreDict string, False otherwise.
     */
    public boolean matches(@NotNull String oreDictString, ItemInstance itemInstance) {
        List<OreDictEntryObject> entryObjects = oreDict.get(oreDictString);
        return entryObjects != null && entryObjects.stream().anyMatch(oreDictEntryObject -> oreDictEntryObject.itemInstancePredicate.test(itemInstance));
    }

    /**
     * Returns all current OreDict strings as a Set.
     * @return All current OreDict string.
     */
    public Set<String> dumpOreDictStrings() {
        return oreDict.keySet();
    }

}
