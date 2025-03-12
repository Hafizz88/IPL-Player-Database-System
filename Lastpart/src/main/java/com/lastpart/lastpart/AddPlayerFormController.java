package com.lastpart.lastpart;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPlayerFormController {

    @FXML
    private TextField playerNameField;

    @FXML
    private TextField playerPositionField;

    @FXML
    private TextField playerPriceField;

    @FXML
    private TextField playerCountryField;  // New Field for Country

    @FXML
    private TextField playerHeightField;   // New Field for Height

    @FXML
    private TextField playerAgeField;      // New Field for Age

    private boolean playerAdded = false;
    private Player newPlayer;

    @FXML
    private void savePlayer() {
        String name = playerNameField.getText();
        String position = playerPositionField.getText();
        double price = 0;
        String country = playerCountryField.getText();
        double height = 0;
        int age = 0;

        try {
            price = Double.parseDouble(playerPriceField.getText());
        } catch (NumberFormatException e) {
            playerPriceField.setPromptText("Invalid Price");
            return;
        }

        try {
            height = Double.parseDouble(playerHeightField.getText());
        } catch (NumberFormatException e) {
            playerHeightField.setPromptText("Invalid Height");
            return;
        }

        try {
            age = Integer.parseInt(playerAgeField.getText());
        } catch (NumberFormatException e) {
            playerAgeField.setPromptText("Invalid Age");
            return;
        }

        // Create the new player object with the additional fields
        newPlayer = new Player(name, position, price, country, height, age);

        // Mark that the player has been added
        playerAdded = true;

        // Close the form
        closeForm();
    }

    @FXML
    private void cancel() {
        playerAdded = false;
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) playerNameField.getScene().getWindow();
        stage.close();
    }

    public boolean isPlayerAdded() {
        return playerAdded;
    }

    public Player getNewPlayer() {
        return newPlayer;
    }
}

