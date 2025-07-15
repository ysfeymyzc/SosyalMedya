package com.yourname.socialmedia.data;

import java.util.UUID;

public class User {
    private String username;
    private String password;
    private UUID playerId;
    private String verificationCode;
    
    public User(String username, String password, UUID playerId) {
        this.username = username;
        this.password = password;
        this.playerId = playerId;
        this.verificationCode = "";
    }
    
    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public UUID getPlayerId() { return playerId; }
    public void setPlayerId(UUID playerId) { this.playerId = playerId; }
    
    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
}