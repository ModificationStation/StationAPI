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

    public static final ItemRegistry INSTANCE = new ItemRegistry(Identifier.of(ModID.of(StationLoader.INSTANCE), "items"));
}
