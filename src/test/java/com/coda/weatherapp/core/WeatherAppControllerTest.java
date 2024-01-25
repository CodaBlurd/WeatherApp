package com.coda.weatherapp.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherAppControllerTest {

    private WeatherAppController controller;

    @Mock
    private HttpURLConnection mockConnection;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize controller with the mocked HttpURLConnection
        controller = new WeatherAppController(mockConnection);

        // Set up a default response for the mockConnection
        String sampleResponse = "{\"main\":{\"temp\":298.15},\"weather\":[{\"main\":\"Sunny\"}]}";
        InputStream inputStream = new ByteArrayInputStream(sampleResponse.getBytes());
        when(mockConnection.getInputStream()).thenReturn(inputStream);
    }

    @Test
    public void testGetWeatherSuccess() throws Exception {
        WeatherInfo weatherInfo = controller.getWeather("London");

        assertNotNull(weatherInfo);
        assertEquals(25, weatherInfo.getMain().getTemp());
        assertEquals("Sunny", weatherInfo.getWeather().get(0).getMain());
    }

    @Test
    public void testGetWeatherFailure() throws Exception {
        when(mockConnection.getInputStream()).thenThrow(new IOException());

        WeatherInfo weatherInfo = controller.getWeather("London");

        assertNull(weatherInfo);
    }

    // Additional tests for error handling, invalid responses, etc.
}
