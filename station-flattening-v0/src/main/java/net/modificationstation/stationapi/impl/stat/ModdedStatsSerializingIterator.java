package net.modificationstation.stationapi.impl.stat;

import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.util.Namespace;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

/**
 * Iterates over a set of stats, filtering out modded ones and serializing them separately.
 */
public final class ModdedStatsSerializingIterator implements Iterator<Stat> {
    public static final String STATIONAPI_STATS_CHANGE_KEY = NAMESPACE.id("stats-change").toString();

    private final Iterator<Stat> source;
    private final StringBuilder moddedStatsSerialized;
    private final Map<Stat, Integer> statCounts;

    private Stat nextVanillaStat;
    private boolean firstModdedStat = true;

    public ModdedStatsSerializingIterator(
            Iterator<Stat> source, StringBuilder moddedStatsSerialized, Map<Stat, Integer> statCounts
    ) {
        this.source = source;
        this.moddedStatsSerialized = moddedStatsSerialized;
        this.statCounts = statCounts;

        moddedStatsSerialized.append("  \"").append(STATIONAPI_STATS_CHANGE_KEY).append("\":[");

        advance();
    }

    /**
     * Searches for the next vanilla stat in the set and serializes all modded stats found on the way.
     */
    private void advance() {
        nextVanillaStat = null;

        // Searching for the next vanilla stat in the set
        // and serializing all modded stats found on the way
        while (source.hasNext()) {
            Stat candidate = source.next();

            if (candidate.getRegistryEntry().registryKey().getValue().namespace == Namespace.MINECRAFT) {
                nextVanillaStat = candidate;
                return;
            }

            serializeModded(candidate);
        }
    }

    /**
     * Serializes the given stat under the modded stats array.
     *
     * @param stat the stat to serialize under the modded stats array
     */
    private void serializeModded(Stat stat) {
        if (!firstModdedStat) moddedStatsSerialized.append("},");
        else firstModdedStat = false;

        moddedStatsSerialized.append("\r\n    {\"");
        moddedStatsSerialized.append(stat.getRegistryEntry().registryKey().getValue()).append("\":").append(statCounts.get(stat));
    }

    @Override
    public boolean hasNext() {
        if (nextVanillaStat != null)
            return true;

        // If there are no more stats from the source iterator,
        // finish serializing modded stats and terminate the loop
        if (!firstModdedStat)
            moddedStatsSerialized.append("}");
        moddedStatsSerialized.append("\r\n  ],\r\n");
        return false;
    }

    /**
     * Returns the next vanilla {@link Stat} and advances the source iterator via {@link #advance()}.
     *
     * @return the next vanilla {@link Stat} from the source iterator
     * @throws NoSuchElementException if there are no more vanilla stats
     */
    @Override
    public Stat next() {
        if (nextVanillaStat == null) throw new NoSuchElementException();

        final Stat stat = nextVanillaStat;
        advance();
        return stat;
    }
}
