package com.example.demo.controller;

import com.example.demo.models.UserModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.annotation.MultipartConfig;

import java.io.IOException;

@WebServlet(name = "DashboardServlet", value = "/DashboardServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)

public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check authentication
        HttpSession session = request.getSession();
        UserModel user = (UserModel) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        // Set user data for the JSP
        request.setAttribute("user", user);

        // Forward to dashboard JSP
        request.getRequestDispatcher("/WEB-INF/views/list-items.jsp").forward(request, response);
    }
}