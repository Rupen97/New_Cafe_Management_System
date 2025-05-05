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
//import java.util.List;
//
//@WebServlet(name = "RemoveOrderItemServlet", value = "/remove-order-item")
//public class RemoveOrderItemServlet extends HttpServlet {
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
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        if (!AuthService.isUser(request)) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        int itemId = Integer.parseInt(request.getParameter("itemId"));
//        int orderId = Integer.parseInt(request.getParameter("orderId"));
//        int userId = AuthService.getUserId(request);
//
//        OrderModel order = ordersDao.getOrderById(orderId);
//
//        // Verify the order belongs to the user and is PENDING
//        if (order == null || order.getUserId() != userId || order.getStatus() != Status.PENDING) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN);
//            return;
//        }
//
//        // Remove the item
//        boolean success = orderItemsDao.deleteOrderItem(itemId);
//
//        if (success) {
//            // Recalculate order total and quantity
//            List<OrderItemModel> items = orderItemsDao.getItemsByOrder(orderId);
//            double newTotal = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
//            int newQuantity = items.stream().mapToInt(OrderItemModel::getQuantity).sum();
//
//            order.setTotalAmount(newTotal);
//            order.setQuantity(newQuantity);
//            ordersDao.updateOrder(order);
//
//            response.setStatus(HttpServletResponse.SC_OK);
//        } else {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }
//    }
//}