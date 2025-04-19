package com.example.demo.services;

import com.example.demo.dao.UserDAO;
import com.example.demo.models.UserModel;
import com.example.demo.utils.PasswordHashUtil;

/**
 * AuthService Class
 *
 * This service class provides authentication and user management functionality.
 * It acts as an intermediary between the controllers and the data access layer,
 * encapsulating the business logic related to user authentication and registration.
 */
public class AuthService {

    // Current authenticated user (in-memory session)
    private static UserModel currentUser = null;
    private static long sessionExpiryTime = 0;

    /**
     * Register a new user
     *
     * @param name The user's full name
     * @param email The user's email address
     * @param password The user's password
     * @param role The user's role ("admin" or "user")
     * @param image The user's profile picture as a byte array
     * @return The generated user ID if registration is successful, -1 otherwise
     */
    public static int register(String name, String email, String password, String role, byte[] image) {
        // Hash the password
        password = PasswordHashUtil.hashPassword(password);

        // Create a new UserModel object
        UserModel user = new UserModel();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(UserModel.Role.valueOf(role));
        user.setImage(image);

        // Register the user and return the generated ID
        return UserDAO.registerUser(user);
    }

    /**
     * Authenticate a user and create a session
     *
     * @param email The user's email address
     * @param password The user's password
     * @param rememberMe Whether to extend the session duration
     * @return The complete UserModel if authentication is successful, null otherwise
     */
    public static UserModel login(String email, String password, boolean rememberMe) {
        // Find user by email
        UserModel userRequest = new UserModel();
        userRequest.setEmail(email);

        UserModel authenticatedUser = UserDAO.loginUser(userRequest);

        if (authenticatedUser == null) {
            System.out.println("No user found with email: " + email);
            return null;
        }

        // Verify password
        boolean isValidUser = PasswordHashUtil.checkPassword(password, authenticatedUser.getPassword());

        if (!isValidUser) {
            System.out.println("Invalid password for user: " + email);
            return null;
        }

        // Create session
        createSession(authenticatedUser, rememberMe);
        return authenticatedUser;
    }

    /**
     * Create a new session for the authenticated user
     *
     * @param user The authenticated user
     * @param rememberMe Whether to extend the session duration
     */
    private static void createSession(UserModel user, boolean rememberMe) {
        currentUser = user;
        // Set session expiry (30 minutes standard, 30 days if rememberMe)
        sessionExpiryTime = System.currentTimeMillis() +
                (rememberMe ? 30L * 24 * 60 * 60 * 1000 : 30 * 60 * 1000);
    }

    /**
     * Retrieve a user by ID
     *
     * @param id The user ID to look up
     * @return The complete UserModel if found, null otherwise
     */
    public static UserModel getUserById(int id) {
        return UserDAO.getUserById(id);
    }

    /**
     * Validate if a session exists and is not expired
     *
     * @return true if session is valid, false otherwise
     */
    public static boolean validateSession() {
        if (currentUser == null) {
            return false;
        }
        return System.currentTimeMillis() < sessionExpiryTime;
    }

    /**
     * Check if the current user is an admin
     *
     * @return true if user is an admin, false otherwise
     */
    public static boolean isAdmin() {
        if (!validateSession()) {
            return false;
        }
        return currentUser.getRole() == UserModel.Role.admin;
    }

    /**
     * Get the current authenticated user
     *
     * @return The current UserModel if session is valid, null otherwise
     */
    public static UserModel getCurrentUser() {
        if (!validateSession()) {
            return null;
        }
        return currentUser;
    }

    /**
     * Handle user logout by clearing the current session
     */
    public static void logout() {
        currentUser = null;
        sessionExpiryTime = 0;
    }

    /**
     * Refresh the session expiry time
     *
     * @param rememberMe Whether to extend the session duration
     */
    public static void refreshSession(boolean rememberMe) {
        if (validateSession()) {
            createSession(currentUser, rememberMe);
        }
    }
}