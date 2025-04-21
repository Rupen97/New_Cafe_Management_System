package com.example.demo.dao;

import com.example.demo.models.UserModel;
import com.example.demo.utils.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // SQL QUERIES
    public static final String UPDATE_USER_WITH_PASSWORD = "UPDATE users SET name=?, email=?, role=?, profile_picture=?, password=? WHERE id=?";

    public static final String UPDATE_USER_WITHOUT_PASSWORD = "UPDATE users SET name=?, email=?, role=?, profile_picture=? WHERE id=?";
    public static final String UPDATE_USER_WITHOUT_IMAGE = "UPDATE users SET name=?, email=?, role=? WHERE id=?";
    public static final String UPDATE_USER_WITH_PASSWORD_NO_IMAGE = "UPDATE users SET name=?, email=?, role=?, password=? WHERE id=?";

    //Updateing the users
    public static final String UPDATE_USER = "UPDATE users SET name=?, email=?, role=?, profile_picture=? WHERE id=?";

    //Deleting Servlet
    public static final String DELETE_USER = "DELETE FROM users WHERE id=?";

    //selecting all from users
    public static final String SELECT_ALL_USERS = "SELECT * FROM users";

    //Inserting new user to database
    public static final String INSERT_USER = "INSERT INTO users(name, email, password, role, profile_picture) VALUES (?,?,?,?, ?)";

    //sql query to find user by email and password
    public static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";

    //selecting user by id
    public static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";

    //method to register a new user
    public static int registerUser(UserModel user){
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);) {
            // Set parameters for the prepared statement
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword()); // In production, this should be hashed
            ps.setString(4, user.getRole().name());
            ps.setBytes(5, user.getImage());

            // Execute the insert statement
            int rows = ps.executeUpdate();

            // If insertion was successful, get the generated user ID
            if (rows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            // Log the exception details for debugging
            System.err.println("Error registering user: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return -1;
    }


    //method to login
    @Deprecated
    public static UserModel loginUser(UserModel user) {
        // This method is kept for backward compatibility
        // Authentication is now handled by retrieving the user by email and then verifying the password
        return getUserByEmail(user.getEmail());
    }

    //method to get user by email
    public static UserModel getUserByEmail(String email) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_EMAIL);) {
            // Set the email parameter
            ps.setString(1, email);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // If the user is found, create and return a UserModel object
            if (rs.next()) {
                UserModel userFromDB = new UserModel();
                userFromDB.setId(rs.getInt("id"));
                userFromDB.setName(rs.getString("name"));
                userFromDB.setEmail(rs.getString("email"));
                userFromDB.setPassword(rs.getString("password"));
                userFromDB.setRole(UserModel.Role.valueOf(rs.getString("role")));
                userFromDB.setImage(rs.getBytes("profile_picture"));
                return userFromDB;
            }
        } catch (SQLException e) {
            // Log the exception details for debugging
            System.err.println("Error retrieving user by email: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null; // Return null if user not found
    }


    //method to Getting user by id
    public static UserModel getUserById(int id) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_ID);) {
            // Set the user ID parameter
            ps.setInt(1, id);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // If the user is found, create and return a UserModel object
            if (rs.next()) {
                UserModel userFromDB = new UserModel();
                userFromDB.setId(rs.getInt("id"));
                userFromDB.setName(rs.getString("name"));
                userFromDB.setEmail(rs.getString("email"));
                userFromDB.setPassword(rs.getString("password"));
                userFromDB.setRole(UserModel.Role.valueOf(rs.getString("role")));
                userFromDB.setImage(rs.getBytes("profile_picture"));
                return userFromDB;
            }
        } catch (SQLException e) {
            // Log the exception details for debugging
            System.err.println("Error retrieving user by ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null; // Return null if user not found
    }

    //method to select all users
    public static List<UserModel> getAllUsers() {
        List<UserModel> users = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_USERS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UserModel user = new UserModel();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(UserModel.Role.valueOf(rs.getString("role")));
                user.setImage(rs.getBytes("profile_picture"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all users: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return users;
    }

    // Method to update user
    public static boolean updateUser(UserModel user, boolean changingPassword) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_USER)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getRole().name());
            ps.setBytes(4, user.getImage());
            ps.setInt(5, user.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Method to delete user
    public static boolean deleteUser(int userId) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_USER)) {

            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //method to edit profile
    public static boolean updateProfile(UserModel user, boolean updatePassword) {
        try (Connection connection = DBConnectionUtil.getConnection()) {
            String sql;

            if (user.getImage() != null && user.getImage().length > 0) {
                sql = updatePassword ? UPDATE_USER_WITH_PASSWORD : UPDATE_USER_WITHOUT_PASSWORD;
            } else {
                sql = updatePassword ? UPDATE_USER_WITH_PASSWORD_NO_IMAGE : UPDATE_USER_WITHOUT_IMAGE;
            }

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getRole().name());

                int paramIndex = 4;
                if (user.getImage() != null && user.getImage().length > 0) {
                    ps.setBytes(paramIndex++, user.getImage());
                }
                if (updatePassword) {
                    ps.setString(paramIndex++, user.getPassword());
                }

                ps.setInt(paramIndex, user.getId());

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //filter
}