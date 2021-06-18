package net.modificationstation.stationapi.api.registry;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.mixin.item.ItemBaseAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class ItemRegistry extends LevelSerialRegistry<ItemBase> {

    public static final ItemRegistry INSTANCE = new ItemRegistry(Identifier.of(MODID, "items"));

    private ItemRegistry(@NotNull Identifier identifier) {
        super(identifier);
    }

    @Override
    protected void remap(int newSerialID, ItemBase value) {
        ItemBase.byId[value.id] = null;
        if (ItemBase.byId[newSerialID] != null)
            remap(getNextSerialID(), ItemBase.byId[newSerialID]);
        ((ItemBaseAccessor) value).setId(newSerialID);
        ItemBase.byId[newSerialID] = value;
    }

    @Override
    public int getSize() {
        return ItemBase.byId.length;
    }

    @Override
    public int getSerialID(ItemBase value) {
        return value.id;
    }

    @Override
    public Optional<ItemBase> get(int serialID) {
        try {
            return Optional.ofNullable(ItemBase.byId[serialID]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    @Override
    public int getFirstSerialID() {
        return BlockRegistry.INSTANCE.getSize();
    }

    @Override
    public Optional<ItemBase> get(@NotNull Identifier identifier) {
        Optional<ItemBase> item = super.get(identifier);
        return item.isPresent() ? item : get(BlockRegistry.INSTANCE.getSerialID(identifier));
    }
}
