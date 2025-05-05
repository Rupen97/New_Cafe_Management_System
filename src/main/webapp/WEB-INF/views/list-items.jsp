<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.demo.models.itemsModel" %>
<%@ page import="com.example.demo.models.itemsModel.Category" %>
<%@ page import="com.example.demo.models.itemsModel.Status" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Item Management - Brew and Beans</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-dashboard.css">
    <style>
        .item-image {
            max-width: 80px;
            max-height: 80px;
            display: block;
        }
        .filter-form {
            background: #f5f5f5;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        .filter-form label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .filter-form select, .filter-form input {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .filter-buttons {
            display: flex;
            gap: 10px;
        }
        .status-available {
            color: green;
            font-weight: bold;
        }
        .status-unavailable {
            color: #cc0000;
            font-weight: bold;
        }
        .action-buttons {
            display: flex;
            gap: 5px;
        }
    </style>
</head>
<body>
<c:if test="${not empty success}">
    <div class="alert alert-success">
        <p>${success}</p>
    </div>
</c:if>
<c:if test="${not empty error}">
    <div class="alert alert-error">
        <p>${error}</p>
    </div>
</c:if>

<header>
    <h1>Item Management</h1>
    <p>Brew and Beans - Cafe Management System</p>
</header>

<div class="container clearfix">
    <div class="sidebar">
        <h2>Admin Menu</h2>
        <div class="menu-item"><a href="${pageContext.request.contextPath}//AdminDashboardServlet">Dashboard</a></div>
        <div class="menu-item"><a href="${pageContext.request.contextPath}/ListItemsServlet">Manage Items</a></div>
        <div class="menu-item"><a href="${pageContext.request.contextPath}/ListUsersServlet">Manage Users</a></div>
        <div class="menu-item"><a href="${pageContext.request.contextPath}/admin/profile.jsp">Profile Settings</a></div>
        <div class="menu-item"><a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a></div>
    </div>

    <div class="main-content">
        <div class="card">
            <h2>Item Management</h2>

            <!-- Filter Form -->
            <div class="filter-form">
                <form action="${pageContext.request.contextPath}/ListItemsServlet" method="get">
                    <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 15px;">
                        <div>
                            <label for="name">Name</label>
                            <input type="text" id="name" name="name" value="${param.name}" placeholder="Search by name...">
                        </div>
                        <div>
                            <label for="status">Status</label>
                            <select id="status" name="status">
                                <option value="">All Statuses</option>
                                <option value="AVAILABLE" ${param.status == 'AVAILABLE' ? 'selected' : ''}>Available</option>
                                <option value="UNAVAILABLE" ${param.status == 'UNAVAILABLE' ? 'selected' : ''}>Unavailable</option>
                            </select>
                        </div>
                        <div>
                            <label for="category">Category</label>
                            <select id="category" name="category">
                                <option value="">All Categories</option>
                                <option value="DRINKS" ${param.category == 'DRINKS' ? 'selected' : ''}>Drinks</option>
                                <option value="DESSERT" ${param.category == 'DESSERT' ? 'selected' : ''}>Dessert</option>
                                <option value="BAKERY" ${param.category == 'BAKERY' ? 'selected' : ''}>Bakery</option>
                                <option value="OTHER" ${param.category == 'OTHER' ? 'selected' : ''}>Other</option>
                            </select>
                        </div>
                    </div>
                    <div class="filter-buttons" style="margin-top: 10px;">
                        <button type="submit" class="btn-small">Filter</button>
                        <a href="${pageContext.request.contextPath}/ListItemsServlet" class="btn-small">Reset</a>
                        <a href="${pageContext.request.contextPath}/AddItemServlet" class="btn-small" style="margin-left: auto;">Add New Item</a>
                    </div>
                </form>
            </div>

            <!-- Items Table -->
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Image</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Category</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${items}" var="item">
                    <tr>
                        <td>${item.id}</td>
                        <td>
                            <c:if test="${not empty item.image}">
                                <img src="data:image/jpeg;base64,${item.image}" class="item-image" alt="${item.name}">
                            </c:if>
                            <c:if test="${empty item.image}">
                                <span class="text-muted">No image</span>
                            </c:if>
                        </td>
                        <td>${item.name}</td>
                        <td>${item.description}</td>
                        <td>$${String.format("%.2f", item.price)}</td>
                        <td>${item.quantity}</td>
                        <td>${item.category}</td>
                        <td class="${item.status == 'AVAILABLE' ? 'status-available' : 'status-unavailable'}">
                                ${item.status}
                        </td>
                        <td>
                            <div class="action-buttons">
                                <a href="${pageContext.request.contextPath}/EditItemServlet?id=${item.id}" class="btn-small">Edit</a>
                                <form action="${pageContext.request.contextPath}/DeleteItemServlet" method="post" style="display:inline;">
                                    <input type="hidden" name="id" value="${item.id}">
                                    <button type="submit" class="btn-small btn-danger"
                                            onclick="return confirm('Are you sure you want to delete this item?')">
                                        Delete
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty items}">
                    <tr>
                        <td colspan="9" style="text-align: center;">No items found</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<footer>
    <p>&copy; 2025 Brew and Beans - Cafe Management System</p>
</footer>
</body>
</html>