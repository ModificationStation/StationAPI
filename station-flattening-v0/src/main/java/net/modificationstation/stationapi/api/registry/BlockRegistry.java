package net.modificationstation.stationapi.api.registry;

import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.block.context.BlockContext;
import net.modificationstation.stationapi.api.event.registry.RegistryAttribute;
import net.modificationstation.stationapi.api.event.registry.RegistryAttributeHolder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.context.ConditionType;
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
     * Creates a builder for a data-less tag condition that operates on a {@link BlockContext}.
     * <p>
     * This is a convenience overload that automatically projects the raw context
     * via {@link BlockContext#of(Context)}.
     *
     * @see #buildTagCondition(Identifier, Function, Predicate)
     */
    public ConditionType.Builder<Unit, BlockContext> buildBlockTagCondition(Identifier id, Predicate<BlockContext> condition) {
        return buildTagCondition(id, BlockContext::of, condition);
    }

    /**
     * Creates a builder for a data-backed tag condition that operates on a {@link BlockContext}.
     * <p>
     * This is a convenience overload that automatically projects the raw context
     * via {@link BlockContext#of(Context)}.
     *
     * @see #buildTagCondition(Identifier, MapCodec, Function, BiPredicate)
     */
    public <DATA> ConditionType.Builder<DATA, BlockContext> buildBlockTagCondition(Identifier id, MapCodec<DATA> codec, BiPredicate<DATA, BlockContext> condition) {
        return buildTagCondition(id, codec, BlockContext::of, condition);
    }
}
