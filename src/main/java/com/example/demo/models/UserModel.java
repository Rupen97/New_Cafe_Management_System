package com.example.demo.models;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;

public class UserModel implements Serializable {

    public enum Role { admin, user }

    private int id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private byte[] image;

    public UserModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // Check if the password is already hashed (starts with $2a$)
        if (password != null && !password.startsWith("$2a$")) {
            // Hash the password with BCrypt
            this.password = BCrypt.hashpw(password, BCrypt.gensalt(12));
        } else {
            // Password is already hashed or null
            this.password = password;
        }
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean verifyPassword(String plainTextPassword) {
        if (this.password == null || plainTextPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainTextPassword, this.password);
    }

}
