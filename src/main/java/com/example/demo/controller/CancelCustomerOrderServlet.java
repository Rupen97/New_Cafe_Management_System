package com.example.demo.controller;

import com.example.demo.dao.OrdersDAO;
import com.example.demo.models.OrderModel;
import com.example.demo.models.OrderModel.Status;
import com.example.demo.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "CancelOrderServlet", value = "/cancel-order")
public class CancelCustomerOrderServlet extends HttpServlet {
    private OrdersDAO ordersDao;

    @Override
    public void init() {
        ordersDao = new OrdersDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthService.isUser(request)) {
            response.sendRedirect("login");
            return;
        }

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        int userId = AuthService.getUserId(request);

        OrderModel order = ordersDao.getOrderById(orderId);

        // Verify the order belongs to the user and is PENDING
        if (order == null || order.getUserId() != userId || order.getStatus() != Status.PENDING) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You can only cancel your own pending orders");
            return;
        }

        // Update order status to CANCELLED
        order.setStatus(Status.CANCELLED);
        ordersDao.updateOrder(order);

        response.sendRedirect(request.getContextPath() + "/customer-orders");
    }
}