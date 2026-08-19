package net.modificationstation.stationapi.api.util.context;

public record Condition<DATA>(ConditionType<DATA> type, DATA data) {
    public boolean test(Context ctx) {
        return type.test(data, ctx);
    }
}
