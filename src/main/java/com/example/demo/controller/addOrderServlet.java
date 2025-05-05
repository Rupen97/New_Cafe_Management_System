package com.example.demo.controller;

import com.example.demo.dao.ItemsDAO;
import com.example.demo.dao.OrderItemsDAO;
import com.example.demo.dao.OrdersDAO;
import com.example.demo.models.OrderItemModel;
import com.example.demo.models.itemsModel;
import com.example.demo.models.OrderModel;
import com.example.demo.services.AuthService;
import com.example.demo.utils.DBConnectionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

@WebServlet(name = "AddOrderServlet", value = "/AddOrderServlet")
public class addOrderServlet extends HttpServlet {
    private OrdersDAO ordersDao;
    private ItemsDAO itemsDao;
    private OrderItemsDAO orderItemsDao;

    @Override
    public void init() {
        ordersDao = new OrdersDAO();
        itemsDao = new ItemsDAO();
        orderItemsDao = new OrderItemsDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthService.isUser(request)) {
            response.sendRedirect("LoginServlet");
            return;
        }

        String action = request.getParameter("action");

        if ("view".equalsIgnoreCase(action)) {
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            itemsModel item = itemsDao.getItemById(itemId);

            if (item != null && "AVAILABLE".equals(item.getStatus().toString())) {
                request.setAttribute("item", item);
                request.getRequestDispatcher("/WEB-INF/views/add-order.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("error", "Item is not available.");
                response.sendRedirect("CustomerItemsServlet");
            }
        } else if ("add".equalsIgnoreCase(action)) {
            try {
                int itemId = Integer.parseInt(request.getParameter("itemId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                itemsModel item = itemsDao.getItemById(itemId);

                if (item != null && "AVAILABLE".equals(item.getStatus().toString())) {
                    // Create the order
                    OrderModel order = new OrderModel();
                    order.setUserId(AuthService.getUserId(request));
                    order.setStatus(OrderModel.Status.PENDING);
                    order.setQuantity(quantity);
                    order.setTotalAmount(item.getPrice() * quantity);
                    order.setOrderDate(new Timestamp(System.currentTimeMillis()));

                    // Save the order and get the generated ID
                    int orderId = ordersDao.createOrder(order);

                    if (orderId > 0) {
                        // Create the order item
                        OrderItemModel orderItem = new OrderItemModel();
                        orderItem.setOrderId(orderId);
                        orderItem.setItemId(itemId);
                        orderItem.setName(item.getName());
                        orderItem.setPrice(item.getPrice());
                        orderItem.setQuantity(quantity);

                        // Save the order item
                        boolean itemSaved = orderItemsDao.createOrderItem(orderItem);

                        if (itemSaved) {
                            request.getSession().setAttribute("success", "Order placed successfully!");
                        } else {
                            // Rollback the order if item couldn't be saved
                            ordersDao.deleteOrder(orderId);
                            request.getSession().setAttribute("error", "Failed to save order items.");
                        }
                    } else {
                        request.getSession().setAttribute("error", "Failed to place order.");
                    }
                } else {
                    request.getSession().setAttribute("error", "Item is not available.");
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "Invalid item ID or quantity.");
            }
            response.sendRedirect("CustomerItemsServlet");
        }
    }
}