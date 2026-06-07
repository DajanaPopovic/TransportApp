package com.transportapp;

import com.transportapp.generator.*;
import com.transportapp.model.*;
import com.transportapp.gui.*;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point of the Transport Application.
 * This class initializes the application, generates and loads transport data,
 * and launches the JavaFX GUI for dimensions input and route selection.
 */
public class Main extends Application {

    /**
     * Holds the loaded transport data used throughout the application.
     */
    private static TransportData transportData;

    @Override
    public void start(Stage primaryStage) {

        DimensionInputDialog dimensionInputDialog = new DimensionInputDialog();
        dimensionInputDialog.showDimensionInputDialog(primaryStage);

        int[] dimensions = dimensionInputDialog.getDimensions();
        int xDimension = dimensions[0];
        int yDimension = dimensions[1];

        TransportDataGenerator generator = new TransportDataGenerator(xDimension, yDimension);
        TransportData data = generator.generateData();
        generator.saveToJson(data, "transport_data.json");

        transportData = TransportData.readValue("transport_data.json");
        //transportData.printDepartures();

        RouteSelectionScreen screen = new RouteSelectionScreen(transportData);
        screen.show(primaryStage);
    }

/**
 * The main entry point of the JavaFX application.
 */
    public static void main(String[] args) {
        launch(args);
    }
}
