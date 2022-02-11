package net.modificationstation.stationapi.api.registry;

import net.minecraft.item.ItemBase;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class ItemRegistry extends AbstractArrayBackedRegistry<ItemBase> {

    public static final ItemRegistry INSTANCE = new ItemRegistry(Identifier.of(MODID, "items"));

    private ItemRegistry(@NotNull Identifier identifier) {
        super(identifier, true);
    }

    @Override
    protected ItemBase[] getBackingArray() {
        return ItemBase.byId;
    }

    @Override
    public int getSerialIDShift() {
        return BlockRegistry.INSTANCE.getSize();
    }

    @Override
    public @NotNull Optional<ItemBase> get(@NotNull Identifier identifier) {
        Optional<ItemBase> item = super.get(identifier);
        if (item.isPresent())
            return item;
        else {
            OptionalInt serialID = BlockRegistry.INSTANCE.getSerialID(identifier);
            return serialID.isPresent() ? get(serialID.getAsInt()) : Optional.empty();
        }
    }

    @Override
    public @NotNull Identifier getIdentifier(@NotNull ItemBase value) {
        Identifier identifier = super.getIdentifier(value);
        //noinspection ConstantConditions
        return identifier == null ? BlockRegistry.INSTANCE.get(getSerialID(value)).map(BlockRegistry.INSTANCE::getIdentifier).orElse(null) : identifier;
    }

    @Override
    protected boolean setSize(int newSize) {
        if (!super.setSize(newSize))
            ItemBase.byId = Arrays.copyOf(ItemBase.byId, newSize);
        return true;
    }
}
