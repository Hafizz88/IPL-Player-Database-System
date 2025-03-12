package com.lastpart.lastpart;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class PlayerController {

    @FXML
    private ImageView playerImage;

    @FXML
    private Label playerNameLabel;

    @FXML
    private Label playerPositionLabel;

    @FXML
    private Label playerPriceLabel;

    @FXML
    private Button playerSellButton;

    private Player player; // Correctly storing player reference
    private ClubHomeController clubHomeController;

    // ✅ **Set player data correctly**
    public void setData(Player player) {
        this.player = player;
        playerNameLabel.setText(player.getName());
        playerPositionLabel.setText(player.getPosition());
        playerPriceLabel.setVisible(false); // Hide price initially

        System.out.println("Setting data for player: " + player.getName()); // Debug log

        // ✅ **Fix the image path**
        String sanitizedPlayerName = player.getName().replace(" ", "_"); // Replace spaces with underscores
        String playerImagePath = "/com/lastpart/images/player/" + sanitizedPlayerName + ".png";

        // ✅ **Try loading the image**
        InputStream imageStream = getClass().getResourceAsStream(playerImagePath);

        if (imageStream == null) {
            System.err.println("Player image not found: " + playerImagePath);
            // Load default player image
            playerImagePath = "/com/lastpart/images/default-player.png";
            imageStream = getClass().getResourceAsStream(playerImagePath);
        }

        if (imageStream != null) {
            playerImage.setImage(new Image(imageStream));
        } else {
            System.err.println("Default player image not found!");
        }

        // ✅ **Handle Buy/Sell Button and Price**
        if (player.isInTransferList()) {
            playerSellButton.setText("Buy");
            playerPriceLabel.setVisible(true);
            playerPriceLabel.setText("Price: $" + String.format("%,.2f", player.getPrice()));
        } else {
            playerSellButton.setText("Sell");
        }
    }

    // ✅ **Show Player Details with Correct Data**
    @FXML
    void showPlayerDetails(ActionEvent event) {
        try {
            System.out.println("Opening details for: " + player.getName()); // Debugging log

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lastpart/lastpart/PlayerDetails.fxml"));
            Parent root = loader.load();

            // Get the controller and set the correct player object
            PlayerDetailsController controller = loader.getController();
            controller.setPlayer(this.player); // Ensure correct player is set

            // Open the details window
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(player.getName());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ✅ **Ensure sellPlayer method correctly interacts with ClubHomeController**
    @FXML
    void sellPlayer(ActionEvent event) {
        // Open the Sale Confirmation window for the selected player
        if (clubHomeController != null) {
            try {
                // Load the SaleConfirmation.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lastpart/lastpart/SaleConfirmation.fxml"));
                Parent root = loader.load();

                // Get the controller for SaleConfirmation.fxml
                SaleConfimationController controller = loader.getController();
                controller.setPlayer(player);  // Pass the selected player to SaleConfirmationController

                // Create a new Stage for the Sale Confirmation Dialog
                Stage stage = new Stage();
                stage.setTitle("Confirm Sale");
                stage.setScene(new Scene(root));
                stage.showAndWait();  // Wait for the user to interact with the dialog

                // After the user confirms the sale, check if it's confirmed
                if (controller.isSaleConfirmed()) {
                    // Proceed with the sale after confirmation
                    double playerPrice = player.getPrice();
                    clubHomeController.sellPlayer(player.getName(), playerPrice);  // Sell the player
                } else {
                    System.out.println("Sale was canceled.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("ClubHomeController is null! Cannot sell player.");
        }
    }


    public void setClubHomeWindowController(ClubHomeController clubHomeController) {
        this.clubHomeController = clubHomeController;
    }



    @FXML
    void showSellButton(MouseEvent event) {
        playerSellButton.setVisible(true);
    }

    @FXML
    void hideSellButton(MouseEvent event) {
        playerSellButton.setVisible(false);
    }


}
