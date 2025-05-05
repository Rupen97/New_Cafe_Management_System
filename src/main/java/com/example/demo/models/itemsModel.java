package com.example.demo.models;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;

public class itemsModel implements Serializable {

    public enum Category { DRINKS, DESSERT, BAKERY, OTHER }
    public enum Status { AVAILABLE, UNAVAILABLE }

    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private Category category;
    private Status status;
    private byte[] image;

    public itemsModel() {
    }

    // Getters and Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    // Business logic methods
    public boolean isAvailable() {
        return status == Status.AVAILABLE && quantity > 0;
    }

    public void updateStatusBasedOnQuantity() {
        if (quantity <= 0) {
            status = Status.UNAVAILABLE;
        } else if (status == Status.UNAVAILABLE) {
            status = Status.AVAILABLE;
        }
    }

}
