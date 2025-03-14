package org.api.steps;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.api.models.cities.search.City;
import org.mobile.utils.ConfigReader;
import org.mobile.utils.HttpClientUtil;

import java.util.List;

import static org.mobile.base.TestManagement.objectMapper;

public class RequestSteps {

    public static final String API_KEY = System.getProperty("api_key", ConfigReader.get("api_key"));

    public static String getLocationKeyFor(String city) throws JsonProcessingException {
        String path = ConfigReader.get("api_base_url") + "/locations/v1/cities/search?apikey=%s&q=%s".formatted(API_KEY, city);
        HttpClientUtil.sendGetRequest(path);
        String cityBody = HttpClientUtil.getHttpClientResponseBody();
        List<City> cities = objectMapper.readValue(cityBody, new TypeReference<>() {
        });
        return cities.get(0).getKey();
    }

    public static String getResponseForFiveDaysForecast(String locationKey) throws JsonProcessingException {
        String path = ConfigReader.get("api_base_url") + "/forecasts/v1/daily/5day/%s?apikey=%s".formatted(locationKey, API_KEY);
        HttpClientUtil.sendGetRequest(path);
        return HttpClientUtil.getHttpClientResponseBody();
    }

}