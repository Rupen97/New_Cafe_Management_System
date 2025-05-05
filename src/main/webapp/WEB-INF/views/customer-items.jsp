<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.demo.models.itemsModel" %>
<%@ page import="com.example.demo.models.itemsModel.Category" %>
<%@ page import="com.example.demo.models.itemsModel.Status" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Item Catalog - Brew and Beans</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-dashboard.css">
    <style>
        /* Overall Layout */
        .item-card {
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            margin: 15px;
            text-align: center;
            padding: 15px;
            width: 280px;
        }
        .item-card img {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
        }
        .item-card h3 {
            font-size: 1.2em;
            margin-top: 15px;
            color: #333;
        }
        .item-card p {
            font-size: 0.9em;
            color: #666;
            margin-top: 5px;
        }
        .item-card .price {
            font-size: 1.1em;
            font-weight: bold;
            color: #27ae60;
            margin-top: 10px;
        }
        .item-card .status {
            font-weight: bold;
            margin-top: 5px;
        }
        .item-card .status-available {
            color: green;
        }
        .item-card .status-unavailable {
            color: #e74c3c;
        }
        .item-card .add-to-cart-btn {
            background-color: #3498db;
            color: white;
            padding: 8px 16px;
            border-radius: 4px;
            text-decoration: none;
            margin-top: 10px;
            display: inline-block;
            transition: background-color 0.3s;
        }
        .item-card .order-now-btn {
            background-color: #2ecc71;
            color: white;
            padding: 8px 16px;
            border-radius: 4px;
            text-decoration: none;
            margin-top: 10px;
            display: inline-block;
            transition: background-color 0.3s;
        }
        .item-card .add-to-cart-btn:hover {
            background-color: #2980b9;
        }
        .item-card .order-now-btn:hover {
            background-color: #27ae60;
        }
        .item-card .button-group {
            display: flex;
            gap: 10px;
            justify-content: center;
            margin-top: 15px;
        }
        /* Filter Form Styling */
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
    </style>
</head>
<body>

<header>
    <h1>Item Catalog</h1>
    <p>Browse our selection of items at Brew and Beans</p>
</header>

<!-- Filter Form -->
<div class="filter-form">
    <form action="${pageContext.request.contextPath}/CustomerItemsServlet" method="get">
        <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 15px;">
            <div>
                <label for="name">Search</label>
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
            <a href="${pageContext.request.contextPath}/CustomerItemsServlet" class="btn-small">Reset</a>
        </div>
    </form>
</div>

<!-- Items Grid -->
<div class="container" style="display: flex; flex-wrap: wrap; justify-content: center;">
    <c:forEach items="${items}" var="item">
        <div class="item-card">
            <c:if test="${not empty item.image}">
                <img src="data:image/jpeg;base64,${item.image}" alt="${item.name}">
            </c:if>
            <c:if test="${empty item.image}">
                <img src="${pageContext.request.contextPath}/assets/images/default-item.jpg" alt="Default Item">
            </c:if>
            <h3>${item.name}</h3>
            <p>${item.description}</p>
            <p class="price">$${String.format("%.2f", item.price)}</p>
            <p class="status ${item.status == 'AVAILABLE' ? 'status-available' : 'status-unavailable'}">${item.status}</p>
            <div class="button-group">
                <!-- Simplified and cleaner redirection -->
                <a href="${pageContext.request.contextPath}/item-details?id=${item.id}" class="add-to-cart-btn">View Details</a>
                <a href="AddOrderServlet?action=view&itemId=${item.id}">Order</a>
            </div>
        </div>
    </c:forEach>
</div>


<footer>
    <p>&copy; 2025 Brew and Beans - Cafe Management System</p>
</footer>

</body>
</html>