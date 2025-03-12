package com.lastpart.lastpart;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    private volatile Datahouse db;
    private FileOperation fileOperations;
    private volatile List<Player> transferPlayerList;
    private volatile HashMap<String, ClientInformation> clientMap;
    public Datahouse getDb() {
        return db;
    }

    public static void main(String[] args) {
        int port = 45045;
        new Server(port);
    }

    public Server(int port) {
        clientMap = new HashMap<>();
        transferPlayerList = new ArrayList<>();

        try {
            // Load database and initialize
            loadDatahouse();

            // Start server
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server started on port " + port);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected from " + clientSocket.getInetAddress());
                    serve(clientSocket);
                }
            }
        } catch (IOException e) {
            System.err.println("Error in server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    synchronized public List<Player> getTransferPlayerList() {
        return transferPlayerList;
    }

    private void loadDatahouse() throws IOException {
        db = new Datahouse();
        fileOperations = new FileOperation();

        // Read players from file
        System.out.println("Loading players from file...");
        fileOperations.readFromFile(db);
        transferPlayerList = new ArrayList<>();
        System.out.println("Data loaded successfully.");
    }

    private void serve(Socket socket) {
        try {
            Networking networking = new Networking(socket);
            System.out.println("Networking object for server created.");
            Threading threading = new Threading(networking, this);
            threading.start();
        } catch (IOException e) {
            System.err.println("Error serving client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    synchronized public boolean sellPlayer(String playerName, String newClubName) {
        try {
            Player player = db.searchPlayerByName(playerName);
            if (player.isInTransferList()) {
                transferPlayerList.remove(player);
                player.setInTransferList(false);
                player.setClub(newClubName);

                System.out.println("Player sold: " + player.getName() + " to " + player.getClub());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    synchronized public boolean addToTransferWindow(String playerName, double playerPrice) {
        try {
            db.removePlayerFromClub(playerName);
            Player player = db.searchPlayerByName(playerName);
            player.setPrice(playerPrice);
            player.setInTransferList(true);
            transferPlayerList.add(player);

            System.out.println("Player added to transfer window: " + playerName + " - Price: " + player.getPrice());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    synchronized public boolean registerClub(String clubName, String password, Networking networking) {
        if (clientMap.containsKey(clubName.toLowerCase())) {
            System.out.println("Registration failed: Club already exists.");
            return false;
        }
        ClientInformation clientInfo = new ClientInformation();
        clientInfo.setClubName(clubName);
        clientInfo.setPassword(password);
        clientInfo.setNetworkUtil(networking);
        clientInfo.setLoggedIn(false);
        clientMap.put(clubName.toLowerCase(), clientInfo);
        System.out.println("Registration successful for club: " + clubName);
        return true;
    }

    synchronized public boolean loginClub(String username, String password) {
        if (clientMap.containsKey(username.toLowerCase())
                && clientMap.get(username.toLowerCase()).getPassword().equals(password)
                && !clientMap.get(username.toLowerCase()).isLoggedIn()) {
            clientMap.get(username.toLowerCase()).setLoggedIn(true);
            System.out.println("Login successful for club: " + username);
            return true;
        }
        System.out.println("Login unsuccessful for club: " + username);
        return false;
    }

    synchronized public boolean changePassword(String username, String oldPassword, String newPassword) {
        if (clientMap.containsKey(username.toLowerCase())
                && clientMap.get(username.toLowerCase()).getPassword().equals(oldPassword)
                && clientMap.get(username.toLowerCase()).isLoggedIn()) {
            clientMap.get(username.toLowerCase()).setPassword(newPassword);
            return true;
        }
        return false;
    }

    synchronized public boolean logoutClub(String username) {
        if (clientMap.containsKey(username.toLowerCase()) && clientMap.get(username.toLowerCase()).isLoggedIn()) {
            clientMap.get(username.toLowerCase()).setLoggedIn(false);
            return true;
        }
        return false;
    }

    synchronized public List<String> sendClubList() {
        List<String> clubList = new ArrayList<>();
        db.getClubList().forEach(e -> clubList.add(e.getName()));
        return clubList;
    }
}
