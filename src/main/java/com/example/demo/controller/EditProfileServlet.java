package com.example.demo.controller;

import com.example.demo.dao.UserDAO;
import com.example.demo.models.UserModel;
import com.example.demo.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.Base64;

@WebServlet(name = "EditProfileServlet", value = "/EditProfileServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5,   // 5MB
        maxRequestSize = 1024 * 1024 * 10 // 10MB
)
public class EditProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthService.isAuthenticated(request)) {
            response.sendRedirect("LoginServlet");
            return;
        }

        UserModel user = AuthService.getCurrentUser(request);
        request.setAttribute("user", user);

        // Convert image to Base64 for display
        if (user.getImage() != null && user.getImage().length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(user.getImage());
            request.setAttribute("base64Image", base64Image);
        }

        request.getRequestDispatcher("/WEB-INF/views/edit-profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthService.isAuthenticated(request)) {
            response.sendRedirect("LoginServlet");
            return;
        }

        HttpSession session = request.getSession();
        UserModel currentUser = (UserModel) session.getAttribute("user");
        UserModel updatedUser = new UserModel();

        // Get form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Handle profile picture upload
        Part filePart = request.getPart("profilePicture");
        byte[] imageBytes = null;
        if (filePart != null && filePart.getSize() > 0) {
            imageBytes = filePart.getInputStream().readAllBytes();
        }

        // Validate password change if fields are filled
        boolean changingPassword = currentPassword != null && !currentPassword.isEmpty()
                || newPassword != null && !newPassword.isEmpty()
                || confirmPassword != null && !confirmPassword.isEmpty();

        if (changingPassword) {
            // Verify current password
            if (!currentUser.getPassword().equals(currentPassword)) {
                session.setAttribute("error", "Current password is incorrect");
                response.sendRedirect("EditProfileServlet");
                return;
            }

            // Validate new password
            if (!newPassword.equals(confirmPassword)) {
                session.setAttribute("error", "New passwords don't match");
                response.sendRedirect("EditProfileServlet");
                return;
            }

            if (newPassword.length() < 8) {
                session.setAttribute("error", "Password must be at least 8 characters");
                response.sendRedirect("EditProfileServlet");
                return;
            }

            updatedUser.setPassword(newPassword);
        }

        // Update user model
        updatedUser.setId(currentUser.getId());
        updatedUser.setName(name);
        updatedUser.setEmail(email);
        updatedUser.setRole(currentUser.getRole());
        updatedUser.setImage(imageBytes != null ? imageBytes : currentUser.getImage());

        // Update in database
        boolean updated = UserDAO.updateUser(updatedUser, changingPassword);

        if (updated) {
            // Update session with new user data (except password for security)
            currentUser.setName(updatedUser.getName());
            currentUser.setEmail(updatedUser.getEmail());
            currentUser.setImage(updatedUser.getImage());
            session.setAttribute("user", currentUser);

            session.setAttribute("success", "Profile updated successfully");
            response.sendRedirect("UserDashboardServlet");
        } else {
            session.setAttribute("error", "Failed to update profile");
            response.sendRedirect("EditProfileServlet");
        }
    }
}