package com.coda.weatherapp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class WeatherAppUI extends Application {

    private static final Logger log = LoggerFactory.getLogger(WeatherAppUI.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Weather App");

        // Create UI components
        Label locationLabel = new Label("Enter Location:");
        TextField locationTextField = new TextField();
        Button fetchButton = new Button("Fetch Weather");
        VBox weatherDataVBox = new VBox(10); // To display weather data
        Label errorMessage = new Label();

        // Define the action for the fetchButton
        fetchButton.setOnAction(event -> {
            String location = locationTextField.getText();
            try {
                WeatherAppController weatherAppController = new WeatherAppController();
                WeatherInfo weatherInfo = weatherAppController.getWeather(location);
                log.info("Weather info: {}", weatherInfo);

                if (weatherInfo != null) {
                    // Clear previous data
                    clearWeatherData(weatherDataVBox);

                    // Display weather information
                    displayWeatherData(weatherInfo, weatherDataVBox);

                    clearWeatherDataAfterDelay(weatherDataVBox);


                } else {
                    // Handle cases where the API response is empty or null
                    errorMessage.setText("Error: Empty or null API response");
                }
            } catch (Exception e) {
                // Handle exceptions
                errorMessage.setText("Error: Unable to fetch weather data");
            }
        });

        // Create a VBox to hold the UI components
        VBox vbox = new VBox(10); // 10 pixels spacing
        vbox.setPadding(new Insets(20, 20, 20, 20)); // Insets (top, right, bottom, left)
        vbox.getChildren().addAll(locationLabel, locationTextField, fetchButton, errorMessage, weatherDataVBox);

        // Create the Scene
        Scene scene = new Scene(vbox, 400, 400); // Set scene width and height


//         Load the CSS file with the correct resource path

        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

        } catch (NullPointerException e) {
            errorMessage.setText("Cannot fetch resource: " + e.getMessage());

        }


// Create a drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(3);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        dropShadow.setColor(javafx.scene.paint.Color.rgb(0, 0, 0, 0.2));

        // Apply styles to UI components
        locationLabel.getStyleClass().add("label");
        locationTextField.getStyleClass().add("text-field");
        fetchButton.getStyleClass().add("button");
        // Apply the drop shadow effect to your node
        weatherDataVBox.setEffect(dropShadow);
        errorMessage.getStyleClass().add("error-message");
        vbox.getStyleClass().add("vbox");

        // Set the scene on the primary stage
        primaryStage.setScene(scene);

        // Show the primary stage
        primaryStage.show();
    }

    // Helper method to display weather data
    private void displayWeatherData(WeatherInfo weatherInfo, VBox weatherDataVBox) {
        // Add labels to display various weather data fields
        weatherDataVBox.getChildren().addAll(
                new Label("Location: " + weatherInfo.getName()),
                new Label("Temperature: " + String.format("%.0f°C", weatherInfo.getMain().getTemp())),
                new Label("Humidity: " + weatherInfo.getMain().getHumidity() + "%"),
                new Label("Pressure: " + weatherInfo.getMain().getPressure() + " hPa"),
                new Label("Weather: " + weatherInfo.getWeather().get(0).getDescription())
        );
    }

    // Define a Timeline for clearing weather data after 1 minute
    private void clearWeatherDataAfterDelay(VBox weatherDataVBox) {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(5.0), // Adjust the delay duration as needed
                        event -> clearWeatherData(weatherDataVBox)
                )
        );
        timeline.setCycleCount(1); // Run only once
        timeline.play();
    }

    private void clearWeatherData(VBox weatherDataVBox) {
        weatherDataVBox.getChildren().clear();
    }


}
