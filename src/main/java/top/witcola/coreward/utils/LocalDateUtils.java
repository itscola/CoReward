package top.witcola.coreward.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LocalDateUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }


    public static LocalDateTime parseToLocalDateTime(String dateString) {
        return LocalDateTime.parse(dateString, formatter);
    }

    public static long minutesBetween(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return ChronoUnit.MINUTES.between(dateTime1, dateTime2);
    }

    public static long secondsBetween(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return ChronoUnit.SECONDS.between(dateTime1, dateTime2);
    }

    public static boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
//        System.out.println(date1.getDayOfMonth()+" "+date2.getDayOfMonth());
        return date1.getDayOfMonth() == date2.getDayOfMonth();
    }
}
