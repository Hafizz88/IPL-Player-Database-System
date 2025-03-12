package com.lastpart.lastpart;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerDetailsController implements Initializable  {
    @FXML
    private ImageView playerImage;

    @FXML
    private Label playerNameLabel;

    @FXML
    private Label playerPositionLabel;

    @FXML
    private Label playerClubLabel;

    @FXML
    private Label playerCountryLabel;

    @FXML
    private Label playerAgeLabel;

    @FXML
    private Label playerSalaryLabel;

    @FXML
    private Label playerHeightLabel;

    @FXML
    private Label playerNumberLabel;
    private Player player;

    public void setData(Player player) {
        playerImage.setImage(new Image(getClass().getResourceAsStream(player.getImgSource())));
        playerNameLabel.setText(player.getName());
        playerPositionLabel.setText(player.getPosition());
        playerClubLabel.setText(player.getClub());
        playerCountryLabel.setText(player.getCountry());
        playerCountryLabel.setStyle("-fx-font-family: Cambria");
        playerAgeLabel.setText("Age: " + player.getAge() + " years");
        playerHeightLabel.setText("Height: " + player.getHeight() + " meters");
        playerNumberLabel.setText("Number: " + player.getNumber());
        playerSalaryLabel.setText("Weekly Salary: " + String.format("%,.2f", player.getSalary()) + " USD");
    }
    public void setPlayer(Player player) {
        this.player = player;

        playerNameLabel.setText(player.getName());
        playerPositionLabel.setText(player.getPosition());

        // ✅ FIX: Proper Formatting for Numbers
        playerAgeLabel.setText("Age: " + player.getAge());  // Age is an integer, so it's fine
        playerHeightLabel.setText("Height: " + String.format("%.2f", player.getHeight()) + "m");  // Use %f for float
        playerSalaryLabel.setText("Weekly Salary: $" + String.format("%,.2f", player.getSalary()));  // Use %,.2f for salary

        playerClubLabel.setText("Club: " + player.getClub());
        playerCountryLabel.setText("Country: " + player.getCountry());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
