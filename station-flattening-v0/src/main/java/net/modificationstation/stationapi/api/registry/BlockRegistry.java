package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.event.registry.RegistryAttribute;
import net.modificationstation.stationapi.api.event.registry.RegistryAttributeHolder;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public final class BlockRegistry extends SimpleRegistry<Block> {

    public static final RegistryKey<Registry<Block>> KEY = RegistryKey.ofRegistry(NAMESPACE.id("blocks"));
    public static final BlockRegistry INSTANCE = Registries.create(KEY, new BlockRegistry(), registry -> Block.BLOCKS[0], Lifecycle.experimental());

    private BlockRegistry() {
        super(KEY, Lifecycle.experimental(), true);
        RegistryAttributeHolder.get(this).addAttribute(RegistryAttribute.SYNCED);
    }
}
