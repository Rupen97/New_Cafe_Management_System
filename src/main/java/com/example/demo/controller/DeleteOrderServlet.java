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

@WebServlet("/delete-order")
public class DeleteOrderServlet extends HttpServlet {
    private OrdersDAO ordersDao;

    @Override
    public void init() {
        ordersDao = new OrdersDAO();
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
            OrderModel order = ordersDao.getOrderById(orderId);

            if (order == null) {
                request.getSession().setAttribute("error", "Order not found");
                response.sendRedirect(request.getContextPath() + "/admin-orders");
                return;
            }

            // Only allow deletion of COMPLETED or CANCELLED orders
            if (order.getStatus() != OrderModel.Status.COMPLETED &&
                    order.getStatus() != OrderModel.Status.CANCELLED) {
                request.getSession().setAttribute("error", "Only completed or cancelled orders can be deleted");
                response.sendRedirect(request.getContextPath() + "/admin-orders");
                return;
            }

            if (ordersDao.deleteOrder(orderId)) {
                request.getSession().setAttribute("success", "Order deleted successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to delete order");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid order ID");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error processing request: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin-orders");
    }
}