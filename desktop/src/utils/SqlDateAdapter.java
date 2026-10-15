package utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SqlDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private static final String[] FORMATS = {
            "MMM d, yyyy",
            "MMM d, yyyy, h:mm:ss a",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd"
    };

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return src == null ? JsonNull.INSTANCE : new JsonPrimitive(src.getTime());
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) return null;

        if (json.getAsJsonPrimitive().isNumber()) {
            return new Date(json.getAsLong());
        }

        String value = json.getAsString();

        if (value.isEmpty()) return null;

        value = value.replace("Sept ", "Sep ");

        for (String fmt : FORMATS) {
            try {
                return new SimpleDateFormat(fmt, Locale.ENGLISH).parse(value);
            }
            catch (ParseException ignored) {}
        }

        try {
            return Date.from(java.time.Instant.parse(value));
        }
        catch (Exception ignored) {}

        throw new JsonParseException("Unparseable date: " + value);
    }
}