package utils;

import records.Settings;

import java.time.format.DateTimeFormatter;

public final class DateFormats {

    private static final DateTimeFormatter DEFAULT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DateFormats() {}

    public static DateTimeFormatter of(Settings settings) {
        String pattern = settings == null ? null : settings.dateFormat();

        if (pattern == null || pattern.isBlank()) {
            return DEFAULT;
        }

        String javaPattern = pattern.replace("DD", "dd").replace("YYYY", "yyyy");

        try {
            return DateTimeFormatter.ofPattern(javaPattern);
        }

        catch (IllegalArgumentException e) {
            return DEFAULT;
        }
    }
}