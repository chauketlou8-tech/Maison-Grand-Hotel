package utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public class SqlTimestampAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

    @Override
    public JsonElement serialize(Timestamp src, Type typeOfSrc, JsonSerializationContext context) {
        return src == null ? JsonNull.INSTANCE : new JsonPrimitive(src.toInstant().toString());
    }

    @Override
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) return null;

        // already a number (epoch millis)
        if (json.getAsJsonPrimitive().isNumber()) {
            return new Timestamp(json.getAsLong());
        }

        String value = json.getAsString();

        if (value.isEmpty()) return null;

        // ISO-8601 (new format)
        try {
            return Timestamp.from(java.time.Instant.parse(value));
        }

        catch (java.time.format.DateTimeParseException ignored) {}

        // legacy toString() format (old cached values)
        try {
            java.util.Date d = new java.text.SimpleDateFormat("MMM d, yyyy, h:mm:ss a", java.util.Locale.ENGLISH).parse(value);
            return new Timestamp(d.getTime());
        }

        catch (java.text.ParseException e) {
            throw new JsonParseException("Unparseable timestamp: " + value, e);
        }
    }
}