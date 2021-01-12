package net.modificationstation.stationapi.api.common.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.stat.Stats;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.LevelRegistry;
import net.modificationstation.stationapi.impl.common.StationAPI;
import net.modificationstation.stationapi.mixin.common.accessor.ItemBaseAccessor;

public final class ItemRegistry extends LevelRegistry<ItemBase> {

    public static final ItemRegistry INSTANCE = new ItemRegistry(Identifier.of(StationAPI.MODID, "items"));

    private ItemRegistry(Identifier registryId) {
        super(registryId);
    }

    @Override
    public void load(CompoundTag tag) {
        forEach((identifier, item) -> {
            if (tag.containsKey(identifier.toString())) {
                int newID = tag.getInt(identifier.toString());
                if (newID != item.id)
                    remap(item, newID);
            }
        });
        Stats.onItemsRegistered();
    }

    private void remap(ItemBase item, int newID) {
        if (ItemBase.byId[newID] != null)
            setID(ItemBase.byId[newID], item.id);
        setID(item, newID);
    }

    private void setID(ItemBase item, int newID) {
        ItemBase.byId[newID] = item;
        ((ItemBaseAccessor) item).setId(newID);
    }

    @Override
    public void save(CompoundTag tag) {
        forEach((identifier, item) -> tag.put(identifier.toString(), item.id));
    }

    @Override
    public void registerSerializedValue(Identifier identifier, ItemBase value, int serializedId) {
        registerValue(identifier, value);
        serializedId += BlockRegistry.INSTANCE.getRegistrySize();
        ItemBase[] byId = ItemBase.byId;
        if (byId[serializedId] == null)
            byId[serializedId] = value;
    }

    @Override
    public int getNextSerializedID() {
        BlockRegistry blocks = BlockRegistry.INSTANCE;
        ItemBase[] byId = ItemBase.byId;
        for (int i = blocks.getRegistrySize(); i < byId.length; i++)
            if (byId[i] == null)
                return i - blocks.getRegistrySize();
        throw new RuntimeException("No free space left!");
    }

    @Override
    protected void update() {
        BlockRegistry blocks = BlockRegistry.INSTANCE;
        int id;
        for (ItemBase itemBase : ItemBase.byId)
            if (itemBase != null && getIdentifier(itemBase) == null) {
                id = itemBase.id;
                registerValue(id < blocks.getRegistrySize() ? blocks.getIdentifier(BlockBase.BY_ID[id]) : Identifier.of(itemBase.getTranslationKey() + "_" + id), itemBase);
            }
    }

    @Override
    public int getRegistrySize() {
        return ItemBase.byId.length;
    }
}
