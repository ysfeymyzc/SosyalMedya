package com.yourname.socialmedia.data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {
    private static Map<String, User> users = new ConcurrentHashMap<>();
    private static Map<UUID, String> loggedInUsers = new ConcurrentHashMap<>();
    private static List<Post> posts = Collections.synchronizedList(new ArrayList<>());
    private static final int MAX_POSTS = 30;
    
    public static void initialize() {
        // Initialize data structures
    }
    
    public static boolean registerUser(String username, String password, UUID playerId) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password, playerId));
        return true;
    }
    
    public static boolean authenticateUser(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }
    
    public static boolean isUserLoggedIn(String username) {
        return loggedInUsers.containsValue(username);
    }
    
    public static void loginUser(UUID playerId, String username) {
        loggedInUsers.put(playerId, username);
    }
    
    public static void logoutUser(UUID playerId) {
        loggedInUsers.remove(playerId);
    }
    
    public static String getLoggedInUser(UUID playerId) {
        return loggedInUsers.get(playerId);
    }
    
    public static User getUser(String username) {
        return users.get(username);
    }
    
    public static void addPost(Post post) {
        posts.add(0, post); // Add to beginning
        if (posts.size() > MAX_POSTS) {
            posts.remove(posts.size() - 1); // Remove oldest
        }
    }
    
    public static List<Post> getPosts() {
        return new ArrayList<>(posts);
    }
    
    public static boolean removePost(UUID postId, String username) {
        return posts.removeIf(post -> post.getId().equals(postId) && post.getUsername().equals(username));
    }
    
    public static void setVerificationCode(String username, String code) {
        User user = users.get(username);
        if (user != null) {
            user.setVerificationCode(code);
        }
    }
    
    public static String getVerificationCode(String username) {
        User user = users.get(username);
        return user != null ? user.getVerificationCode() : "";
    }
}