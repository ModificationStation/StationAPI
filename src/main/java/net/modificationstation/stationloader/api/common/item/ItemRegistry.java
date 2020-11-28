package net.modificationstation.stationloader.api.common.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import net.modificationstation.stationloader.api.common.registry.ModID;
import net.modificationstation.stationloader.api.common.registry.SerializedRegistry;

public final class ItemRegistry extends SerializedRegistry<ItemBase> {

    public ItemRegistry(Identifier registryId) {
        super(registryId);
    }

    @Override
    public Identifier getIdentifier(ItemBase value) {
        BlockRegistry blocks = BlockRegistry.INSTANCE;
        int id = value.id;
        return id < blocks.getRegistrySize() ? blocks.getIdentifier(BlockBase.BY_ID[id]) : super.getIdentifier(value);
    }

    @Override
    public ItemBase getByIdentifier(Identifier identifier) {
        ItemBase value = super.getByIdentifier(identifier);
        BlockRegistry blocks = BlockRegistry.INSTANCE;
        if (value == null || value.id < blocks.getRegistrySize()) {
            BlockBase blockValue = blocks.getByIdentifier(identifier);
            return blockValue == null ? null : ItemBase.byId[blockValue.id];
        } else
            return value;
    }

    @Override
    public void registerSerializedValue(Identifier identifier, ItemBase value, int serializedId) {
        registerValue(identifier, value);
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
        ItemBase itemBase;
        for (int i = BlockRegistry.INSTANCE.getRegistrySize(); i < ItemBase.byId.length; i++) {
            itemBase = ItemBase.byId[i];
            if (itemBase != null && getIdentifier(itemBase) == null)
                registerValue(Identifier.of(itemBase.getTranslationKey() + "_" + itemBase.id), itemBase);
        }
    }

    @Override
    public int getRegistrySize() {
        return ItemBase.byId.length;
    }

    public static final ItemRegistry INSTANCE = new ItemRegistry(Identifier.of(ModID.of(StationLoader.INSTANCE), "items"));
}
