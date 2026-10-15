package utils;

import com.google.gson.*;

import java.lang.reflect.Type;

public class LowerCaseEnumAdapter<T extends Enum<T>> implements JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return src == null ? JsonNull.INSTANCE : new JsonPrimitive(src.name().toLowerCase());
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) return null;

        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) typeOfT;

        String value = json.getAsString();

        for (T constant : clazz.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(value)) return constant;
        }

        throw new JsonParseException("Unknown enum value for " + clazz.getSimpleName() + ": " + value);
    }
}