package net.modificationstation.stationapi.api.resource;

import com.google.common.base.Supplier;
import cyclops.control.Option;
import cyclops.data.tuple.Tuple2;
import cyclops.function.Function2;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import lombok.val;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.function.Function;

import static com.google.common.base.Suppliers.memoize;
import static cyclops.control.Option.none;
import static cyclops.control.Option.some;
import static cyclops.data.tuple.Tuple.tuple;
import static cyclops.function.Function1.constant;
import static cyclops.function.Function2._2;

import F;

public final class CompositeResourceReload implements ResourceReload {
    private static final float DEFAULT_WEIGHT = 1;
    private static final Function2<Function<Float, Float>, Tuple2<Option<Supplier<ResourceReload>>, Float>, Tuple2<Option<Supplier<ResourceReload>>, Float>> WEIGHT_MAPPER_FACTORY = Function2.<Tuple2<Option<Supplier<ResourceReload>>, Float>, Function<Float, Float>, Tuple2<Option<Supplier<ResourceReload>>, Float>>λ(Tuple2::map2).reverse();
    private static final Function2<Function<Option<Supplier<ResourceReload>>, Option<Supplier<ResourceReload>>>, Tuple2<Option<Supplier<ResourceReload>>, Float>, Tuple2<Option<Supplier<ResourceReload>>, Float>> RESOURCE_RELOAD_MAPPER_FACTORY = Function2.<Tuple2<Option<Supplier<ResourceReload>>, Float>, Function<Option<Supplier<ResourceReload>>, Option<Supplier<ResourceReload>>>, Tuple2<Option<Supplier<ResourceReload>>, Float>>λ(Tuple2::map1).reverse();

    private final Reference2ObjectMap<Identifier, Tuple2<Option<Supplier<ResourceReload>>, Float>> reloads = Reference2ObjectMaps.synchronize(new Reference2ObjectOpenHashMap<>());

    public void setWeight(Identifier id, float weight) {
        reloads.merge(
                id, tuple(none(), weight),
                _2(WEIGHT_MAPPER_FACTORY.apply(constant(weight)))
        );
    }

    public void scheduleReload(Identifier id, Supplier<ResourceReload> reload) {
        reloads.merge(
                id, tuple(some(memoize(reload)), DEFAULT_WEIGHT),
                _2(RESOURCE_RELOAD_MAPPER_FACTORY.apply(constant(some(memoize(reload)))))
        );
    }

    public void scheduleReload(Identifier id, Supplier<ResourceReload> reload, float weight) {
        reloads.put(id, tuple(some(memoize(reload)), weight));
    }

    @Override
    public float getProgress() {
        var totalWeight = 0F;
        var progress = 0F;
        for (val weightedReload : reloads.values()) {
            totalWeight += weightedReload._2();
            if (!weightedReload._1().isPresent()) continue;
            progress += weightedReload._1().orElse(null/*safe*/).get().getProgress() * weightedReload._2();
        }
        return progress / totalWeight;
    }

    @Override
    public boolean isComplete() {
        if (reloads.values().isEmpty())
            return false;
        for (val weightedReload : reloads.values()) {
            if (weightedReload._1().isPresent() && !weightedReload._1().orElse(null/*safe*/).get().isComplete())
                return false;
        }
        return true;
    }

    @Override
    public void throwException() {
        for (val weightedReload : reloads.values())
            if (weightedReload._1().isPresent())
                weightedReload._1().orElse(null/*safe*/).get().throwException();
    }
}
