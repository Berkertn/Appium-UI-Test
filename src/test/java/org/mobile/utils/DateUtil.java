package org.mobile.utils;

import org.api.models.forecast.fiveDaily.DailyForecast;
import org.api.models.forecast.fiveDaily.DailyForecastResponse;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class DateUtil {

    public static DailyForecast findHottestDate(DailyForecastResponse dailyForecastResponse) {
        DailyForecast hottestForecast = dailyForecastResponse.getDailyForecasts().stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparingDouble(d -> d.getTemperature().getMaximum().getValue()))
                .orElse(null);
        if (hottestForecast == null) {
            Assertions.fail("No hottest date found in the forecast response, forecast as day list: " + dailyForecastResponse);

        }
        return hottestForecast;
    }

    public static String convertDateToUIFormat(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH);
        return date.format(formatter);
    }

    public static LocalDate convertDateToLocalDate(String date) {
        OffsetDateTime dateTime = OffsetDateTime.parse("2025-03-16T10:15:30+00:00");
       return dateTime.toLocalDate();
    }

    public static String convertDateToGivenFormat(LocalDate date, DateTimeFormatter format) {
        return date.format(format);
    }

    public static int getDayAsNumber(LocalDate date) {
        return date.getDayOfMonth();
    }

    public static String getMonthAsName(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
        return date.format(formatter);
    }
}
