package com.yourname.socialmedia.data;

import java.util.UUID;

public class Post {
    private UUID id;
    private String username;
    private String content;
    private boolean isEncrypted;
    private String encryptionPassword;
    private long timestamp;
    
    public Post(String username, String content, boolean isEncrypted, String encryptionPassword) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.content = content;
        this.isEncrypted = isEncrypted;
        this.encryptionPassword = encryptionPassword;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getPreview() {
        if (isEncrypted) {
            return "<Şifreli gönderi>";
        }
        
        String[] words = content.split(" ");
        StringBuilder preview = new StringBuilder();
        for (int i = 0; i < Math.min(3, words.length); i++) {
            preview.append(words[i]).append(" ");
        }
        return preview.toString().trim();
    }
    
    // Getters and setters
    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getContent() { return content; }
    public boolean isEncrypted() { return isEncrypted; }
    public String getEncryptionPassword() { return encryptionPassword; }
    public long getTimestamp() { return timestamp; }
}