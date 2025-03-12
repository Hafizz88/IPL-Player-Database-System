package com.lastpart.lastpart;

import java.io.Serializable;

public class SaleInformation implements Serializable {
    private MessageHeader messageHeader;
    private String playerName;
    private double playerPrice;

    public SaleInformation(MessageHeader messageHeader, String playerName, double playerPrice) {
        this.messageHeader = messageHeader;
        this.playerName = playerName;
        this.playerPrice = playerPrice;
    }

    public MessageHeader getMessageHeader() {

        return messageHeader;
    }

    public void setMessageHeader(MessageHeader messageHeader) {

        this.messageHeader = messageHeader;
    }

    public String getPlayerName() {

        return playerName;
    }

    public void setPlayerName(String playerName) {

        this.playerName = playerName;
    }

    public double getPlayerPrice() {

        return playerPrice;
    }

    public void setPlayerPrice(double playerPrice) {

        this.playerPrice = playerPrice;
    }
}
