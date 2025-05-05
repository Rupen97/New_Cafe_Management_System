//package com.example.demo.controller;
//
//import com.example.demo.dao.OrderItemsDAO;
//import com.example.demo.dao.OrdersDAO;
//import com.example.demo.models.OrderItemModel;
//import com.example.demo.models.OrderModel;
//import com.example.demo.models.OrderModel.Status;
//import com.example.demo.services.AuthService;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@WebServlet(name = "EditOrderServlet", value = "/edit-order")
//public class EditCustomerOrderServlet extends HttpServlet {
//    private OrdersDAO ordersDao;
//    private OrderItemsDAO orderItemsDao;
//
//    @Override
//    public void init() {
//        ordersDao = new OrdersDAO();
//        orderItemsDao = new OrderItemsDAO();
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        if (!AuthService.isUser(request)) {
//            response.sendRedirect("login");
//            return;
//        }
//
//        int orderId = Integer.parseInt(request.getParameter("id"));
//        int userId = AuthService.getUserId(request);
//
//        OrderModel order = ordersDao.getOrderById(orderId);
//
//        // Verify the order belongs to the user and is PENDING
//        if (order == null || order.getUserId() != userId || order.getStatus() != Status.PENDING) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You can only edit your own pending orders");
//            return;
//        }
//
//        List<OrderItemModel> items = orderItemsDao.getItemsByOrder(orderId);
//
//        request.setAttribute("order", order);
//        request.setAttribute("items", items);
//        request.getRequestDispatcher("/WEB-INF/views/edit-customer-order.jsp").forward(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        if (!AuthService.isUser(request)) {
//            response.sendRedirect("login");
//            return;
//        }
//
//        int orderId = Integer.parseInt(request.getParameter("orderId"));
//        int userId = AuthService.getUserId(request);
//
//        OrderModel order = ordersDao.getOrderById(orderId);
//
//        // Verify the order belongs to the user and is PENDING
//        if (order == null || order.getUserId() != userId || order.getStatus() != Status.PENDING) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You can only edit your own pending orders");
//            return;
//        }
//
//        try {
//            // Get all parameters
//            String[] itemIds = request.getParameterValues("itemId");
//            String[] quantities = request.getParameterValues("quantity");
//
//            List<OrderItemModel> itemsToUpdate = new ArrayList<>();
//            List<Integer> keptItemIds = new ArrayList<>();
//
//            // Process updated items
//            if (itemIds != null && quantities != null && itemIds.length == quantities.length) {
//                for (int i = 0; i < itemIds.length; i++) {
//                    int itemId = Integer.parseInt(itemIds[i]);
//                    int quantity = Integer.parseInt(quantities[i]);
//
//                    OrderItemModel item = orderItemsDao.getOrderItemById(itemId);
//                    if (item != null && item.getOrderId() == orderId) {
//                        item.setQuantity(quantity);
//                        itemsToUpdate.add(item);
//                        keptItemIds.add(itemId);
//                    }
//                }
//            }
//
//            // Update items in database
//            boolean success = true;
//            for (OrderItemModel item : itemsToUpdate) {
//                success = success && orderItemsDao.updateOrderItem(item);
//            }
//
//            // Delete items that were removed
//            List<OrderItemModel> currentItems = orderItemsDao.getItemsByOrder(orderId);
//            for (OrderItemModel item : currentItems) {
//                if (!keptItemIds.contains(item.getId())) {
//                    success = success && orderItemsDao.deleteOrderItem(item.getId());
//                }
//            }
//
//            // Recalculate order total
//            if (success) {
//                List<OrderItemModel> updatedItems = orderItemsDao.getItemsByOrder(orderId);
//                double newTotal = 0;
//                int newQuantity = 0;
//
//                for (OrderItemModel item : updatedItems) {
//                    newTotal += item.getPrice() * item.getQuantity();
//                    newQuantity += item.getQuantity();
//                }
//
//                order.setTotalAmount(newTotal);
//                order.setQuantity(newQuantity);
//                ordersDao.updateOrder(order);
//
//                response.sendRedirect(request.getContextPath() + "/customer-orders?success=Order updated successfully");
//            } else {
//                response.sendRedirect(request.getContextPath() + "/edit-order?id=" + orderId + "&error=Failed to update order");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendRedirect(request.getContextPath() + "/edit-order?id=" + orderId + "&error=Error processing your request");
//        }
//    }
//}