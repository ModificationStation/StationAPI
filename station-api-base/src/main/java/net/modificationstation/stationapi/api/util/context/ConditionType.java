package net.modificationstation.stationapi.api.util.context;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.MapCodec;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

public final class ConditionType<DATA> {
    private final Identifier id;
    private final MapCodec<DATA> dataCodec;
    private final BiPredicate<DATA, Context> logic;
    private final Pattern shorthandPattern;
    private final Function<Dynamic<?>, Dynamic<?>> unfolder;
    private final MapCodec<Condition<DATA>> conditionCodec;

    private <C extends Context> ConditionType(Builder<DATA, C> builder) {
        this.id = builder.id;
        this.dataCodec = builder.codec;
        this.shorthandPattern = builder.shorthandPattern;
        this.unfolder = builder.unfolder;
        
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
        private Pattern shorthandPattern;
        private Function<Dynamic<?>, Dynamic<?>> unfolder;

        private Builder(Identifier id, MapCodec<DATA> codec, Function<Context, C> projection, BiPredicate<DATA, C> logic, Consumer<ConditionType<DATA>> registryCallback) {
            this.id = id;
            this.codec = codec;
            this.projection = projection;
            this.logic = logic;
            this.registryCallback = registryCallback;
        }

        public Builder<DATA, C> shorthand(Pattern pattern, Function<Dynamic<?>, Dynamic<?>> unfolder) {
            this.shorthandPattern = pattern;
            this.unfolder = unfolder;
            return this;
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

    public Pattern shorthandPattern() {
        return shorthandPattern;
    }

    public Function<Dynamic<?>, Dynamic<?>> unfolder() {
        return unfolder;
    }
}
