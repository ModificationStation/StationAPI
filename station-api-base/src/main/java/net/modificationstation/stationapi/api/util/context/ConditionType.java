package net.modificationstation.stationapi.api.util.context;

import com.mojang.serialization.MapCodec;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ConditionType<DATA> {
    private final Identifier id;
    private final MapCodec<DATA> dataCodec;
    private final BiPredicate<DATA, Context> logic;
    private final MapCodec<Condition<DATA>> conditionCodec;

    private <C extends Context> ConditionType(Builder<DATA, C> builder) {
        this.id = builder.id;
        this.dataCodec = builder.codec;
        
        BiPredicate<DATA, C> logic = builder.logic;
        Function<Context, C> projection = builder.projection;
        this.logic = (data, ctx) -> logic.test(data, projection.apply(ctx));
        
        conditionCodec = dataCodec.xmap(
                data -> new Condition<>(this, data),
                Condition::data
        );
    }

    public static <DATA, C extends Context> Builder<DATA, C> builder(Identifier id, MapCodec<DATA> codec, Function<Context, C> projection, BiPredicate<DATA, C> logic, Consumer<ConditionType<DATA>> registryCallback) {
        return new Builder<>(id, codec, projection, logic, registryCallback);
    }

    public static class Builder<DATA, C extends Context> {
        private final Identifier id;
        private final MapCodec<DATA> codec;
        private final Function<Context, C> projection;
        private final BiPredicate<DATA, C> logic;
        private final Consumer<ConditionType<DATA>> registryCallback;

        private Builder(Identifier id, MapCodec<DATA> codec, Function<Context, C> projection, BiPredicate<DATA, C> logic, Consumer<ConditionType<DATA>> registryCallback) {
            this.id = id;
            this.codec = codec;
            this.projection = projection;
            this.logic = logic;
            this.registryCallback = registryCallback;
        }

        public void register() {
            registryCallback.accept(new ConditionType<>(this));
        }
    }

    public boolean test(DATA data, Context ctx) {
        return logic.test(data, ctx);
    }

    public MapCodec<Condition<DATA>> conditionCodec() {
        return conditionCodec;
    }

    public Identifier id() {
        return id;
    }

    public MapCodec<DATA> dataCodec() {
        return dataCodec;
    }

    public BiPredicate<DATA, Context> logic() {
        return logic;
    }
}
