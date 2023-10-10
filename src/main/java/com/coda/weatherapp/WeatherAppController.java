package com.coda.weatherapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.fasterxml.jackson.databind.ObjectMapper;


public class WeatherAppController {

    private static final Logger log = LoggerFactory.getLogger(WeatherAppController.class);


    private static final String API_KEY = WeatherAppConfig.API_KEY;

    private static final String API_URL = WeatherAppConfig.API_URL;

    public WeatherInfo getWeather(String location) throws Exception {
        //create url for the API request

        String urlString = API_URL + "?q=" + location + "&APPID=" + API_KEY;

        //create HTTP connection to the url

        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        //Read the API response

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

            StringBuilder sb = new StringBuilder();


            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            log.info("ApiResponse: {}" + sb);

            ObjectMapper objectMapper = new ObjectMapper();
            WeatherInfo weatherInfo = objectMapper.readValue(sb.toString(), WeatherInfo.class);

            log.info("Weather info: {}" + weatherInfo.toString());

            return weatherInfo;


        } catch (Exception e) {
            log.error("Error while fetching weather data", e);
            return null;

        } finally {
            connection.disconnect();
        }

    }

}