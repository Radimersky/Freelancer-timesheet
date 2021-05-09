package data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimestampConverter {

    public static Timestamp convertToTimeStamp(LocalDateTime locDateTime) {
        return Timestamp.valueOf(locDateTime);
    }

    public static LocalDateTime convertToLocalDateTime(Timestamp sqlTimestamp) {
        return sqlTimestamp.toLocalDateTime();
    }
}
