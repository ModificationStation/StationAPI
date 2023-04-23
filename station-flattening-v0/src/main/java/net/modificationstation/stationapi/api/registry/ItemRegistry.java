package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class ItemRegistry extends SimpleRegistry<ItemBase> {

    public static final RegistryKey<Registry<ItemBase>> KEY = RegistryKey.ofRegistry(MODID.id("items"));
    public static final ItemRegistry INSTANCE = Registry.create(KEY, new ItemRegistry(), Lifecycle.experimental());
    public static final Int2IntFunction SHIFTED_ID = id -> id - BlockBase.BY_ID.length;

    private ItemRegistry() {
        super(KEY, Lifecycle.experimental(), ItemBase::getRegistryEntry);
    }
}
