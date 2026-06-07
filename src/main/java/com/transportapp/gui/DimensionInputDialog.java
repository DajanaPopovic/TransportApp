package com.transportapp.gui;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * A dialog window that allows the user to input the dimensions of the map.
 * This class creates a simple interface with two text fields for the user to input the X and Y dimensions,
 * and a confirm button to proceed after the dimensions are entered.
 *
 * The dialog ensures that only valid integer values are accepted for the dimensions.
 * If invalid input is provided, an error message is displayed.
 */
public class DimensionInputDialog {

    /**
     * Array to hold the entered dimensions.
     * The first element (index 0) represents the X dimension, and the second element (index 1) represents the Y dimension.
     */
    private int[] dimensions;

    /**
     * Gets the dimensions entered by the user.
     *
     * @return an array of integers representing the X and Y dimensions
     */
    public int[] getDimensions() {
        return dimensions;
    }

    /**
     * Displays a dialog window to the user, allowing them to input the dimensions of the map.
     * The dialog will prompt for the X and Y dimensions and provide a confirmation button.
     * After confirmation, the dialog will close and the entered dimensions will be stored.
     *
     * If invalid input is entered (non-integer values), an error alert will be shown.
     *
     * @param parentStage The main application stage used to initialize the dialog window. This will be the owner of the dialog.
     */
    public void showDimensionInputDialog(Stage parentStage) {

        Stage dialogStage = new Stage();
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("Unesite dimenzije");

        TextField xInput = new TextField();
        xInput.setPromptText("Unesi X dimenziju");
        TextField yInput = new TextField();
        yInput.setPromptText("Unesi Y dimenziju");

        Button confirmButton = new Button("Potvrdi");

        confirmButton.setOnAction(event -> {
            try {
                int x = Integer.parseInt(xInput.getText());
                int y = Integer.parseInt(yInput.getText());
                dimensions = new int[]{x, y};
                dialogStage.close();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Netacan unos");
                alert.setHeaderText("Unesite ispravne cjelobrojne vrijednosti kao dimenzije.");
                alert.showAndWait();
            }
        });

        VBox vbox = new VBox(10, xInput, yInput, confirmButton);
        Scene scene = new Scene(vbox, 300, 200);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }
}

