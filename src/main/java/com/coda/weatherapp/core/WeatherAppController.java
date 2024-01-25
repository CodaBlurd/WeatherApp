package com.coda.weatherapp.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAppController {

    private static final Logger log = LoggerFactory.getLogger(WeatherAppController.class);

    private static final String API_KEY = WeatherAppConfig.API_KEY;
    private static final String API_URL = WeatherAppConfig.API_URL;

    private HttpURLConnection connection;

    // Constructor for regular use
    public WeatherAppController() {
    }

    // Constructor for testing
    public WeatherAppController(HttpURLConnection connection) {
        this.connection = connection;
    }
    public WeatherInfo getWeather(String location) throws Exception {
        String urlString = API_URL + "?q=" + location + "&APPID=" + API_KEY;
        log.info(System.getenv("API_KEY"));
        log.info(System.getenv("API_URL"));

        // Use the provided connection for testing; otherwise, create a new one
        if (this.connection == null) {
            URL url = new URL(urlString);
            this.connection = (HttpURLConnection) url.openConnection();
        }

        this.connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            log.info("ApiResponse: {}", sb);

            ObjectMapper objectMapper = new ObjectMapper();
            WeatherInfo weatherInfo = objectMapper.readValue(sb.toString(), WeatherInfo.class);

            log.info("Weather info: {}", weatherInfo.toString());

            return weatherInfo;

        } catch (Exception e) {
            log.error("Error while fetching weather data", e);
            return null;

        } finally {
            this.connection.disconnect();
        }
    }
}
