module com.coda.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;


    opens com.coda.weatherapp to javafx.fxml;
    exports com.coda.weatherapp;
}