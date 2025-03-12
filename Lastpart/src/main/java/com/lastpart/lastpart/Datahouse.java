package com.lastpart.lastpart;

import java.util.ArrayList;
import java.util.List;

public class Datahouse {
    private List<Player> playerList;
    private List<Country> countryList;
    private List<CLUB> clubList;

    public Datahouse() {
        playerList = new ArrayList<>();
        countryList = new ArrayList<>();
        clubList = new ArrayList<>();
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public List<CLUB> getClubList() {
        return clubList;
    }

    public void setClubList(List<CLUB> clubList) {
        this.clubList = clubList;
    }

    // returns null if not found
    public Player searchPlayerByName(String name) {
        for (int i = 0; i < this.playerList.size(); i++) {
            Player p = this.playerList.get(i);
            if (p.getName().equalsIgnoreCase(name)) return p;
        }
        return null;
    }

    public List<Player> searchPlayerByCountryAndClub(String countryName, String clubName) {
        List<Player> players = searchPlayerByCountry(countryName);
        if (clubName.equalsIgnoreCase("any")) return players;
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (!p.getClub().equalsIgnoreCase(clubName)) {
                players.remove(i);
                i--;
            }
        }
        return players;
    }

    public List<Player> searchPlayerByCountry(String countryName) {
        List<Player> players = new ArrayList<>();
        if (countryName.equalsIgnoreCase("any")) {
            players.addAll(this.playerList);
        } else {
            for (Country c : this.countryList
            ) {
                if (countryName.equalsIgnoreCase(c.getName())) {
                    players.addAll(c.getPlayerList());
                    break;
                }
            }
        }
        return players;
    }

    public List<Player> searchPlayerByPosition(String position) {
        List<Player> players = new ArrayList<>();
        for (Player p : this.playerList) {
            if (p.getPosition().equalsIgnoreCase(position)) players.add(p);
        }
        return players;
    }


    public int addPlayer(Player player) {
        Player p = searchPlayerByName(player.getName());
        if (p == null) {

            System.out.println("Attempting to add player: " + player.getName());


            boolean b = checkClubValidity(player.getClub());
            if (!b) {
                System.err.println("Club is full for player: " + player.getName());
                return -1;
            }


            b = checkDuplicateNumber(player.getClub(), player.getNumber());
            if (b) {
                System.err.println("Duplicate number in club for player: " + player.getName());
                return -2;
            }


            modifyClubName(player);
            modifyCountryName(player);

            playerList.add(player);
            updateCountryList(player);
            updateClubList(player);
            System.out.println("Player added successfully: " + player.getName());
            return 1;
        }

        System.err.println("Player already exists: " + player.getName());
        return 0;
    }


    public void addPlayer(List<Player> playerList) {
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            addPlayer(player);
        }
    }

    private void updateCountryList(Player player) {
        for (Country c : countryList
        ) {
            if (c.getName().equalsIgnoreCase(player.getCountry())) {
                c.addPlayer(player);
                return;
            }
        }

        Country c = new Country(player);
        countryList.add(c);
    }


    public boolean checkClubValidity(String club) {
        for (CLUB c : clubList
        ) {
            if (c.getName().equalsIgnoreCase(club) && c.getPlayerCount() == c.getMaxPlayerLimit()) {
                return false;
            }
        }
        return true;
    }

    private void updateClubList(Player player) {
        String club = player.getClub();
        for (int i = 0; i < this.clubList.size(); i++) {
            CLUB c = this.clubList.get(i);
            if (c.getName().equalsIgnoreCase(club)) {
                c.addPlayer(player);
                return;
            }
        }

        CLUB c = new CLUB(player);
        clubList.add(c);
    }


    public CLUB searchClub(String clubName) {
        for (int i = 0; i < this.clubList.size(); i++) {
            CLUB c = this.clubList.get(i);
            if (c.getName().equalsIgnoreCase(clubName)) return c;
        }
        return null;
    }

    private void modifyClubName(Player player) {
        for (CLUB club : clubList
        ) {
            if (club.getName().equalsIgnoreCase(player.getClub())) {
                player.setClub(club.getName());
                return;
            }
        }
    }

    private void modifyCountryName(Player player) {
        for (Country country : countryList
        ) {
            if (country.getName().equalsIgnoreCase(player.getCountry())) {
                player.setCountry(country.getName());
                return;
            }
        }
    }


    public boolean checkDuplicateNumber(String club, int number) {
        for (CLUB c : clubList
        ) {
            if (c.getName().equalsIgnoreCase(club)) {
                return c.checkNumber(number);
            }
        }
        return false;
    }

    /*public void showCountryCount() {
        for (Country c: countryList
        ) {
//            System.out.println(c);
        }
//        System.out.println();
    }*/

    public List<Player> searchPlayerByAge(int lo, int hi) {
        List<Player> playerList = new ArrayList<>();
        for (Player p : this.playerList) {
            int age = p.getAge();
            if (age >= lo && age <= hi) playerList.add(p);
        }
        return playerList;
    }

    public List<Player> searchPlayerByHeight(double lo, double hi) {
        List<Player> playerList = new ArrayList<>();
        for (Player p : this.playerList) {
            double height = p.getHeight();
            if (height >= lo && height <= hi) playerList.add(p);
        }
        return playerList;
    }

    public List<Player> searchPlayerBySalary(double lo, double hi) {
        List<Player> players = new ArrayList<>();
        for (Player p : this.playerList) {
            double salary = p.getSalary();
            if (salary >= lo && salary <= hi) players.add(p);
        }
        return players;
    }

    public void removePlayerFromClub(String playerName) {
        Player player = searchPlayerByName(playerName);
        CLUB club = searchClub(player.getClub());
        club.removePlayer(playerName);
    }
}



