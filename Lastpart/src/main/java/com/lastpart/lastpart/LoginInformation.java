package com.lastpart.lastpart;

import java.io.Serial;
import java.io.Serializable;

public class LoginInformation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private MessageHeader messageHeader;
    private String username;
    private String password;
    private String newPassword;

    public LoginInformation(MessageHeader messageHeader, String username, String password) {
        this.messageHeader = messageHeader;
        this.username = username;
        this.password = password;
    }

    public LoginInformation(MessageHeader messageHeader, String username, String password, String newPassword) {
        this.messageHeader = messageHeader;
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
    }

    public MessageHeader getMessageHeader() {

        return messageHeader;
    }

    public void setMessageHeader(MessageHeader messageHeader) {

        this.messageHeader = messageHeader;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getNewPassword() {

        return newPassword;
    }

    public void setNewPassword(String newPassword) {

        this.newPassword = newPassword;
    }
}
