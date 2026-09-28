package com.canteen.service;

import com.canteen.model.Role;
import com.canteen.model.User;
import com.canteen.storage.DataStore;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthService {
    private static AuthService instance;
    private final DataStore dataStore;
    private final Map<String, String> activeSessions = new ConcurrentHashMap<>();

    private AuthService() {
        this.dataStore = DataStore.getInstance();
    }

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public String login(String username, String password) {
        if (username == null || password == null) return null;
        User user = dataStore.findUserByUsername(username.trim());
        if (user != null && user.getPassword().equals(password)) {
            String token = UUID.randomUUID().toString();
            activeSessions.put(token, user.getId());
            return token;
        }
        return null;
    }

    public String register(String username, String password, String fullName, String rollNumber, String email, String phone, Role role) throws IllegalArgumentException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full Name is required");
        }
        if (role == Role.STUDENT && (rollNumber == null || rollNumber.trim().isEmpty())) {
            throw new IllegalArgumentException("College Roll Number is required for students");
        }
        if (dataStore.findUserByUsername(username.trim()) != null) {
            throw new IllegalArgumentException("Username '" + username.trim() + "' is already taken");
        }

        User newUser = new User(
                UUID.randomUUID().toString(),
                username.trim(),
                password,
                fullName.trim(),
                rollNumber != null ? rollNumber.trim().toUpperCase() : "",
                email != null ? email.trim() : "",
                phone != null ? phone.trim() : "",
                role != null ? role : Role.STUDENT
        );
        dataStore.addUser(newUser);

        String token = UUID.randomUUID().toString();
        activeSessions.put(token, newUser.getId());
        return token;
    }

    public User getUserBySession(String token) {
        if (token == null) return null;
        String userId = activeSessions.get(token);
        if (userId == null) return null;
        return dataStore.findUserById(userId);
    }

    public void logout(String token) {
        if (token != null) {
            activeSessions.remove(token);
        }
    }
}
