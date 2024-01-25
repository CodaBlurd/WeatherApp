module com.coda.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;


    exports com.coda.weatherapp.core;
    opens com.coda.weatherapp.core to javafx.fxml;
}