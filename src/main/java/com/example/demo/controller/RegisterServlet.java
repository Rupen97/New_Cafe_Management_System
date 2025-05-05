package com.example.demo.controller;
import com.example.demo.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

/**
 * RegisterServlet handles user registration.
 * Shows the registration form on GET and processes registration on POST.
 * Supports profile image upload using @MultipartConfig.
 */
@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5, // 5MB
        maxRequestSize = 1024 * 1024 * 20 // 20MB
)
public class RegisterServlet extends HttpServlet {

    /**
     * Handles GET request to show the registration form.
     * If the user is already logged in, they can be redirected to the dashboard.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    /**
     * Handles POST request to register a new user.
     * Validates input, processes profile image, and registers the user using AuthService.
     * Redirects to login page on success. Shows error on failure.
     *
     * Optionally, you can:
     * - Start a session and redirect to dashboard after registration
     * - Set a remember-me cookie for the user
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get form values
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirm-password");
            String role = request.getParameter("role");

            // Validate name
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Name is required");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }

            // Validate email
            if (email == null || email.trim().isEmpty() || !email.contains("@")) {
                request.setAttribute("errorMessage", "Valid email is required");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }

            // Validate password
            if (password == null || password.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Password is required");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                request.setAttribute("errorMessage", "Passwords do not match");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }

            // Handle image upload
            Part imagePart = request.getPart("image");
            byte[] imageBytes = null;
            if (imagePart != null && imagePart.getSize() > 0) {
                imageBytes = imagePart.getInputStream().readAllBytes();
            }

            // Register user
            int userID = AuthService.register(name, email, password, role, imageBytes);

            if (userID != -1) {
                // Success: redirect to login with a message
                response.sendRedirect("LoginServlet?message=Registration+successful!+Please+login+with+your+credentials.");

                // Optional: start session and redirect to dashboard
                // UserModel user = AuthService.getUserById(userID);
                // HttpSession session = request.getSession();
                // session.setAttribute("user", user);
                // if (user.getRole() == UserModel.Role.admin) {
                //     response.sendRedirect("AdminDashboardServlet");
                // } else {
                //     response.sendRedirect("UserDashboardServlet");
                // }

                // Optional: remember-me cookie
                // Cookie userCookie = new Cookie("user_email", email);
                // userCookie.setMaxAge(60*60*24*30); // 30 days
                // userCookie.setPath("/");
                // response.addCookie(userCookie);
            } else {
                // Failure: show error
                request.setAttribute("errorMessage", "Registration failed. Email may already be in use.");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Handle error
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}
