package records;

import java.time.LocalDateTime;

public record Event(String color, String text, LocalDateTime when) {}