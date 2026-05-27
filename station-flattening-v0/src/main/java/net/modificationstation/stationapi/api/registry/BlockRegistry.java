package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.event.registry.RegistryAttribute;
import net.modificationstation.stationapi.api.event.registry.RegistryAttributeHolder;
import net.modificationstation.stationapi.api.block.BlockContext;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.context.Context;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public final class BlockRegistry extends SimpleRegistry<Block> {
    public static final RegistryKey<Registry<Block>> KEY = RegistryKey.ofRegistry(NAMESPACE.id("blocks"));
    public static final BlockRegistry INSTANCE = Registries.create(KEY, new BlockRegistry(), Lifecycle.experimental());

    private BlockRegistry() {
        super(KEY, Lifecycle.experimental(), true);
        RegistryAttributeHolder.get(this).addAttribute(RegistryAttribute.SYNCED);
        nextId = 256;
    }

    /**
     * Registers a data-less tag condition that operates on a {@link BlockContext}.
     * <p>
     * This is a convenience overload that automatically projects the raw context
     * via {@link BlockContext#of(Context)}.
     *
     * @see #registerTagCondition(Identifier, Function, Predicate)
     */
    public void registerBlockTagCondition(Identifier id, Predicate<BlockContext> condition) {
        registerTagCondition(id, BlockContext::of, condition);
    }

    /**
     * Registers a data-backed tag condition that operates on a {@link BlockContext}.
     * <p>
     * This is a convenience overload that automatically projects the raw context
     * via {@link BlockContext#of(Context)}.
     *
     * @see #registerTagCondition(Identifier, MapCodec, Function, BiPredicate)
     */
    public <DATA> void registerBlockTagCondition(Identifier id, MapCodec<DATA> codec, BiPredicate<DATA, BlockContext> condition) {
        registerTagCondition(id, codec, BlockContext::of, condition);
    }
}
