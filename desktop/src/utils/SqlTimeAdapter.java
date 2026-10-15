package utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Time;

public class SqlTimeAdapter implements JsonSerializer<Time>, JsonDeserializer<Time> {

    @Override
    public JsonElement serialize(Time src, Type typeOfSrc, JsonSerializationContext context) {
        return src == null ? JsonNull.INSTANCE : new JsonPrimitive(src.toString());
    }

    @Override
    public Time deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) return null;
        String value = json.getAsString();
        if (value.isEmpty()) return null;
        return Time.valueOf(value);
    }
}