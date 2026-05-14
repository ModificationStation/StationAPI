package net.modificationstation.stationapi.api.util.context;

import com.mojang.serialization.MapCodec;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.function.BiPredicate;

public final class ConditionType<DATA> {
    private final Identifier id;
    private final MapCodec<DATA> dataCodec;
    private final BiPredicate<DATA, Context> logic;
    private final MapCodec<Condition<DATA>> conditionCodec;

    public ConditionType(Identifier id, MapCodec<DATA> dataCodec, BiPredicate<DATA, Context> logic) {
        this.id = id;
        this.dataCodec = dataCodec;
        this.logic = logic;
        conditionCodec = dataCodec.xmap(
                data -> new Condition<>(this, data),
                Condition::data
        );
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
