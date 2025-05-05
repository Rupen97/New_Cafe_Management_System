package com.example.demo.controller;

import com.example.demo.dao.ItemsDAO;
import com.example.demo.models.itemsModel;
import com.example.demo.models.itemsModel.Category;
import com.example.demo.models.itemsModel.Status;
import com.example.demo.models.itemsModel;
import com.example.demo.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "AddItemServlet", value = "/AddItemServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,     // 1MB
        maxFileSize = 1024 * 1024 * 5,      // 5MB
        maxRequestSize = 1024 * 1024 * 10   // 10MB
)
public class AddItemsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthService.isAdmin(request)) {
            response.sendRedirect("LoginServlet");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/add-items.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthService.isAdmin(request)) {
            response.sendRedirect("LoginServlet");
            return;
        }

        HttpSession session = request.getSession();

        try {
            // Get form data
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            Category category = Category.valueOf(request.getParameter("category"));
            Status status = Status.valueOf(request.getParameter("status"));

            // Handle image upload
            Part filePart = request.getPart("itemImage");
            byte[] imageBytes = null;

            if (filePart != null && filePart.getSize() > 0) {
                // Validate file type
                String contentType = filePart.getContentType();
                if (!contentType.startsWith("image/")) {
                    session.setAttribute("error", "Only image files are allowed");
                    response.sendRedirect("AddItemServlet");
                    return;
                }

                // Validate file size
                if (filePart.getSize() > 5 * 1024 * 1024) {
                    session.setAttribute("error", "Image size must be less than 5MB");
                    response.sendRedirect("AddItemServlet");
                    return;
                }

                // Read file content
                try (InputStream fileContent = filePart.getInputStream()) {
                    imageBytes = fileContent.readAllBytes();
                } catch (IOException e) {
                    session.setAttribute("error", "Error reading uploaded file");
                    response.sendRedirect("AddItemServlet");
                    return;
                }
            }

            // Create new item
            itemsModel newItem = new itemsModel();
            newItem.setName(name);
            newItem.setDescription(description);
            newItem.setPrice(price);
            newItem.setQuantity(quantity);
            newItem.setCategory(category);
            newItem.setStatus(status);
            newItem.setImage(imageBytes);

            // Save to database
            int itemId = ItemsDAO.createItem(newItem);

            if (itemId > 0) {
                session.setAttribute("success", "Item added successfully");
                response.sendRedirect("ListItemsServlet");
            } else {
                session.setAttribute("error", "Failed to add item");
                response.sendRedirect("AddItemServlet");
            }
        } catch (Exception ex) {
            session.setAttribute("error", "An error occurred: " + ex.getMessage());
            response.sendRedirect("AddItemServlet");
        }
    }
}