package org.mobile.utils;

import org.api.models.forecast.fiveDaily.DailyForecast;
import org.api.models.forecast.fiveDaily.DailyForecastResponse;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class DateUtil {

    public static DailyForecast findHottestDate(DailyForecastResponse dailyForecastResponse) {
        // Today included in the epoch
        long todayEpochStart = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toEpochSecond();

        return dailyForecastResponse.getDailyForecasts().stream()
                .filter(Objects::nonNull)
                .filter(dailyForecast -> dailyForecast.getEpochDate() >= todayEpochStart)
                .max(Comparator.comparingDouble(dailyForecast -> dailyForecast.getTemperature().getMaximum().getValue()))
                .orElseGet(() -> {
                    Assertions.fail("No hottest date found in the forecast response (Today included). Daily Forecasts to analyze: " + dailyForecastResponse);
                    return null;
                });
    }

    public static String convertDateToUIFormat(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH);
        return date.format(formatter);
    }

    public static LocalDate convertDateToLocalDate(String date) {
        OffsetDateTime dateTime = OffsetDateTime.parse(date);
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
