package com.lastpart.lastpart;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LoginController {

    @FXML
    private ChoiceBox<String> usernameChoiceBox;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button registerButton;

    private Client client;

    @FXML
    void login(ActionEvent event) {
        String username = usernameChoiceBox.getValue();
        String password = passwordTextField.getText();
        if (username == null || password.isBlank()) {
            showAlert("Login");
        } else {
            client.loginClub(username, password);
            reset(event);
        }
    }

    private void showAlert(String header) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(header + " not successful");
        a.setContentText("Username or password field cannot be empty.");
        a.showAndWait();
    }

    @FXML
    void register(ActionEvent event) {
        String username = usernameChoiceBox.getValue();
        if (username == null) {
            showAlert("Registration request");
        } else {
            try {
                client.showRegWindow(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void reset(ActionEvent event) {
        usernameChoiceBox.setValue(null);
        passwordTextField.setText("");
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void init() {
        initializeUsernameChoiceBox();
    }

    private void initializeUsernameChoiceBox() {
        // List of current IPL clubs
        List<String> clubList = List.of(
                "Chennai Super Kings",
                "Delhi Capitals",
                "Gujarat Titans",
                "Kolkata Knight Riders",
                "Lucknow Super Giants",
                "Mumbai Indians",
                "Punjab Kings",
                "Rajasthan Royals",
                "Royal Challengers Bangalore",
                "Sunrisers Hyderabad"
        );


        usernameChoiceBox.getItems().addAll(clubList);


        usernameChoiceBox.setValue(clubList.get(0));
    }
}


