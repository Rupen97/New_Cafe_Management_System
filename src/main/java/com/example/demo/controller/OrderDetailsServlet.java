package com.example.demo.controller;

import com.example.demo.dao.OrderItemsDAO;
import com.example.demo.dao.OrdersDAO;
import com.example.demo.models.OrderItemModel;
import com.example.demo.models.OrderModel;
import com.example.demo.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderDetailsServlet", value = "/order-details")
public class OrderDetailsServlet extends HttpServlet {
    private OrdersDAO ordersDao;
    private OrderItemsDAO orderItemsDao;

    @Override
    public void init() {
        ordersDao = new OrdersDAO();
        orderItemsDao = new OrderItemsDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthService.isUser(request)) {
            response.sendRedirect("login");
            return;
        }

        int orderId = Integer.parseInt(request.getParameter("id"));
        int userId = AuthService.getUserId(request);

        OrderModel order = ordersDao.getOrderById(orderId);

        // Verify the order belongs to the user
        if (order == null || order.getUserId() != userId) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You can only view your own orders");
            return;
        }

        List<OrderItemModel> items = orderItemsDao.getItemsByOrder(orderId);

        request.setAttribute("order", order);
        request.setAttribute("items", items);
        request.getRequestDispatcher("/WEB-INF/views/order-details.jsp").forward(request, response);
    }
}