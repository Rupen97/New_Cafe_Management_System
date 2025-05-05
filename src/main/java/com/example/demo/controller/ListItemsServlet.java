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
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "ListItemsServlet", value = "/ListItemsServlet")
public class ListItemsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthService.isAuthenticated(request)) {
            response.sendRedirect("LoginServlet");
            return;
        }

        HttpSession session = request.getSession();

        try {
            // Get filter parameters
            String nameFilter = request.getParameter("name");
            String statusFilter = request.getParameter("status");
            String categoryFilter = request.getParameter("category");

            // Store current filters for the view
            request.setAttribute("currentNameFilter", nameFilter);
            request.setAttribute("currentStatusFilter", statusFilter);
            request.setAttribute("currentCategoryFilter", categoryFilter);

            // Get filtered items using existing DAO methods
            List<itemsModel> items = applyFilters(nameFilter, statusFilter, categoryFilter);

            // Convert images to Base64 for display
            for (itemsModel item : items) {
                if (item.getImage() != null && item.getImage().length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(item.getImage());
                    item.setImage(base64Image.getBytes());
                }
            }

            request.setAttribute("items", items);
            request.getRequestDispatcher("/WEB-INF/views/list-items.jsp").forward(request, response);

        } catch (Exception ex) {
            session.setAttribute("error", "Error loading items: " + ex.getMessage());
            response.sendRedirect("DashboardServlet");
        }
    }

    private List<itemsModel> applyFilters(String nameFilter, String statusFilter, String categoryFilter) {
        List<itemsModel> items = new ArrayList<>();

        // Case 1: No filters - get all items
        if ((nameFilter == null || nameFilter.isEmpty()) &&
                (statusFilter == null || statusFilter.isEmpty()) &&
                (categoryFilter == null || categoryFilter.isEmpty())) {
            return ItemsDAO.getAllItems();
        }

        // Case 2: Only name filter
        if (nameFilter != null && !nameFilter.isEmpty() &&
                (statusFilter == null || statusFilter.isEmpty()) &&
                (categoryFilter == null || categoryFilter.isEmpty())) {
            return ItemsDAO.searchItemsByName(nameFilter);
        }

        // Case 3: Only status filter
        if ((nameFilter == null || nameFilter.isEmpty()) &&
                statusFilter != null && !statusFilter.isEmpty() &&
                (categoryFilter == null || categoryFilter.isEmpty())) {
            Status status = Status.valueOf(statusFilter.toUpperCase());
            return ItemsDAO.getItemsByStatus(status);
        }

        // Case 4: Only category filter
        if ((nameFilter == null || nameFilter.isEmpty()) &&
                (statusFilter == null || statusFilter.isEmpty()) &&
                categoryFilter != null && !categoryFilter.isEmpty()) {
            Category category = Category.valueOf(categoryFilter.toUpperCase());
            return ItemsDAO.getItemsByCategory(category);
        }

        // Case 5: Combined filters - we need to intersect results
        List<itemsModel> filteredItems = ItemsDAO.getAllItems();

        // Apply name filter if present
        if (nameFilter != null && !nameFilter.isEmpty()) {
            List<itemsModel> nameFiltered = ItemsDAO.searchItemsByName(nameFilter);
            filteredItems = intersectLists(filteredItems, nameFiltered);
        }

        // Apply status filter if present
        if (statusFilter != null && !statusFilter.isEmpty()) {
            Status status = Status.valueOf(statusFilter.toUpperCase());
            List<itemsModel> statusFiltered = ItemsDAO.getItemsByStatus(status);
            filteredItems = intersectLists(filteredItems, statusFiltered);
        }

        // Apply category filter if present
        if (categoryFilter != null && !categoryFilter.isEmpty()) {
            Category category = Category.valueOf(categoryFilter.toUpperCase());
            List<itemsModel> categoryFiltered = ItemsDAO.getItemsByCategory(category);
            filteredItems = intersectLists(filteredItems, categoryFiltered);
        }

        return filteredItems;
    }

    // Helper method to find intersection of two lists of items
    private List<itemsModel> intersectLists(List<itemsModel> list1, List<itemsModel> list2) {
        Set<Integer> ids = list2.stream()
                .map(itemsModel::getId)
                .collect(Collectors.toSet());

        return list1.stream()
                .filter(item -> ids.contains(item.getId()))
                .collect(Collectors.toList());
    }
}