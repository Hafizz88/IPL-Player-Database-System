package com.lastpart.lastpart;

import java.io.IOException;
import java.util.List;

public class Threading implements Runnable {
    private final Networking networking;
    private final Server server;
    private Thread thread;

    public Threading(Networking networking, Server server) {
        this.networking = networking;
        this.server = server;
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Read object from networking
                Object obj = networking.read();

                // Handle different object types
                if (obj instanceof Message msg) {
                    handleMessage(msg);
                } else if (obj instanceof LoginInformation loginInfo) {
                    handleLoginInformation(loginInfo);
                } else if (obj instanceof Buying buyInfo) {
                    handleBuying(buyInfo);
                } else if (obj instanceof SaleInformation saleInfo) {
                    handleSaleInformation(saleInfo);
                } else {
                    System.out.println("Unknown object type received: " + obj.getClass().getName());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during communication: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeNetworking();
        }
    }

    private void handleMessage(Message msg) throws IOException {
        switch (msg.getMessageHeader()) {
            case CLUB_INFO -> {
                // Use getter to access db
                networking.write(server.getDb().searchClub(msg.getMessage()));
            }
            case TRANSFER_WINDOW -> {
                List<Player> transferList = server.getTransferPlayerList();
                networking.write(transferList != null ? transferList : List.of());
            }
            case LOGOUT -> {
                boolean result = server.logoutClub(msg.getMessage());
                networking.write(result);
            }
            case CLUB_LIST -> {
                List<String> clubList = server.sendClubList();
                networking.write(clubList);
            }
            default -> System.out.println("Unknown message header: " + msg.getMessageHeader());
        }
    }

    private void handleLoginInformation(LoginInformation loginInfo) throws IOException {
        switch (loginInfo.getMessageHeader()) {
            case REGISTER -> {
                boolean result = server.registerClub(
                        loginInfo.getUsername(), loginInfo.getPassword(), networking);
                networking.write(result);
            }
            case LOGIN -> {
                boolean result = server.loginClub(
                        loginInfo.getUsername(), loginInfo.getPassword());
                networking.write(result);
            }
            case CHANGE_PASS -> {
                boolean result = server.changePassword(
                        loginInfo.getUsername(), loginInfo.getPassword(), loginInfo.getNewPassword());
                networking.write(result);
            }
            default -> System.out.println("Unknown login request: " + loginInfo.getMessageHeader());
        }
    }

    private void handleBuying(Buying buyInfo) throws IOException {
        if (buyInfo.getMessageHeader() == MessageHeader.BUY) {
            boolean result = server.sellPlayer(buyInfo.getPlayerName(), buyInfo.getClubName());
            networking.write(result);
        } else {
            System.out.println("Unknown buying request");
        }
    }

    private void handleSaleInformation(SaleInformation saleInfo) throws IOException {
        if (saleInfo.getMessageHeader() == MessageHeader.SELL) {
            boolean result = server.addToTransferWindow(saleInfo.getPlayerName(), saleInfo.getPlayerPrice());
            networking.write(result);
        } else {
            System.out.println("Unknown sale information request");
        }
    }

    private void closeNetworking() {
        try {
            networking.closeConnection();
            System.out.println("Disconnected successfully");
        } catch (IOException e) {
            System.err.println("Error closing networking connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
