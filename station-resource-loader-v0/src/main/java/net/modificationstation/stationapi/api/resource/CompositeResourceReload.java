package net.modificationstation.stationapi.api.resource;

import com.google.common.base.Supplier;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import lombok.val;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Optional;

import static com.google.common.base.Suppliers.memoize;

public final class CompositeResourceReload implements ResourceReload {
    private static final float DEFAULT_WEIGHT = 1;

    private final Reference2ObjectMap<Identifier, Pair<Optional<Supplier<ResourceReload>>, Float>> reloads = Reference2ObjectMaps.synchronize(new Reference2ObjectOpenHashMap<>());

    public void setWeight(Identifier id, float weight) {
        reloads.merge(
                id, Pair.of(Optional.empty(), weight),
                (oldReload, newReload) -> Pair.of(oldReload.getFirst(), newReload.getSecond())
        );
    }

    public void scheduleReload(Identifier id, Supplier<ResourceReload> reload) {
        reloads.merge(
                id, Pair.of(Optional.of(memoize(reload)), DEFAULT_WEIGHT),
                (oldReload, newReload) -> Pair.of(newReload.getFirst(), oldReload.getSecond())
        );
    }

    public void scheduleReload(Identifier id, Supplier<ResourceReload> reload, float weight) {
        reloads.put(id, Pair.of(Optional.of(memoize(reload)), weight));
    }

    @Override
    public float getProgress() {
        var totalWeight = 0F;
        var progress = 0F;
        for (val weightedReload : reloads.values()) {
            totalWeight += weightedReload.getSecond();
            if (weightedReload.getFirst().isEmpty()) continue;
            progress += weightedReload.getFirst().get().get().getProgress() * weightedReload.getSecond();
        }
        return progress / totalWeight;
    }

    @Override
    public boolean isComplete() {
        if (reloads.values().isEmpty())
            return false;
        for (val weightedReload : reloads.values()) {
            if (weightedReload.getFirst().map(reload -> !reload.get().isComplete()).orElse(false))
                return false;
        }
        return true;
    }

    @Override
    public void throwException() {
        for (val weightedReload : reloads.values())
            weightedReload.getFirst().ifPresent(reload -> reload.get().throwException());
    }
}
