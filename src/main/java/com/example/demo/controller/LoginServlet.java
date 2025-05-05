package com.example.demo.controller;

import com.example.demo.models.UserModel;
import com.example.demo.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Base64;

/**
 * LoginServlet
 *
 * Handles user login.
 * If login is successful:
 * - Starts a session
 * - Redirects to dashboard based on user role
 * - Handles "Remember Me" cookie
 */
@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // If user is already logged in, redirect to the correct dashboard
        if (AuthService.isAuthenticated(request)) {
            UserModel user = AuthService.getCurrentUser(request);
            if (user.getRole() == UserModel.Role.admin) {
                response.sendRedirect("AdminDashboardServlet");
            } else {
                response.sendRedirect("UserDashboardServlet");
            }
            return;
        }

        // Check for saved email in cookie (Remember Me feature)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("userEmail")) {
                    request.setAttribute("savedEmail", c.getValue());
                    break;
                }
            }
        }

        // Pass success message (like "registration successful") to the login page
        String message = request.getParameter("message");
        if (message != null && !message.isEmpty()) {
            request.setAttribute("successMessage", message);
        }

        // Show login form
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get email, password, and remember checkbox from form
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String remember = request.getParameter("remember");

            // Validate email
            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Email is required");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // Validate password
            if (password == null || password.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Password is required");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // Authenticate user
            UserModel user = AuthService.login(email, password);

            if (user != null) {
                // Login successful - create session
                AuthService.createUserSession(request, user, 1800); // 30 minutes

                // Handle "Remember Me" cookie
                if ("on".equals(remember)) {
                    Cookie cookie = new Cookie("userEmail", email);
                    cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                    response.addCookie(cookie);
                } else {
                    // Clear cookie if checkbox was not selected
                    Cookie cookie = new Cookie("userEmail", "");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }

                // If user has a profile image, prepare it as base64 (optional use in frontend)
                if (user.getImage() != null && user.getImage().length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(user.getImage());
                    request.setAttribute("base64Image", base64Image);
                }

                // Redirect to appropriate dashboard
                if (user.getRole() == UserModel.Role.admin) {
                    response.sendRedirect("AdminDashboardServlet");
                } else {
                    response.sendRedirect("UserDashboardServlet");
                }
            } else {
                // Login failed
                request.setAttribute("errorMessage", "Invalid email or password");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Handle unexpected error
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
