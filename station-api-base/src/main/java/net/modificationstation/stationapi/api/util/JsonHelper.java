package net.modificationstation.stationapi.api.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class JsonHelper {
    private static final Gson GSON = (new GsonBuilder()).create();

    public static boolean hasString(JsonObject object, String element) {
        return hasPrimitive(object, element) && object.getAsJsonPrimitive(element).isString();
    }

    @Environment(EnvType.CLIENT)
    public static boolean isString(JsonElement element) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
    }

    public static boolean isNumber(JsonElement element) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber();
    }

    @Environment(EnvType.CLIENT)
    public static boolean hasBoolean(JsonObject object, String element) {
        return hasPrimitive(object, element) && object.getAsJsonPrimitive(element).isBoolean();
    }

    public static boolean hasArray(JsonObject object, String element) {
        return hasElement(object, element) && object.get(element).isJsonArray();
    }

    public static boolean hasPrimitive(JsonObject object, String element) {
        return hasElement(object, element) && object.get(element).isJsonPrimitive();
    }

    public static boolean hasElement(JsonObject object, String lement) {
        if (object == null) {
            return false;
        } else {
            return object.get(lement) != null;
        }
    }

    public static String asString(JsonElement element, String name) {
        if (element.isJsonPrimitive()) {
            return element.getAsString();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a string, was " + getType(element));
        }
    }

    public static String getString(JsonObject object, String element) {
        if (object.has(element)) {
            return asString(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a string");
        }
    }

    public static String getString(JsonObject object, String element, String defaultStr) {
        return object.has(element) ? asString(object.get(element), element) : defaultStr;
    }

//   public static Item asItem(JsonElement element, String name) {
//      if (element.isJsonPrimitive()) {
//         String string = element.getAsString();
//         return (Item)Registry.ITEM.getOrEmpty(new Identifier(string)).orElseThrow(() -> {
//            return new JsonSyntaxException("Expected " + name + " to be an item, was unknown string '" + string + "'");
//         });
//      } else {
//         throw new JsonSyntaxException("Expected " + name + " to be an item, was " + getType(element));
//      }
//   }
//
//   public static Item getItem(JsonObject object, String key) {
//      if (object.has(key)) {
//         return asItem(object.get(key), key);
//      } else {
//         throw new JsonSyntaxException("Missing " + key + ", expected to find an item");
//      }
//   }

    public static boolean asBoolean(JsonElement element, String name) {
        if (element.isJsonPrimitive()) {
            return element.getAsBoolean();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a Boolean, was " + getType(element));
        }
    }

    public static boolean getBoolean(JsonObject object, String element) {
        if (object.has(element)) {
            return asBoolean(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a Boolean");
        }
    }

    public static boolean getBoolean(JsonObject object, String element, boolean defaultBoolean) {
        return object.has(element) ? asBoolean(object.get(element), element) : defaultBoolean;
    }

    public static float asFloat(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsFloat();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a Float, was " + getType(element));
        }
    }

    public static float getFloat(JsonObject object, String element) {
        if (object.has(element)) {
            return asFloat(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a Float");
        }
    }

    public static float getFloat(JsonObject object, String element, float defaultFloat) {
        return object.has(element) ? asFloat(object.get(element), element) : defaultFloat;
    }

    public static long asLong(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsLong();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a Long, was " + getType(element));
        }
    }

    public static long getLong(JsonObject object, String name) {
        if (object.has(name)) {
            return asLong(object.get(name), name);
        } else {
            throw new JsonSyntaxException("Missing " + name + ", expected to find a Long");
        }
    }

    public static long getLong(JsonObject object, String element, long defaultLong) {
        return object.has(element) ? asLong(object.get(element), element) : defaultLong;
    }

    public static int asInt(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsInt();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a Int, was " + getType(element));
        }
    }

    public static int getInt(JsonObject object, String element) {
        if (object.has(element)) {
            return asInt(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a Int");
        }
    }

    public static int getInt(JsonObject object, String element, int defaultInt) {
        return object.has(element) ? asInt(object.get(element), element) : defaultInt;
    }

    public static byte asByte(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsByte();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a Byte, was " + getType(element));
        }
    }

    public static byte getByte(JsonObject object, String element, byte defaultByte) {
        return object.has(element) ? asByte(object.get(element), element) : defaultByte;
    }

    public static JsonObject asObject(JsonElement element, String name) {
        if (element.isJsonObject()) {
            return element.getAsJsonObject();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a JsonObject, was " + getType(element));
        }
    }

    public static JsonObject getObject(JsonObject object, String element) {
        if (object.has(element)) {
            return asObject(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a JsonObject");
        }
    }

    public static JsonObject getObject(JsonObject object, String element, JsonObject defaultObject) {
        return object.has(element) ? asObject(object.get(element), element) : defaultObject;
    }

    public static JsonArray asArray(JsonElement element, String name) {
        if (element.isJsonArray()) {
            return element.getAsJsonArray();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a JsonArray, was " + getType(element));
        }
    }

    public static JsonArray getArray(JsonObject object, String element) {
        if (object.has(element)) {
            return asArray(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a JsonArray");
        }
    }

    @Nullable
    public static JsonArray getArray(JsonObject object, String name, @Nullable JsonArray defaultArray) {
        return object.has(name) ? asArray(object.get(name), name) : defaultArray;
    }

    public static <T> T deserialize(@Nullable JsonElement element, String name, JsonDeserializationContext context, Class<? extends T> type) {
        if (element != null) {
            return context.deserialize(element, type);
        } else {
            throw new JsonSyntaxException("Missing " + name);
        }
    }

    public static <T> T deserialize(JsonObject object, String element, JsonDeserializationContext context, Class<? extends T> type) {
        if (object.has(element)) {
            return deserialize(object.get(element), element, context, type);
        } else {
            throw new JsonSyntaxException("Missing " + element);
        }
    }

    public static <T> T deserialize(JsonObject object, String element, T defaultValue, JsonDeserializationContext context, Class<? extends T> type) {
        return object.has(element) ? deserialize(object.get(element), element, context, type) : defaultValue;
    }

    public static String getType(JsonElement element) {
        String string = StringUtils.abbreviateMiddle(String.valueOf(element), "...", 10);
        if (element == null) {
            return "null (missing)";
        } else if (element.isJsonNull()) {
            return "null (json)";
        } else if (element.isJsonArray()) {
            return "an array (" + string + ")";
        } else if (element.isJsonObject()) {
            return "an object (" + string + ")";
        } else {
            if (element.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = element.getAsJsonPrimitive();
                if (jsonPrimitive.isNumber()) {
                    return "a number (" + string + ")";
                }

                if (jsonPrimitive.isBoolean()) {
                    return "a boolean (" + string + ")";
                }
            }

            return string;
        }
    }

    @Nullable
    public static <T> T deserialize(Gson gson, Reader reader, Class<T> type, boolean lenient) {
        try {
            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.setLenient(lenient);
            return gson.getAdapter(type).read(jsonReader);
        } catch (IOException var5) {
            throw new JsonParseException(var5);
        }
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public static <T> T deserialize(Gson gson, Reader reader, TypeToken<T> typeToken, boolean lenient) {
        try {
            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.setLenient(lenient);
            return gson.getAdapter(typeToken).read(jsonReader);
        } catch (IOException var5) {
            throw new JsonParseException(var5);
        }
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public static <T> T deserialize(Gson gson, String content, TypeToken<T> typeToken, boolean lenient) {
        return deserialize(gson, new StringReader(content), typeToken, lenient);
    }

    @Nullable
    public static <T> T deserialize(Gson gson, String content, Class<T> class_, boolean lenient) {
        return deserialize(gson, new StringReader(content), class_, lenient);
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public static <T> T deserialize(Gson gson, Reader reader, TypeToken<T> typeToken) {
        return deserialize(gson, reader, typeToken, false);
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public static <T> T deserialize(Gson gson, String content, TypeToken<T> typeToken) {
        return deserialize(gson, content, typeToken, false);
    }

    @Nullable
    public static <T> T deserialize(Gson gson, Reader reader, Class<T> class_) {
        return deserialize(gson, reader, class_, false);
    }

    @Nullable
    public static <T> T deserialize(Gson gson, String content, Class<T> class_) {
        return deserialize(gson, content, class_, false);
    }

    public static JsonObject deserialize(String content, boolean lenient) {
        return deserialize((Reader)(new StringReader(content)), lenient);
    }

    public static JsonObject deserialize(Reader reader, boolean lenient) {
        return deserialize(GSON, reader, JsonObject.class, lenient);
    }

    public static JsonObject deserialize(String content) {
        return deserialize(content, false);
    }

    public static JsonObject deserialize(Reader reader) {
        return deserialize(reader, false);
    }
}
