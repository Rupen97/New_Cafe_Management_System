package com.example.demo.models;

public class OrderItemModel {
    private int id;
    private int orderId;
    private int itemId;
    private String itemName;
    private double itemPrice;
    private int quantity;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getName() { return itemName; }
    public void setName(String itemName) { this.itemName = itemName; }

    public double getPrice() { return itemPrice; }
    public void setPrice(double itemPrice) { this.itemPrice = itemPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
