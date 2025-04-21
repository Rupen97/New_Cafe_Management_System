package com.example.demo.controller;

import com.example.demo.dao.UserDAO;
import com.example.demo.models.UserModel;
import com.example.demo.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Base64;

@WebServlet(name = "UpdateUserServlet", value = "/UpdateUserServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)

public class UpdateUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Check if admin is authenticated
        if (!AuthService.isAuthenticated(request) || !AuthService.isAdmin(request)) {
            response.sendRedirect("LoginServlet");
            return;
        }

        // Get user ID to edit
        String userIdStr = request.getParameter("id");
        if (userIdStr == null || userIdStr.isEmpty()) {
            response.sendRedirect("AdminDashboardServlet");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            UserModel userToEdit = UserDAO.getUserById(userId);

            if (userToEdit != null) {
                // Convert image to Base64 if exists
                if (userToEdit.getImage() != null && userToEdit.getImage().length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(userToEdit.getImage());
                    request.setAttribute("base64Image", base64Image);
                }

                request.setAttribute("userToEdit", userToEdit);
                request.getRequestDispatcher("/WEB-INF/views/edit-user.jsp").forward(request, response);
            } else {
                response.sendRedirect("AdminDashboardServlet?error=User+not+found");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("AdminDashboardServlet?error=Invalid+user+ID");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if admin is authenticated
        if (!AuthService.isAuthenticated(request) || !AuthService.isAdmin(request)) {
            response.sendRedirect("LoginServlet");
            return;
        }

        // Parse the multipart request
        Part filePart = request.getPart("image");

        // Get form parameters
        String userIdStr = request.getParameter("id");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String role = request.getParameter("role");

        try {
            int userId = Integer.parseInt(userIdStr);
            UserModel userToUpdate = UserDAO.getUserById(userId);

            if (userToUpdate != null) {
                // Update user details
                userToUpdate.setName(name);
                userToUpdate.setEmail(email);
                userToUpdate.setRole(UserModel.Role.valueOf(role));

                // TODO: Add logic to update password if needed
                // TODO: Add logic to update profile picture if needed

                // Update user in database
                boolean changingPassword = false;
                boolean success = UserDAO.updateProfile(userToUpdate, changingPassword);

                if (success) {
                    response.sendRedirect("AdminDashboardServlet?success=User+updated+successfully");
                } else {
                    response.sendRedirect("AdminDashboardServlet?error=Failed+to+update+user");
                }
            } else {
                response.sendRedirect("AdminDashboardServlet?error=User+not+found");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("AdminDashboardServlet?error=Invalid+user+ID");
        }
    }
}