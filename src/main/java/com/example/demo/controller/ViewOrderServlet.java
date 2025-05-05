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

@WebServlet(name = "ViewOrderServlet", value = "/view-order")
public class ViewOrderServlet extends HttpServlet {
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

        // 1. Authentication Check - using request context path to avoid loops
        if (!AuthService.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // 2. Get Order ID from request with validation
            String orderIdParam = request.getParameter("id");
            if (orderIdParam == null || !orderIdParam.matches("\\d+")) {
                redirectToOrderList(request, response);
                return;
            }

            int orderId = Integer.parseInt(orderIdParam);

            // 3. Retrieve Order
            OrderModel order = ordersDao.getOrderById(orderId);

            // 4. Authorization Check
            if (order == null) {
                redirectToOrderList(request, response, "Order not found");
                return;
            }

            if (!hasAccessToOrder(request, order)) {
                redirectToOrderList(request, response, "Access denied");
                return;
            }

            // 5. Get Order Items
            List<OrderItemModel> items = orderItemsDao.getItemsByOrder(orderId);

            // 6. Set Attributes and Forward
            request.setAttribute("order", order);
            request.setAttribute("items", items);
            request.setAttribute("isAdmin", AuthService.isAdmin(request));

            request.getRequestDispatcher("/WEB-INF/views/orders/view.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            redirectToOrderList(request, response, "Error loading order details");
        }
    }

    private boolean hasAccessToOrder(HttpServletRequest request, OrderModel order) {
        return AuthService.isAdmin(request) || order.getUserId() == AuthService.getUserId(request);
    }

    private void redirectToOrderList(HttpServletRequest request,
                                     HttpServletResponse response,
                                     String errorMessage) throws IOException {
        if (errorMessage != null) {
            request.getSession().setAttribute("error", errorMessage);
        }

        String redirectPath = AuthService.isAdmin(request)
                ? request.getContextPath() + "/admin-orders"
                : request.getContextPath() + "/customer-orders"; // Point to servlet

        response.sendRedirect(redirectPath);
    }

    // Overloaded method without error message
    private void redirectToOrderList(HttpServletRequest request,
                                     HttpServletResponse response) throws IOException {
        redirectToOrderList(request, response, null);
    }
}