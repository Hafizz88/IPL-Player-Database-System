package com.lastpart.lastpart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClubHomeController {


    @FXML
    private ImageView clubLogoImage;
    @FXML
    private Label clubNameFirstLine;
    @FXML
    private Label clubNameSecondLine;
    @FXML
    private Label clubBudgetLabel;
    @FXML
    private Button buyPlayerButton;
    @FXML
    private VBox bodyVBox;
    @FXML
    private HBox topBarHBox;
    @FXML
    private TextField searchPlayerNameTextField;
    @FXML
    private Button searchPlayerNameButton;
    @FXML
    private Button resetPlayerNameButton;
    @FXML
    private HBox refreshRateHBox;
    @FXML
    private ChoiceBox<String> refreshRateChoiceBox;
    @FXML
    private MenuButton clubMenuButton;
    @FXML
    private MenuItem usernameMenuItem;
    @FXML
    private HBox listPlayerHBox;
    @FXML
    private VBox playerListVBox;
    @FXML
    private TreeView<CheckBox> filterTreeCountry;
    @FXML
    private TreeView<CheckBox> filterTreePosition;
    @FXML
    private TextField ageFromTextField;
    @FXML
    private TextField ageToTextField;
    @FXML
    private TextField heightFromTextField;
    @FXML
    private TextField salaryFromTextField;
    @FXML
    private TextField heightToTextField;
    @FXML
    private TextField salaryToTextField;
    @FXML
    private Button applyFiltersButton;
    @FXML
    private Button resetFiltersButton;
    @FXML
    private HBox bottomBarHBox;

    private CLUB club;
    private String clubName;
    private String logoImgSource;
    private List<Player> playerListOnDisplay;
    private Client client;
    static volatile private int refreshRate;
    private ActionEvent event;
    public void addPlayerToUI(Player player) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lastpart/lastpart/Player.fxml"));
            Parent playerCard = loader.load();

            PlayerController controller = loader.getController();
            controller.setData(player);
            controller.setClubHomeWindowController(this);

            playerListVBox.getChildren().add(playerCard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Load Players into UI
    public void loadPlayers() {
        playerListVBox.getChildren().clear();
        for (Player player : club.getPlayers()) {
            addPlayerToUI(player);
        }
    }


    @FXML
    void showTransferWindow(ActionEvent event) {
        this.event = event;
        if (buyPlayerButton.getText().equals("Buy Player")) {
            client.startRefreshThread(this);
            refreshRateHBox.setVisible(true);
            buyPlayerButton.setText("Home");
        } else {
            client.interruptRefreshThread();
            refreshRateHBox.setVisible(false);
            buyPlayerButton.setText("Buy Player");
            loadPlayerCards(this.club.getPlayers());
        }
    }

    void loadTransferWindow() {
        List<?> players = this.client.loadTransferList();
//        System.out.println(players);
        if (players != null) {
            List<Player> playerList = new ArrayList<>();
            for (Object e : players) {
                if (e instanceof Player && !((Player) e).getClub().equals(this.clubName)) {
                    playerList.add((Player) e);
                }
            }
            loadPlayerCards(playerList);
        }

    }

    @FXML
    void searchPlayerByName(ActionEvent event) {
        this.event = event;
        String playerName = searchPlayerNameTextField.getText().trim();
        Datahouse db = new Datahouse();
        db.addPlayer(club.getPlayers());
        Player player = db.searchPlayerByName(playerName);
        db.setPlayerList(new ArrayList<>());
        if (player != null) {
            db.getPlayerList().add(player);
        }
        loadPlayerCards(db.getPlayerList());
    }

    @FXML
    void resetPlayerNameTextField(ActionEvent event) {
        this.event = event;
        searchPlayerNameTextField.setText("");
        loadPlayerCards(club.getPlayers());
    }

    @FXML
    void applyFilters(ActionEvent event) {
        this.event = event;
        Datahouse db = new Datahouse();
        db.addPlayer(club.getPlayers());
        applyFiltersCountry(db);
        applyFiltersPosition(db);
        applyFiltersAge(db);
        applyFiltersHeight(db);
        applyFiltersSalary(db);
        loadPlayerCards(db.getPlayerList()); // Load the filtered players to UI
    }


    private void applyFiltersSalary(Datahouse db) {
        double lo, hi;
        try {
            lo = Double.parseDouble(salaryFromTextField.getText());
        } catch (Exception e) {
            lo = 0;
        }
        try {
            hi = Double.parseDouble(salaryToTextField.getText());
        } catch (Exception e) {
            hi = Double.MAX_VALUE;
        }
        db.setPlayerList(db.searchPlayerBySalary(lo, hi));
    }


    private void applyFiltersHeight(Datahouse db) {
        double lo, hi;
        try {
            lo = Double.parseDouble(heightFromTextField.getText());
        } catch (Exception e) {
            lo = 0;
        }
        try {
            hi = Double.parseDouble(heightToTextField.getText());
        } catch (Exception e) {
            hi = Double.MAX_VALUE;
        }
        db.setPlayerList(db.searchPlayerByHeight(lo, hi));
    }
    private void applyFiltersAge(Datahouse db) {
        int lo, hi;
        try {
            lo = Integer.parseInt(ageFromTextField.getText());
        } catch (Exception e) {
            lo = 0;
        }
        try {
            hi = Integer.parseInt(ageToTextField.getText());
        } catch (Exception e) {
            hi = Integer.MAX_VALUE;
        }
        db.setPlayerList(db.searchPlayerByAge(lo, hi));
    }

    private void applyFiltersPosition(Datahouse db) {
        for (TreeItem<CheckBox> item : filterTreePosition.getRoot().getChildren()) {
            if (item.getValue().isSelected()) {
                // Apply position filter
                String position = item.getValue().getText();
                db.setPlayerList(db.searchPlayerByPosition(position));
            }
        }
    }

    private void applyFiltersCountry(Datahouse db) {
        for (TreeItem<CheckBox> item : filterTreeCountry.getRoot().getChildren()) {
            if (item.getValue().isSelected()) {
                // Apply country filter by calling Datahouse's searchPlayerByCountry method
                String country = item.getValue().getText();
                db.setPlayerList(db.searchPlayerByCountry(country));
            }
        }
    }


    @FXML
    void resetFilters(ActionEvent event) {
        for (TreeItem<CheckBox> item:
                filterTreeCountry.getRoot().getChildren()) {
            if (!item.getValue().isSelected()) {
                item.getValue().setSelected(true);
            }
        }

        // reset position
        for (TreeItem<CheckBox> item:
                filterTreePosition.getRoot().getChildren()) {
            if (!item.getValue().isSelected()) {
                item.getValue().setSelected(true);
            }
        }
        resetTreeSelections(filterTreeCountry);
        resetTreeSelections(filterTreePosition);
        ageFromTextField.setText("");
        ageToTextField.setText("");
        heightFromTextField.setText("");
        heightToTextField.setText("");
        salaryFromTextField.setText("");
        salaryToTextField.setText("");
        applyFilters(event);
    }

    private void resetTreeSelections(TreeView<CheckBox> tree) {
        for (TreeItem<CheckBox> item : tree.getRoot().getChildren()) {
            item.getValue().setSelected(true);
        }
    }








    private void makeMenu() {
        clubMenuButton.setText("");

        try {
            String encodedImgSource = URLEncoder.encode(club.getImgSource(), StandardCharsets.UTF_8);
            String menuImgSource = "/com/lastpart/images/logo/" + encodedImgSource;

            InputStream imageStream = getClass().getResourceAsStream(menuImgSource);
            if (imageStream == null) {
                throw new IOException("Menu image not found: " + menuImgSource);
            }

            ImageView imageView = new ImageView(new Image(imageStream));
            imageView.setFitHeight(25);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            clubMenuButton.setGraphic(imageView);
        } catch (IOException e) {
            System.err.println("Failed to load menu image: " + e.getMessage());
            e.printStackTrace();
        }

        usernameMenuItem.setText("Signed in as " + clubName);
    }

    private void makeFilterTree() {
        makeFilterTreeCountry();
        makeFilterTreePosition();
    }

    private void makeFilterTreeCountry() {
        TreeItem<CheckBox> root = new TreeItem<>();
        root.setExpanded(true);  // Expand the root by default

        // Get unique countries from the club
        List<String> countries = club.getCountryList();

        // Add countries to the tree
        for (String country : countries) {
            CheckBox checkBox = new CheckBox(country);
            checkBox.setSelected(true);  // Set checkbox to be selected by default
            TreeItem<CheckBox> item = new TreeItem<>(checkBox);
            root.getChildren().add(item);
        }

        filterTreeCountry.setRoot(root);  // Set the root for country tree
        filterTreeCountry.setShowRoot(false);  // Hide the root
    }

    private void makeFilterTreePosition() {
        TreeItem<CheckBox> root = new TreeItem<>();
        root.setExpanded(true);  // Expand the root by default

        // Get unique positions from the club
        List<String> positions = club.getPositionList();

        // Add positions to the tree
        for (String position : positions) {
            CheckBox checkBox = new CheckBox(position);
            checkBox.setSelected(true);  // Set checkbox to be selected by default
            TreeItem<CheckBox> item = new TreeItem<>(checkBox);
            root.getChildren().add(item);
        }

        filterTreePosition.setRoot(root);  // Set the root for position tree
        filterTreePosition.setShowRoot(false);  // Hide the root
    }

   /* private TreeItem<CheckBox> makeBranchFilterTree(String title, TreeItem<CheckBox> parent) {
        CheckBox checkBox = new CheckBox(title);
        checkBox.setSelected(true);
        TreeItem<CheckBox> item = new TreeItem<>(checkBox);
        parent.getChildren().add(item);
        return item;
    }*/

    public void init(Client client, CLUB club) {
        this.client = client;
        this.club = club;

        // Debugging the club initialization and players
        System.out.println("DEBUG: Club initialized in ClubHomeController -> " + club.getName());
        System.out.println("DEBUG: Players in this club:");
        for (Player player : club.getPlayers()) {
            System.out.println(player.getName() + " - " + player.getPosition());
        }

        // Initialize Club Info (Logo, Name)
        initClubInfo();

        // Make sure to load the Country and Position filter lists
        makeFilterTree();

        // Load players in the club
        loadPlayers();
        initialize();
        loadPlayerCards(club.getPlayers());  // Load player cards for the club
        loadTransferList();  // Load transfer list

        // Optionally, you may want to make sure the club name is properly set
        clubName = club.getName();  // Store the club name for further usage
    }


    private void initClubInfo() {
        try {
            if (club == null) {
                System.err.println("Club is null, cannot initialize Club Info.");
                return;
            }

            String[] nameParts = club.getName().split(" ", 2);
            clubNameFirstLine.setText(nameParts.length > 0 ? nameParts[0] : "");
            clubNameSecondLine.setText(nameParts.length > 1 ? nameParts[1] : "");

            System.out.println("Club Name Updated -> " + club.getName());

            logoImgSource = "/com/lastpart/images/logo/" + club.getName().replace(" ", "_") + ".png";
            InputStream imageStream = getClass().getResourceAsStream(logoImgSource);

            if (imageStream != null) {
                clubLogoImage.setImage(new Image(imageStream));
            } else {
                System.err.println("Club logo not found: " + logoImgSource);
            }
        } catch (Exception e) {
            System.err.println("Error loading club info: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void loadPlayerCards(List<Player> playerList) {
        try {
            playerListVBox.getChildren().clear();

            for (Player player : playerList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/lastpart/lastpart/Player.fxml"));
                Parent playerCard = fxmlLoader.load();

                PlayerController controller = fxmlLoader.getController();
                controller.setData(player);
                controller.setClubHomeWindowController(this);

                playerListVBox.getChildren().add(playerCard);
            }
        } catch (IOException e) {
            System.err.println("Error loading Player.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


   @FXML
    MenuItem maxAgeMenuItem, maxHeightMenuItem, maxSalaryMenuItem, totalYearlySalaryMenuItem, logoutMenuItem;

    public void initialize() {
        if (club == null) {
            System.err.println("ERROR: Club is null in initialize.");
            return; // Don't proceed if the club is not initialized
        }

        // Initialize the MenuButton with club name
        clubMenuButton.setText("Signed in as " + club.getName());

        // Set up the actions for each menu item
        maxAgeMenuItem.setOnAction(this::listMaxAgePlayers);
        maxHeightMenuItem.setOnAction(this::listMaxHeightPlayers);
        maxSalaryMenuItem.setOnAction(this::listMaxSalaryPlayers);
        totalYearlySalaryMenuItem.setOnAction(this::showTotalYearlySalary);
        logoutMenuItem.setOnAction(this::logoutClub);
    }


    @FXML
    void listMaxAgePlayers(ActionEvent event) {
        loadPlayerCards(club.getMaxAgePlayers()); // Use existing method to filter by maximum age
    }

    @FXML
    void listMaxHeightPlayers(ActionEvent event) {
        loadPlayerCards(club.getMaxHeightPlayers()); // Use existing method to filter by maximum height
    }

    @FXML
    void listMaxSalaryPlayers(ActionEvent event) {
        loadPlayerCards(club.getMaxSalaryPlayers()); // Use existing method to filter by maximum salary
    }

    @FXML
    void showTotalYearlySalary(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Total Yearly Salary");
        alert.setHeaderText(this.clubName);
        alert.setContentText("Total yearly salary is " + String.format("%,.2f", this.club.getTotalYearlySalary()) + " USD");
        alert.showAndWait();
    }

    @FXML
    void logoutClub(ActionEvent event) {
        client.logoutClub(this.clubName);
    }

    // Update the transfer list after selling a player
    public void sellPlayer(String playerName, double playerPrice) {
        boolean success = client.sellPlayer(playerName, playerPrice);
        if (success) {
            club.removePlayer(playerName);  // Remove from current club's player list
            loadPlayerCards(club.getPlayers());  // Refresh club player list
            loadTransferList();  // Refresh transfer list to show the player
        } else {
            System.err.println("Failed to sell player: " + playerName);
        }
    }



    public void buyPlayer(Player player) {
        boolean success = client.buyPlayer(player.getName(), this.clubName);
        if (success) {
            // Remove the player from the transfer list and add them to the club’s roster
            player.setInTransferList(false);
            player.setClub(this.clubName);
            club.addPlayer(player);
            loadPlayerCards(club.getPlayers()); // Refresh the club's player list
            loadTransferList();  // Refresh the transfer list
        } else {
            System.err.println("Failed to buy player: " + player.getName());
        }
    }


    @FXML
    private void buyPlayerAction(ActionEvent event) {
        // Get the selected player from the transfer list (or where you're tracking it)
        Player selectedPlayer = getSelectedPlayer();

        if (selectedPlayer != null) {
            buyPlayer(selectedPlayer);  // This will call the buyPlayer method you already defined.
        } else {
            System.err.println("No player selected for buying.");
        }
    }

    @FXML
    private ListView<Player> transferPlayerListView;

    private Player selectedPlayer;


    // Handle player selection from the transfer list
    @FXML
    private void handlePlayerSelection(MouseEvent event) {
        selectedPlayer = transferPlayerListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            System.out.println("Selected player: " + selectedPlayer.getName());
        }
    }


    private Player getSelectedPlayer() {
        return selectedPlayer;  // Returns the selected player
    }

    // Method to load transfer players into the ListView
    private void loadTransferList() {
        List<Player> transferList = (List<Player>) client.loadTransferList();  // Get the updated transfer list from server
        ObservableList<Player> observableList = FXCollections.observableArrayList(transferList);  // Convert to ObservableList
        transferPlayerListView.setItems(observableList);  // Set the items of the ListView to the transfer list
    }

    public void refreshTransferList() {
        List<Player> transferList = (List<Player>) client.loadTransferList();  // Get the list of players in the transfer window
        ObservableList<Player> observableList = FXCollections.observableArrayList(transferList);
        transferPlayerListView.setItems(observableList);  // Set the items of the ListView to the transfer list
    }

    public static int getRefreshRate() {
        return refreshRate;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public void setClub(CLUB club) {
        this.club = club;

        if (club != null) {
            this.clubName = club.getName(); // Ensure clubName is set
            initClubInfo();
        } else {
            System.err.println("Club is null in setClub.");
        }
    }

    public ActionEvent getEvent() {
        return event;
    }

    public void setEvent(ActionEvent event) {
        this.event = event;
    }
    @FXML
    private void addPlayerAction(ActionEvent event) {
        // Show a dialog or a new window to input player details
        showAddPlayerForm();
    }

    private void showAddPlayerForm() {
        try {
            // Load the Add Player form (could be a separate FXML form)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lastpart/lastpart/AddPlayerForm.fxml"));
            Parent root = loader.load();

            // Get the controller for the Add Player form
            AddPlayerFormController controller = loader.getController();

            // Show the Add Player form as a new window
            Stage stage = new Stage();
            stage.setTitle("Add New Player");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // After closing the Add Player form, check if the player was added
            if (controller.isPlayerAdded()) {
                Player newPlayer = controller.getNewPlayer();
                addPlayerToClub(newPlayer); // Add the new player to the club
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Add Player form.");
        }
    }

    private void addPlayerToClub(Player newPlayer) {
        // Add the player to the club (this should update the club's player list)
        club.addPlayer(newPlayer);

        // Refresh the player list on the UI
        loadPlayerCards(club.getPlayers());
    }




}




