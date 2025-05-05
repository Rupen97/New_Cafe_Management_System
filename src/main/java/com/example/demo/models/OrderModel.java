// ordersModel.java
package com.example.demo.models;

import java.sql.Timestamp;

public class OrderModel {
    private int id;
    private int userId;
    private Timestamp orderDate;
    private Status status;
    private double totalAmount;
    private int quantity;

    private String customerName;

    // Add getter and setter
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public enum Status {
        PENDING, ON_THE_WAY, CANCELLED, COMPLETED,
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}