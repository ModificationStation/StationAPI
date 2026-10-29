package net.modificationstation.stationapi.impl.world.chunk;

import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedEvent;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The palette that global sections share: every block state, numbered densely in {@link Block#STATE_IDS} order.
 *
 * <p>The numbering is the sections' own. Saves and packets never carry these indices: saves write states, packets
 * raw IDs through {@link #rawIds()}, so it doesn't have to match raw IDs. It's rebuilt lazily after any block
 * registration or remap: those rebuild {@link Block#STATE_IDS} too, and listeners here can't rely on running after
 * that. A rebuild that comes out in the same order keeps the same array, so sections only see a new array when the
 * numbering really changed.
 *
 * <p>Not thread-safe: section writes, packets and registry remaps all happen on the game thread. Reads of global
 * sections never come here.
 */
final class GlobalStates {
    private static boolean stale = true;
    private static BlockState[] states = new BlockState[0];
    private static int bits;
    private static int[] rawIds = new int[0];
    private static int rawIdLimit;
    private static int[] blockIds = new int[0];

    static {
        BlockRegistry.INSTANCE.getEventBus().register(Listener.<RegistryIdRemapEvent<Block>>simple()
                .listener(event -> stale = true)
                .phase(StationAPI.INTERNAL_PHASE)
                .build());
        BlockRegistry.INSTANCE.getEventBus().register(Listener.<RegistryEntryAddedEvent<Block>>simple()
                .listener(event -> stale = true)
                .phase(StationAPI.INTERNAL_PHASE)
                .build());
    }

    private GlobalStates() {}

    /**
     * {@return every state, at its index}
     * Global sections point their palette at this array; a section whose palette is no longer it is encoded
     * against an older numbering.
     */
    static BlockState[] states() {
        if (stale) rebuild();
        return states;
    }

    /** {@return the index width of a global section} */
    static int bits() {
        if (stale) rebuild();
        return bits;
    }

    /** {@return the raw state ID at each index of {@link #states()}} */
    static int[] rawIds() {
        if (stale) rebuild();
        return rawIds;
    }

    /**
     * {@return one past the largest raw state ID}
     * Not {@code STATE_IDS.size()}, which counts states and falls short if the raw IDs have holes.
     */
    static int rawIdLimit() {
        if (stale) rebuild();
        return rawIdLimit;
    }

    /**
     * {@return the block ID at each index of {@link #states()}}
     * Global sections read block IDs through it. It keeps the same array while the IDs don't change.
     */
    static int[] blockIds() {
        if (stale) rebuild();
        return blockIds;
    }

    static int indexOf(BlockState state) {
        if (stale) rebuild();
        int i = state.globalIndex;
        if (i < 0 || i >= states.length || states[i] != state)
            throw new IllegalStateException("Block state " + state + " isn't in Block.STATE_IDS");
        return i;
    }

    private static void rebuild() {
        List<BlockState> list = new ArrayList<>(Block.STATE_IDS.size());
        for (BlockState state : Block.STATE_IDS) list.add(state);
        BlockState[] old = states;
        boolean same = old.length == list.size();
        for (int i = 0; same && i < old.length; i++) same = old[i] == list.get(i);
        if (!same) {
            BlockState[] next = list.toArray(new BlockState[0]);
            for (int i = 0; i < next.length; i++) next[i].globalIndex = i;
            states = next;
            bits = next.length <= 1 ? 0 : 32 - Integer.numberOfLeadingZeros(next.length - 1);
        }
        // raw IDs can move even when the order doesn't
        int[] ids = new int[states.length];
        int limit = 0;
        for (int i = 0; i < ids.length; i++) limit = Math.max(limit, (ids[i] = Block.STATE_IDS.getRawId(states[i])) + 1);
        rawIds = ids;
        rawIdLimit = limit;
        int[] blocks = new int[states.length];
        for (int i = 0; i < blocks.length; i++) blocks[i] = states[i].block.id;
        if (!Arrays.equals(blocks, blockIds)) blockIds = blocks;
        stale = false;
    }
}
