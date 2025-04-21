package com.example.demo.controller;

import com.example.demo.dao.UserDAO;
import com.example.demo.services.AuthService;
import com.example.demo.models.UserModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DeleteUserServlet", value = "/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if admin is authenticated
        if (!AuthService.isAuthenticated(request) || !AuthService.isAdmin(request)) {
            response.sendRedirect("LoginServlet");
            return;
        }

        // Get user ID to delete
        String userIdStr = request.getParameter("id");
        if (userIdStr == null || userIdStr.isEmpty()) {
            response.sendRedirect("AdminDashboardServlet?error=No+user+ID+provided");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);

            // Prevent admin from deleting themselves
            UserModel currentUser = AuthService.getCurrentUser(request);
            if (currentUser.getId() == userId) {
                response.sendRedirect("AdminDashboardServlet?error=Cannot+delete+your+own+account");
                return;
            }

            // Delete user from database
            boolean success = UserDAO.deleteUser(userId);

            if (success) {
                response.sendRedirect("AdminDashboardServlet?success=User+deleted+successfully");
            } else {
                response.sendRedirect("AdminDashboardServlet?error=Failed+to+delete+user");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("AdminDashboardServlet?error=Invalid+user+ID");
        }
    }
}