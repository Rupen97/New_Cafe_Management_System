package com.example.demo.controller;

import com.example.demo.dao.OrdersDAO;
import com.example.demo.models.OrderModel;
import com.example.demo.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin-orders")
public class AdminOrderServlet extends HttpServlet {
    private OrdersDAO ordersDao;

    @Override
    public void init() {
        ordersDao = new OrdersDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthService.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            String statusFilter = request.getParameter("status");
            List<OrderModel> orders;

            if (statusFilter != null && !statusFilter.isEmpty()) {
                orders = ordersDao.getOrdersByStatus(OrderModel.Status.valueOf(statusFilter));
            } else {
                orders = ordersDao.getAllOrders();
            }

            request.setAttribute("orders", orders);
            request.setAttribute("statusValues", OrderModel.Status.values());
            request.getRequestDispatcher("/WEB-INF/views/admin-orders.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Error loading orders: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthService.isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            OrderModel.Status newStatus = OrderModel.Status.valueOf(request.getParameter("status"));

            OrderModel order = ordersDao.getOrderById(orderId);

            if (order == null) {
                request.getSession().setAttribute("error", "Order not found");
                response.sendRedirect(request.getContextPath() + "/admin-orders");
                return;
            }

            if (!isValidStatusTransition(order.getStatus(), newStatus)) {
                request.getSession().setAttribute("error", "Invalid status transition");
                response.sendRedirect(request.getContextPath() + "/admin-orders");
                return;
            }

            if (ordersDao.updateOrderStatus(orderId, newStatus)) {
                request.getSession().setAttribute("success", "Order status updated successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to update order status");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid order ID");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", "Invalid status value");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error processing request: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin-orders");
    }

    private boolean isValidStatusTransition(OrderModel.Status currentStatus, OrderModel.Status newStatus) {
        // Define valid status transitions
        switch (currentStatus) {
            case PENDING:
                return newStatus == OrderModel.Status.ON_THE_WAY ||
                        newStatus == OrderModel.Status.CANCELLED;
            case ON_THE_WAY:
                return newStatus == OrderModel.Status.COMPLETED;
            case COMPLETED:
            case CANCELLED:
                return false; // These are final states
            default:
                return false;
        }
    }
}