package org.api.steps;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.api.models.cities.search.City;
import org.api.models.conditions.CurrentCondition;
import org.mobile.utils.ConfigReader;
import org.mobile.utils.HttpClientUtil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mobile.base.TestHooks.objectMapper;

public class RequestSteps {

    public static final String API_KEY = System.getProperty("api_key", ConfigReader.get("api_key"));

    public static String getLocationKeyFor(String city) {
        String encodedCity = city.contains(" ") ? URLEncoder.encode(city, StandardCharsets.UTF_8) : city;
        String path = ConfigReader.get("api_base_url") + "/locations/v1/cities/search?apikey=%s&q=%s".formatted(API_KEY, encodedCity);
        HttpClientUtil.sendGetRequest(path);
        String cityBody = HttpClientUtil.getHttpClientResponseBody();
        List<City> cities = null;
        try {
            cities = objectMapper.readValue(cityBody, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return cities.get(0).getKey();
    }

    public static String getResponseForFiveDaysForecast(String locationKey) {
        String path = ConfigReader.get("api_base_url") + "/forecasts/v1/daily/5day/%s?apikey=%s".formatted(locationKey, API_KEY);
        HttpClientUtil.sendGetRequest(path);
        return HttpClientUtil.getHttpClientResponseBody();
    }

    public static CurrentCondition getFirstResponseOfCurrentConditionAsClassInstance(String locationKey) {
        String body = getFirstResponseOfCurrentCondition(locationKey);
        List<CurrentCondition> conditions = null;
        try {
            conditions = objectMapper.readValue(body, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return conditions.get(0);
    }

    public static String getFirstResponseOfCurrentCondition(String locationKey) {
        String path = ConfigReader.get("api_base_url") + "/currentconditions/v1/%s?apikey=%s".formatted(locationKey, API_KEY);
        HttpClientUtil.sendGetRequest(path);
        return HttpClientUtil.getHttpClientResponseBody();
    }
}