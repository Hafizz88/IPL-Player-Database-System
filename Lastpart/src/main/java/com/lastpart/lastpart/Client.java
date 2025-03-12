package com.lastpart.lastpart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Client extends Application {
    private Stage stage;
    private Networking networking;
    private RefreshThread refreshThread;

    public static void main(String[] args) {
        launch(args);
    }

    private void connectToServer() throws IOException {
        String serverAddress = "127.0.0.1"; // Localhost
        int serverPort = 45045;            // Port used by the server
        networking = new Networking(serverAddress, serverPort);
        System.out.println("Connected to server at " + serverAddress + ":" + serverPort);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        try {
            connectToServer();
            showLoginPage();
        } catch (Exception e) {
            showError("Error starting application", e.getMessage());
            e.printStackTrace();
        }
    }

    private void showLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lastpart/lastpart/ClubLogin.fxml"));
            Parent root = loader.load();

            LoginController controller = loader.getController();
            controller.setClient(this);
            controller.init();

            Scene scene = new Scene(root);
            stage.setTitle("Club Login");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Login Page Error", "Failed to load the login page.");
            e.printStackTrace();
        }
    }

    private void showClubHomePage(CLUB club) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lastpart/lastpart/ClubHome.fxml"));
        Parent root = loader.load();

        ClubHomeController controller = loader.getController();
        if (controller != null) {
            System.out.println("DEBUG: Passing club to ClubHomeController -> " + club.getName());
            controller.init(this, club); // Initialize the controller with the club object
        } else {
            System.err.println("ERROR: ClubHomeController is null!");
        }

        Scene scene = new Scene(root);
        stage.setTitle(club.getName());
        stage.setScene(scene);
        stage.setX(10);
        stage.setY(10);
        stage.show();
    }


    public void showRegWindow(String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lastpart/lastpart/ClubRegistration.fxml"));
        Parent root = loader.load();

        RegistrationController controller = loader.getController();
        controller.setClient(this);
        controller.setUserNameLabelText(username);

        Scene scene = new Scene(root);
        stage.setOnCloseRequest(e -> showLoginPage());

        stage.setTitle("Registration");
        stage.setX(375);
        stage.setY(80);
        stage.setScene(scene);
        stage.show();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void loginClub(String username, String password) {
        LoginInformation loginInfo = new LoginInformation(MessageHeader.LOGIN, username, password);
        try {
            networking.write(loginInfo);
            Object obj = networking.read();
            if (obj instanceof Boolean) {
                Boolean success = (Boolean) obj;
                if (success) {
                    CLUB club = loadClubFromServer(username);
                    if (club != null) {
                        showClubHomePage(club);
                    } else {
                        showError("Login Error", "Failed to load club information.");
                    }
                } else {
                    showError("Login Error", "Login unsuccessful. Check your credentials.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            showError("Login Error", "An error occurred during login.");
            e.printStackTrace();
        }
    }

    public void registerClub(String username, String password) {
        LoginInformation loginInfo = new LoginInformation(MessageHeader.REGISTER, username, password);
        try {
            networking.write(loginInfo);
            Object obj = networking.read();
            if (obj instanceof Boolean) {
                Boolean isRegistered = (Boolean) obj;
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setHeaderText("Registration Window");
                if (isRegistered) {
                    alert.setAlertType(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setContentText("Registration is successful.");
                } else {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Registration failed. The club may already be registered.");
                }
                alert.showAndWait();
                showLoginPage();
            }
        } catch (IOException | ClassNotFoundException e) {
            showError("Registration Error", "An error occurred during registration.");
            e.printStackTrace();
        }
    }

    public void logoutClub(String clubName) {
        try {
            networking.write(new Message(MessageHeader.LOGOUT, clubName));
            Object obj = networking.read();
            if (obj instanceof Boolean) {
                Boolean success = (Boolean) obj;
                if (success) {
                    interruptRefreshThread();
                    showLoginPage();
                } else {
                    showError("Logout Error", "Logout unsuccessful.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            showError("Logout Error", "An error occurred during logout.");
            e.printStackTrace();
        }
    }

    public boolean sellPlayer(String playerName, double playerPrice) {
        try {
            // Send the sale information to the server
            networking.write(new SaleInformation(MessageHeader.SELL, playerName, playerPrice));
            Object obj = networking.read();
            if (obj instanceof Boolean) {
                Boolean success = (Boolean) obj;
                if (!success) {
                    showError("Sell Error", "Player could not be sold.");
                } else {
                    // After successfully selling, add the player to the transfer list
                    // Networking write to inform all clubs about the transfer
                    networking.write(new SaleInformation(MessageHeader.TRANSFER_LIST, playerName, playerPrice));
                }
                return success;
            }
        } catch (IOException | ClassNotFoundException e) {
            showError("Sell Error", "An error occurred while selling the player.");
            e.printStackTrace();
        }
        return false;
    }


    public boolean buyPlayer(String playerName, String clubName) {
        try {
            networking.write(new Buying(MessageHeader.BUY, playerName, clubName));
            Object obj = networking.read();
            if (obj instanceof Boolean) {
                Boolean success = (Boolean) obj;
                if (!success) {
                    showError("Buy Error", "Player is unavailable or already bought.");
                }
                return success;
            }
        } catch (IOException | ClassNotFoundException e) {
            showError("Buy Error", "An error occurred while buying the player.");
            e.printStackTrace();
        }
        return false;
    }

    public CLUB loadClubFromServer(String clubName) {
        try {
            networking.write(new Message(MessageHeader.CLUB_INFO, clubName));
            Object obj = networking.read();
            if (obj instanceof CLUB) {
                return (CLUB) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            showError("Load Error", "An error occurred while loading the club.");
            e.printStackTrace();
        }
        return null;
    }

    public List<?> loadClubList() {
        try {
            networking.write(new Message(MessageHeader.CLUB_LIST, null));
            Object obj = networking.read();
            if (obj instanceof List) {
                return (List<?>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            showError("Load Error", "An error occurred while loading the club list.");
            e.printStackTrace();
        }
        return null;
    }

    synchronized public List<?> loadTransferList() {
        try {
            networking.write(new Message(MessageHeader.TRANSFER_WINDOW, null));
            Object obj = networking.read();
            if (obj instanceof List) {
                return (List<?>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            showError("Load Error", "An error occurred while loading the transfer list.");
            e.printStackTrace();
        }
        return null;
    }

    synchronized public void startRefreshThread(ClubHomeController clubHomeWindowController) {
        refreshThread = new RefreshThread(clubHomeWindowController);
    }

    synchronized public void interruptRefreshThread() {
        if (refreshThread != null) refreshThread.getThread().interrupt();
    }

    public Networking getNetworking() {
        return networking;
    }
}
