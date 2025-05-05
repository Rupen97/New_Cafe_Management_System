package com.example.demo.dao;

import com.example.demo.models.OrderItemModel;
import com.example.demo.utils.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemsDAO {

    // SQL Queries
    private static final String INSERT_ITEM = "INSERT INTO order_items (order_id, item_id, item_name, item_price, quantity) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ORDER = "SELECT * FROM order_items WHERE order_id = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM order_items WHERE id = ?";
    private static final String UPDATE_ITEM = "UPDATE order_items SET quantity = ? WHERE id = ?";
    private static final String DELETE_ITEM = "DELETE FROM order_items WHERE id = ?";
    private static final String DELETE_BY_ORDER = "DELETE FROM order_items WHERE order_id = ?";

    // Create a new order item
    public static boolean createOrderItem(OrderItemModel item) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_ITEM)) {

            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getItemId());
            ps.setString(3, item.getName());
            ps.setDouble(4, item.getPrice());
            ps.setInt(5, item.getQuantity());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating order item: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Get all items for an order
    public static List<OrderItemModel> getItemsByOrder(int orderId) {
        List<OrderItemModel> items = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ORDER)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                items.add(mapResultSetToOrderItem(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving order items: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return items;
    }

    // Get single order item by ID
    public static OrderItemModel getOrderItemById(int id) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToOrderItem(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving order item: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    // Update order item quantity
    public static boolean updateOrderItem(OrderItemModel item) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_ITEM)) {

            ps.setInt(1, item.getQuantity());
            ps.setInt(2, item.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating order item: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Delete an order item
    public static boolean deleteOrderItem(int id) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_ITEM)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting order item: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Delete all items for an order
    public static boolean deleteItemsByOrder(int orderId) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_BY_ORDER)) {

            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting order items: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Helper method to map ResultSet to OrderItemModel
    private static OrderItemModel mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItemModel item = new OrderItemModel();
        item.setId(rs.getInt("id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setItemId(rs.getInt("item_id"));
        item.setName(rs.getString("item_name"));
        item.setPrice(rs.getDouble("item_price"));
        item.setQuantity(rs.getInt("quantity"));
        return item;
    }
}