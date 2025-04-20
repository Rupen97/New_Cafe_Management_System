package com.example.demo.controller;

import com.example.demo.models.UserModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.Base64;

/**
 * UserDashboardServlet
 *
 * This servlet handles the customer/user dashboard functionality.
 * It retrieves user information from the session and forwards it to the user dashboard JSP.
 */
@WebServlet(name = "UserDashboardServlet", value = "/UserDashboardServlet")
public class UserDashboardServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the user from the session
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            // If no session or user, redirect to login
            response.sendRedirect("LoginServlet");
            return;
        }
        
        UserModel user = (UserModel) session.getAttribute("user");
        
        // Check if the user is a customer/user (not an admin)
        if (user.getRole() == UserModel.Role.admin) {
            // If admin, redirect to admin dashboard
            response.sendRedirect("AdminDashboardServlet");
            return;
        }
        
        // Set user data for the JSP
        request.setAttribute("user", user);
        
        // Set base64 image if available
        if (user.getImage() != null && user.getImage().length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(user.getImage());
            request.setAttribute("base64Image", base64Image);
        }
        
        // Forward to the user dashboard JSP
        request.getRequestDispatcher("/WEB-INF/views/user-dashboard.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle any POST requests (if needed)
        doGet(request, response);
    }
}
