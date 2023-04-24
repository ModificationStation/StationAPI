package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.block.BlockBase;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class BlockRegistry extends SimpleRegistry<BlockBase> {

    public static final RegistryKey<Registry<BlockBase>> KEY = RegistryKey.ofRegistry(MODID.id("blocks"));
    public static final BlockRegistry INSTANCE = Registries.create(KEY, new BlockRegistry(), registry -> BlockBase.BY_ID[0], Lifecycle.experimental());

    private BlockRegistry() {
        super(KEY, Lifecycle.experimental(), true);
    }
}
