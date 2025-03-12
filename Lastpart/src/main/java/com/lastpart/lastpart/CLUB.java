package com.lastpart.lastpart;

import java.io.Serializable;
import java.util.*;

public class CLUB implements Serializable {
    private String name;
    private List<Player> players;
    private double budget;
    private String imgSource;

    private final int MAX_PLAYER_LIMIT = 150;

    // Default constructor
    public CLUB() {
        players = new ArrayList<>();
    }

    // Constructor with a single player
    public CLUB(Player player) {
        players = new ArrayList<>();
        setName(player.getClub());
        addPlayer(player);
    }

    // Getter for club name
    public String getName() {
        return name;
    }

    // Setter for club name, updates imgSource
    public void setName(String name) {
        this.name = name;
        // Set imgSource correctly by replacing spaces with underscores in the club name
        setImgSource(name.replace(' ', '_') + ".png");  // Correct path without leading slash
    }

    // Getter for the number of players in the club
    public int getPlayerCount() {
        return players.size();
    }

    // Getter for list of players
    public List<Player> getPlayers() {
        return players;
    }

    // Setter for the players list
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    // Getter for club budget
    public double getBudget() {
        return budget;
    }

    // Setter for club budget
    public void setBudget(double budget) {
        this.budget = budget;
    }

    // Getter for image source (logo image path)
    public String getImgSource() {
        return imgSource;
    }

    // Setter for image source (logo image path)
    public void setImgSource(String imgSource) {
        // Set image path relative to resources folder without the leading slash
        this.imgSource = "com/lastpart/images/logo/" + imgSource;
    }

    // Get list of unique countries of the players in the club
    public List<String> getCountryList() {
        Set<String> countrySet = new HashSet<>();  // Using a Set to store unique countries

        // Loop through all players in the club and add their countries to the set
        for (Player player : this.players) {
            countrySet.add(player.getCountry());  // Assuming 'getCountry()' returns the country of the player
        }

        // Convert the Set to a List and return it
        return new ArrayList<>(countrySet);
    }

    // Get list of unique positions of the players in the club
    public List<String> getPositionList() {
        Set<String> positionSet = new HashSet<>();  // Using a Set to store unique positions

        // Loop through all players in the club and add their positions to the set
        for (Player player : this.players) {
            positionSet.add(player.getPosition());  // Assuming 'getPosition()' returns the position of the player
        }

        // Convert the Set to a List and return it
        return new ArrayList<>(positionSet);
    }


    // Getter for the maximum number of players the club can have
    public int getMaxPlayerLimit() {
        return MAX_PLAYER_LIMIT;
    }

    // Add a player to the club
    public void addPlayer(Player player) {
        players.add(player);
    }

    // Get list of players with the maximum salary
    public List<Player> getMaxSalaryPlayers() {
        List<Player> playerList = new ArrayList<>();
        double salary = players.get(0).getSalary();

        // Find maximum salary value
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i).getSalary() > salary) {
                salary = players.get(i).getSalary();
            }
        }

        // Find players with the maximum salary
        double eps = 0.000001;  // precision range
        for (int i = 0; i < this.players.size(); i++) {
            Player player = this.players.get(i);
            if (Math.abs(salary - player.getSalary()) < eps) {
                playerList.add(player);
            }
        }
        return playerList;
    }

    // Get list of players with the maximum age
    public List<Player> getMaxAgePlayers() {
        List<Player> playerList = new ArrayList<>();
        int age = players.get(0).getAge();

        // Find maximum age
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i).getAge() > age) {
                age = players.get(i).getAge();
            }
        }

        // Find players with maximum age
        for (int i = 0; i < this.players.size(); i++) {
            Player player = this.players.get(i);
            if (player.getAge() == age) {
                playerList.add(player);
            }
        }
        return playerList;
    }

    // Get list of players with the maximum height
    public List<Player> getMaxHeightPlayers() {
        List<Player> playerList = new ArrayList<>();
        double height = players.get(0).getHeight();

        // Find maximum height
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i).getHeight() > height) {
                height = players.get(i).getHeight();
            }
        }

        // Find players with maximum height
        double eps = 0.000001;
        for (int i = 0; i < this.players.size(); i++) {
            Player player = this.players.get(i);
            if (Math.abs(height - player.getHeight()) < eps) {
                playerList.add(player);
            }
        }
        return playerList;
    }

    // Get the total yearly salary of all players in the club
    public double getTotalYearlySalary() {
        double sum = 0;
        for (Player player : players) {
            sum += player.getSalary();
        }
        sum *= 52;  // 52 weeks in a year
        return sum;
    }

    // Check if a player number already exists in the club
    public boolean checkNumber(int number) {
        for (Player player : players) {
            if (player.getNumber() == number) return true;
        }
        return false;
    }

    // Remove a player from the club by name
    public void removePlayer(String playerName) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equalsIgnoreCase(playerName)) {
                players.remove(i);
                return;
            }
        }
    }

}
