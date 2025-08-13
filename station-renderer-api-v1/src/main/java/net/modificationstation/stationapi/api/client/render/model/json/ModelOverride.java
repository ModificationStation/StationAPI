package net.modificationstation.stationapi.api.client.render.model.json;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.JsonHelper;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings("ClassCanBeRecord")
@Environment(EnvType.CLIENT)
public class ModelOverride {
    private final Identifier modelId;
    private final List<Condition> conditions;

    public ModelOverride(Identifier modelId, List<Condition> conditions) {
        this.modelId = modelId;
        this.conditions = ImmutableList.copyOf(conditions);
    }

    public Identifier getModelId() {
        return this.modelId;
    }

    public Stream<Condition> streamConditions() {
        return this.conditions.stream();
    }

    @SuppressWarnings("ClassCanBeRecord")
    @Environment(EnvType.CLIENT)
    public static class Condition {
        private final Identifier type;
        private final float threshold;

        public Condition(Identifier type, float threshold) {
            this.type = type;
            this.threshold = threshold;
        }

        public Identifier getType() {
            return this.type;
        }

        public float getThreshold() {
            return this.threshold;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Deserializer implements JsonDeserializer<ModelOverride> {
        protected Deserializer() {
        }

        public ModelOverride deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Identifier identifier = Identifier.of(JsonHelper.getString(jsonObject, "model"));
            List<Condition> list = this.deserializeMinPropertyValues(jsonObject);
            return new ModelOverride(identifier, list);
        }

        protected List<Condition> deserializeMinPropertyValues(JsonObject object) {
            LinkedHashMap<Identifier, Float> map = Maps.newLinkedHashMap();
            JsonObject jsonObject = JsonHelper.getObject(object, "predicate");
            for (Map.Entry<String, JsonElement> entry2 : jsonObject.entrySet())
                map.put(Identifier.of(entry2.getKey()), JsonHelper.asFloat(entry2.getValue(), entry2.getKey()));
            return map.entrySet().stream().map(entry -> new Condition(entry.getKey(), entry.getValue())).collect(ImmutableList.toImmutableList());
        }
    }
}
