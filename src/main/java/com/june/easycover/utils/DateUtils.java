package com.june.easycover.utils;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import jersey.repackaged.com.google.common.collect.ImmutableList;


public class DateUtils {
    public static final long DAYS_IN_WEEK_NUM = 7;

    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";

    public static final String ISO_WEEK_FORMAT = "YYYY-'W'ww";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final String DATE_TIME_FORMAT_WITH_MS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String DATE_TIME_FORMAT_WITH_MS2 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'";

    public static final String REQUEST_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String REQUEST_FORMAT_WITH_T = "yyyy-MM-dd'T'HH:mm:ss";

    public static final DateTimeFormatter REPORT_DATE_TIME_FORMAT = ofPattern("yyyy-MM-dd'T'HH-mm-ss");

    public static final DateTimeFormatter T_TIME_FORMAT = ofPattern("'T'HH:mm:ss");

    public static final DateTimeFormatter DAY_OF_MONTH_AND_TIME = ofPattern("dd'T'HH:mm:ss");

    public static final List<DateTimeFormatter> PARAM_DATE_FORMATTERS = ImmutableList.of(
            ofPattern(REQUEST_FORMAT),
            ofPattern(DATE_TIME_FORMAT),
            ofPattern(DATE_TIME_FORMAT_WITH_MS),
            ofPattern(DATE_TIME_FORMAT_WITH_MS2));

    public static final ZoneId UTC = ZoneId.of("UTC");

    private static final List<String> DATE_FORMATS = Arrays.asList(
            "EEE MMM dd HH:mm:ss yyyy Z", "yyyyMMdd", "EEE MMM d HH:mm:ss yyyy Z", "yyyyMMdd",
            "dd-MM-yyyy", "yyyy-MM-dd", "MM/dd/yyyy", "yyyy/MM/dd", "dd MMM yyyy", "dd MMMM yyyy",
            "yyyyMMddHHmm", "yyyyMMdd HHmm", "dd-MM-yyyy HH:mm", "yyyy-MM-dd HH:mm",
            "MM/dd/yyyy HH:mm",
            "yyyy/MM/dd HH:mm", "dd MMM yyyy HH:mm", "dd MMMM yyyy HH:mm", "yyyyMMddHHmmss",
            "yyyyMMdd HHmmss",
            "dd-MM-yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "MM/dd/yyyy HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss",
            "dd MMM yyyy HH:mm:ss", "dd MMMM yyyy HH:mm:ss");

    /**
     * Determine Date matching with the given date string. Returns null if format is unknown. You can
     * simply extend DateUtil with more formats if needed.
     *
     * @param dateString The date string to determine the Date for.
     * @return The matching Date value, or null if format is unknown.
     */
    public static LocalDateTime determineDate(String dateString) {
        LocalDateTime date = null;
        for (String dateFormatString : DATE_FORMATS) {
            DateTimeFormatter dateTimeFormatter = ofPattern(dateFormatString);
            try {
                // log indicates datetime in Zoned Format, we convert to UTC then force to LocalDateTime
                ZonedDateTime zonedDate = ZonedDateTime.parse(dateString, dateTimeFormatter);
                ZonedDateTime utcZoned = zonedDate.withZoneSameInstant(UTC);
                //date = LocalDateTime.parse(dateString, dateTimeFormatter);
                date = utcZoned.toLocalDateTime();
                break;
            } catch (DateTimeParseException exc) {                
            }
        }
        return date; // Unknown format.
    }

    /**
     * Checks if the supplied date matches one of the accepted date formats defined in PARAM_DATE_FORMATS.
     *
     * @param dateString the date to be processed
     * @return LocalDateTime if parameter matches an accepted format, Optional.empty() if otherwise
     */
    public static Optional<LocalDateTime> determineDateNoZone(String dateString) {
        LocalDateTime date = null;
        for (DateTimeFormatter dateTimeFormatter : PARAM_DATE_FORMATTERS) {
            try {
                date = LocalDateTime.parse(dateString, dateTimeFormatter);
                return Optional.of(date);
            } catch (DateTimeParseException exc) {
                date = null;
            }
        }
        return Optional.ofNullable(date); // Unknown format.
    }

    public static long determineWeeksNum(LocalDateTime startDatetime, LocalDateTime endDatetime) {
        LocalDateTime
                startMonday =
                startDatetime.getDayOfWeek().equals(DayOfWeek.MONDAY) ? startDatetime
                        : startDatetime.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        long daysNum = ChronoUnit.DAYS.between(startMonday, endDatetime);
        // same day diff is 0 but it is one day for business logic
        if (daysNum == 0L) {
            daysNum = 1L;
        }

        return daysNum / DAYS_IN_WEEK_NUM + (daysNum % DAYS_IN_WEEK_NUM == 0 ? 0 : 1);
    }

    public static String getDateTimeString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = ofPattern(DATE_TIME_FORMAT);
        return formatter.format(dateTime);
    }

    public static LocalDateTime timeToLocalDateTime(long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(millis), UTC);
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), UTC);
    }

    /**
     * Since DateTimeFormatter does not supports ISO-8601 week starting on Monday it's required to manually format
     * the date. DateTimeFormatter 'u' returns 1-7 week starting on Sunday, where Sunday is 1, this method returns
     * 1-7 week starting on Monday, where Monday is 1.
     * <p>Output format for 12 March 2016 Sunday 12:13:14 is '7T12:13:14'
     *
     * @param dateTime the LocalDateTime variable to format
     * @return String formatted LocalDateTime
     */
    public static String dayOfWeekISO8601AndTime(LocalDateTime dateTime) {
        int dow = dateTime.getDayOfWeek().getValue();
        return dow + DateUtils.T_TIME_FORMAT.format(dateTime);
    }

    public static String dayOfMonthISO8601AndTime(LocalDateTime dateTime) {
        return DateUtils.DAY_OF_MONTH_AND_TIME.format(dateTime);
    }
}
