package com.lastpart.lastpart;

public class ClientInformation {
    private String clubName;
    private String password;
    private Networking networking;
    private boolean isLoggedIn;

    public Networking getNetworkUtil() {

        return networking;
    }

    public void setNetworkUtil(Networking networking) {

        this.networking = networking;
    }

    public boolean isLoggedIn() {

        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {

        isLoggedIn = loggedIn;
    }

    public String getClubName() {

        return clubName;
    }

    public void setClubName(String clubName) {

        this.clubName = clubName;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }
}
