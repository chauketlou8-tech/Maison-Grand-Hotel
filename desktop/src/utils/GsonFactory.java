package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class GsonFactory {

    public static Gson build() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new SqlDateAdapter())
                .registerTypeAdapter(Time.class, new SqlTimeAdapter())
                .registerTypeAdapter(Timestamp.class, new SqlTimestampAdapter())
                .registerTypeAdapter(RoomStatus.class, new LowerCaseEnumAdapter<>())
                .registerTypeAdapter(BookingStatus.class, new LowerCaseEnumAdapter<>())
                .registerTypeAdapter(ReservationStatus.class, new LowerCaseEnumAdapter<>())
                .registerTypeAdapter(NotificationType.class, new LowerCaseEnumAdapter<>())
                .registerTypeAdapter(NotificationStatus.class, new LowerCaseEnumAdapter<>())
                .registerTypeAdapter(Gender.class, new LowerCaseEnumAdapter<>())
                .registerTypeAdapter(EmploymentStatus.class, new LowerCaseEnumAdapter<>())
                .registerTypeAdapter(Role.class, new LowerCaseEnumAdapter<>())
                .registerTypeAdapter(AccountStatus.class, new LowerCaseEnumAdapter<>())
                .create();
    }
}