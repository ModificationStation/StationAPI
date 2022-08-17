package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.registry.legacy.AbstractArrayBackedLegacyRegistry;
import net.modificationstation.stationapi.api.registry.serial.LegacyIDHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class ItemRegistry extends AbstractArrayBackedLegacyRegistry<ItemBase> {

    public static final RegistryKey<Registry<ItemBase>> KEY = RegistryKey.ofRegistry(MODID.id("items"));
    public static final ItemRegistry INSTANCE = Registry.create(KEY, new ItemRegistry(), Lifecycle.experimental());

    private ItemRegistry() {
        super(KEY, true);
    }

    @Override
    protected ItemBase[] getBackingArray() {
        return ItemBase.byId;
    }

    @Override
    public int getLegacyId(@NotNull ItemBase value) {
        return ((LegacyIDHolder) value).getLegacyID();
    }

    @Override
    public int getLegacyIdShift() {
        return BlockRegistry.INSTANCE.getSize();
    }

    @Override
    protected boolean setSize(int newSize) {
        if (!super.setSize(newSize))
            ItemBase.byId = Arrays.copyOf(ItemBase.byId, newSize);
        return true;
    }
}
