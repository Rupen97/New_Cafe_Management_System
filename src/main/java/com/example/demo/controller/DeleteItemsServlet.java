package com.example.demo.controller;

import com.example.demo.dao.ItemsDAO;
import com.example.demo.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "DeleteItemServlet", value = "/DeleteItemServlet")
public class DeleteItemsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthService.isAdmin(request)) {
            response.sendRedirect("LoginServlet");
            return;
        }

        HttpSession session = request.getSession();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean deleted = ItemsDAO.deleteItem(id);

            if (deleted) {
                session.setAttribute("success", "Item deleted successfully");
            } else {
                session.setAttribute("error", "Failed to delete item");
            }
        } catch (Exception ex) {
            session.setAttribute("error", "An error occurred: " + ex.getMessage());
        }

        response.sendRedirect("ListItemsServlet");
    }
}