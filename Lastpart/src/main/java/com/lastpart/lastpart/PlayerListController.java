package com.lastpart.lastpart;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PlayerListController {

    @FXML
    private ScrollPane scrollPane; // Ensure the ScrollPane is properly linked

    @FXML
    private GridPane gridPane; // The grid where player cards will be placed

    private ClubHomeController clubHomeController; // ClubHomeController to get player data

    // This method will load the player cards into the GridPane dynamically
    public void loadPlayerCards(List<Player> playerList) {
        try {
            int row = 0;  // Row counter for GridPane
            int col = 0;  // Column counter for GridPane

            for (Player player : playerList) {
                // Load each player card from the FXML file
                FXMLLoader fxmlLoader = new FXMLLoader();

                // Find the path to the FXML for the player card
                URL fxmlResource = getClass().getResource("/com/lastpart/lastpart/Player.fxml");

                if (fxmlResource == null) {
                    throw new IllegalStateException("FXML file not found at /com/lastpart/lastpart/Player.fxml");
                }

                fxmlLoader.setLocation(fxmlResource);

                // Load the player card layout
                Parent card = fxmlLoader.load();

                // Access the PlayerController for the loaded FXML and set data
                PlayerController playerCardController = fxmlLoader.getController();
                playerCardController.setData(player);  // Set player-specific data
                playerCardController.setClubHomeWindowController(this.clubHomeController);  // Pass reference to club controller

                // Add the card to the grid at the correct row and column
                gridPane.add(card, col, row++);
                card.getStyleClass().add(player.getClub().replace(' ', '_')); // Add club-specific styles

                // Set the margin around each card for proper spacing
                GridPane.setMargin(card, new Insets(10));

                // Check if the row is filled (for wrapping) and move to the next column if needed
                if (row > 3) {  // If 4 rows are filled, go to the next column
                    row = 0;
                    col++;
                }
            }

            // Allow the ScrollPane to dynamically size to fit the content
            gridPane.setMinHeight(Region.USE_PREF_SIZE);
            gridPane.setMaxHeight(Region.USE_PREF_SIZE);

            // Setting the VBox to allow scrolling
            scrollPane.setFitToWidth(true);
            scrollPane.setContent(gridPane);

        } catch (IOException e) {
            System.err.println("Error loading player cards: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Getter for ClubHomeController
    public ClubHomeController getClubHomeWindowController() {
        return clubHomeController;
    }

    // Setter for ClubHomeController to pass the reference from ClubHomeController
    public void setClubHomeWindowController(ClubHomeController clubHomeWindowController) {
        this.clubHomeController = clubHomeWindowController;
    }
}
