package com.lastpart.lastpart;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SaleConfimationController {

    @FXML
    private Label playerNameLabel;

    @FXML
    private TextField playerPriceTextField;

    @FXML
    private Button confirmSaleButton;

    @FXML
    private Button priceClearButton;

    @FXML
    private Button cancelSaleButton;

    private Player player;
    private boolean saleConfirmed = false;

    /**
     * Handles the confirm action.
     */
    @FXML
    void confirmSale() {
        try {
            double price = Double.parseDouble(playerPriceTextField.getText().trim());
            if (price <= 0) {
                throw new NumberFormatException(); // Ensure price is positive
            }
            player.setPrice(price);
            saleConfirmed = true;
            closeWindow();
        } catch (NumberFormatException e) {
            playerPriceTextField.clear();
            playerPriceTextField.setPromptText("Invalid price! Enter a positive number.");
        }
    }

    /**
     * Handles the clear action for the price input field.
     */
    @FXML
    void clearPriceTextField() {
        playerPriceTextField.clear();
    }

    /**
     * Handles the cancel action.
     */
    @FXML
    void cancelSale() {
        saleConfirmed = false;
        closeWindow();
    }

    /**
     * Sets the player details for confirmation.
     *
     * @param player the player to confirm the sale for
     */
    public void setPlayer(Player player) {
        this.player = player;
        playerNameLabel.setText("Set a Selling Price for " + player.getName());
    }

    /**
     * Returns whether the sale was confirmed.
     *
     * @return true if the sale was confirmed, false otherwise
     */
    public boolean isSaleConfirmed() {
        return saleConfirmed;
    }

    /**
     * Closes the window.
     */
    private void closeWindow() {
        Stage stage = (Stage) confirmSaleButton.getScene().getWindow();
        stage.close();
    }
}
