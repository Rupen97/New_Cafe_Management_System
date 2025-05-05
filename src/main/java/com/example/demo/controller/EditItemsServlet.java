package com.example.demo.controller;

import com.example.demo.dao.ItemsDAO;
import com.example.demo.models.itemsModel;
import com.example.demo.models.itemsModel.Category;
import com.example.demo.models.itemsModel.Status;
import com.example.demo.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.Base64;

@WebServlet(name = "EditItemServlet", value = "/EditItemServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5,   // 5MB
        maxRequestSize = 1024 * 1024 * 10 // 10MB
)
public class EditItemsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthService.isAdmin(request)) {
            response.sendRedirect("LoginServlet");
            return;
        }

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            itemsModel item = ItemsDAO.getItemById(id);

            if (item == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("item", item);

            // Convert image to Base64 for display
            if (item.getImage() != null && item.getImage().length > 0) {
                String base64Image = Base64.getEncoder().encodeToString(item.getImage());
                request.setAttribute("base64Image", base64Image);
            }

            request.getRequestDispatcher("/WEB-INF/views/edit-items.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
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
            int id = Integer.parseInt(request.getParameter("id"));
            itemsModel existingItem = ItemsDAO.getItemById(id);

            if (existingItem == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

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
                imageBytes = filePart.getInputStream().readAllBytes();
            }

            // Update item model
            itemsModel updatedItem = new itemsModel();
            updatedItem.setId(id);
            updatedItem.setName(name);
            updatedItem.setDescription(description);
            updatedItem.setPrice(price);
            updatedItem.setQuantity(quantity);
            updatedItem.setCategory(category);
            updatedItem.setStatus(status);
            updatedItem.setImage(imageBytes != null ? imageBytes : existingItem.getImage());

            // Update in database
            boolean updated = ItemsDAO.updateItem(updatedItem, imageBytes != null);

            if (updated) {
                session.setAttribute("success", "Item updated successfully");
                response.sendRedirect("ListItemsServlet");
            } else {
                session.setAttribute("error", "Failed to update item");
                response.sendRedirect("EditItemServlet?id=" + id);
            }
        } catch (Exception ex) {
            session.setAttribute("error", "An error occurred: " + ex.getMessage());
            response.sendRedirect("ListItemsServlet");
        }
    }
}