package com.example.demo.controller;

import com.example.demo.models.UserModel;
import com.example.demo.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.Base64;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check for existing session
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            UserModel user = (UserModel) session.getAttribute("user");
            redirectBasedOnRole(user, response);
            return;
        }

        // Check for remember-me cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remember_me".equals(cookie.getName())) {
                    String email = cookie.getValue();
                    // Attempt to login with cookie (note: in production you'd need additional security)
                    UserModel user = AuthService.login(email, "", true); // Empty password for cookie login
                    if (user != null) {
                        session = request.getSession();
                        session.setAttribute("user", user);
                        redirectBasedOnRole(user, response);
                        return;
                    }
                }
            }
        }

        // Check for success message from registration
        String message = request.getParameter("message");
        if (message != null && !message.isEmpty()) {
            request.setAttribute("successMessage", message);
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String rememberMe = request.getParameter("remember-me");

            // Validate input
            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Email and password are required");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // Authenticate user
            boolean remember = rememberMe != null && rememberMe.equals("on");
            UserModel user = AuthService.login(email, password, remember);

            if (user != null) {
                // Create session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setMaxInactiveInterval(remember ? 30 * 24 * 60 * 60 : 1800); // 30 days or 30 minutes

                // Set remember-me cookie if requested
                if (remember) {
                    Cookie rememberCookie = new Cookie("remember_me", email);
                    rememberCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
                    rememberCookie.setPath("/");
                    rememberCookie.setHttpOnly(true);
                    response.addCookie(rememberCookie);
                }

                // Prepare user data for dashboard
                request.setAttribute("user", user);
                if (user.getImage() != null && user.getImage().length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(user.getImage());
                    request.setAttribute("base64Image", base64Image);
                }

                redirectBasedOnRole(user, response);
            } else {
                request.setAttribute("errorMessage", "Invalid email or password");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    private void redirectBasedOnRole(UserModel user, HttpServletResponse response) throws IOException {
        if (user.getRole() == UserModel.Role.admin) {
            response.sendRedirect("AdminDashboardServlet");
        } else {
            response.sendRedirect("UserDashboardServlet");
        }
    }
}