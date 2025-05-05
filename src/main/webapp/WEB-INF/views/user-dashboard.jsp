<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.demo.models.UserModel" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.demo.models.itemsModel" %>
<%
    UserModel user = (UserModel) request.getAttribute("user");
    String base64Image = (String) request.getAttribute("base64Image");
    List<itemsModel> recommendedItems = (List<itemsModel>) request.getAttribute("recommendedItems");
    int orderCount = request.getAttribute("orderCount") != null ? (Integer) request.getAttribute("orderCount") : 0;
%>
<html>
<head>
    <title>Customer Dashboard - Brew & Beans</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/edit-user.css">
    <style>
        :root {
            --primary-color: #4f46e5;
            --primary-light: #6366f1;
            --secondary-color: #6b7280;
            --light-gray: #f9fafb;
            --border-color: #e5e7eb;
            --success-color: #10b981;
            --warning-color: #f59e0b;
            --danger-color: #ef4444;
            --card-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
        }

        /* Layout Improvements */
        .container {
            display: grid;
            grid-template-columns: 250px 1fr;
            gap: 2rem;
            max-width: 1200px;
            margin: 0 auto;
            padding: 1rem;
        }

        /* Sidebar Enhancements */
        .sidebar {
            background-color: #fff;
            border-radius: 12px;
            box-shadow: var(--card-shadow);
            padding: 1.5rem;
            position: sticky;
            top: 1rem;
            height: fit-content;
        }

        .sidebar h2 {
            font-size: 1.25rem;
            margin-bottom: 1.5rem;
            color: var(--primary-color);
            text-transform: uppercase;
            letter-spacing: 0.05em;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .sidebar h2:before {
            content: "";
            display: block;
            width: 5px;
            height: 1.25rem;
            background: var(--primary-color);
            border-radius: 3px;
        }

        .menu-item {
            margin-bottom: 1rem;
            transition: all 0.2s ease;
        }

        .menu-item:hover {
            transform: translateX(3px);
        }

        .menu-item a {
            color: var(--secondary-color);
            text-decoration: none;
            font-weight: 600;
            font-size: 0.95rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
            padding: 0.5rem 0;
        }

        .menu-item a i {
            width: 20px;
            text-align: center;
        }

        .menu-item a:hover {
            color: var(--primary-color);
        }

        /* Main Content Styling */
        .main-content {
            display: flex;
            flex-direction: column;
            gap: 2rem;
        }

        /* Welcome Card */
        .welcome-card {
            background: white;
            border-radius: 12px;
            box-shadow: var(--card-shadow);
            padding: 2rem;
        }

        .welcome-card h2 {
            color: var(--primary-color);
            margin-bottom: 1.5rem;
            font-size: 1.5rem;
        }

        .user-profile {
            display: flex;
            align-items: center;
            gap: 2rem;
        }

        .profile-image {
            position: relative;
        }

        .profile-image img {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #eee;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .profile-status {
            position: absolute;
            bottom: 10px;
            right: 10px;
            width: 15px;
            height: 15px;
            background-color: var(--success-color);
            border-radius: 50%;
            border: 2px solid white;
        }

        .profile-details {
            flex: 1;
        }

        .profile-details p {
            margin: 0.5rem 0;
            color: #555;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .profile-details strong {
            color: #333;
            min-width: 80px;
            display: inline-block;
        }

        /* Stats Cards */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1.5rem;
        }

        .stat-card {
            background: white;
            border-radius: 12px;
            box-shadow: var(--card-shadow);
            padding: 1.5rem;
            text-align: center;
            transition: transform 0.3s ease;
        }

        .stat-card:hover {
            transform: translateY(-5px);
        }

        .stat-card i {
            font-size: 1.75rem;
            color: var(--primary-light);
            margin-bottom: 0.5rem;
        }

        .stat-card h3 {
            font-size: 1.75rem;
            color: var(--primary-color);
            margin: 0.5rem 0;
        }

        .stat-card p {
            color: var(--secondary-color);
            margin: 0;
            font-size: 0.9rem;
        }

        /* Recommended Items */
        .recommended-section {
            background: white;
            border-radius: 12px;
            box-shadow: var(--card-shadow);
            padding: 2rem;
        }

        .section-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
        }

        .section-header h2 {
            color: var(--primary-color);
            font-size: 1.5rem;
            margin: 0;
        }

        .items-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 1.5rem;
        }

        .item-card {
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
        }

        .item-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
        }

        .item-image {
            height: 150px;
            overflow: hidden;
        }

        .item-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.3s ease;
        }

        .item-card:hover .item-image img {
            transform: scale(1.05);
        }

        .item-details {
            padding: 1rem;
            background: white;
        }

        .item-name {
            font-weight: 600;
            color: #333;
            margin-bottom: 0.25rem;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .item-price {
            color: var(--success-color);
            font-weight: bold;
            margin-bottom: 0.5rem;
        }

        .item-category {
            font-size: 0.8rem;
            color: var(--secondary-color);
            background: var(--light-gray);
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            display: inline-block;
        }

        /* Buttons */
        .btn {
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            background: var(--primary-color);
            color: white;
            padding: 0.6rem 1.2rem;
            border: none;
            border-radius: 6px;
            font-size: 0.95rem;
            font-weight: 600;
            text-decoration: none;
            transition: all 0.3s ease;
            cursor: pointer;
        }

        .btn:hover {
            background: #4338ca;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .btn-outline {
            background: transparent;
            border: 1px solid var(--primary-color);
            color: var(--primary-color);
        }

        .btn-outline:hover {
            background: var(--primary-light);
            color: white;
        }

        /* Icons (using Unicode as fallback) */
        .icon-dashboard:before { content: "📊"; }
        .icon-menu:before { content: "🍽️"; }
        .icon-orders:before { content: "📋"; }
        .icon-profile:before { content: "👤"; }
        .icon-logout:before { content: "🚪"; }
        .icon-coffee:before { content: "☕"; }
        .icon-orders-count:before { content: "📦"; }
        .icon-favorites:before { content: "❤️"; }
        .icon-points:before { content: "⭐"; }
    </style>
</head>
<body>
<header>
    <h1>Customer Dashboard</h1>
    <p>Welcome back to Brew & Beans</p>
</header>

<div class="container clearfix">
    <!-- Enhanced Sidebar -->
    <div class="sidebar">
        <h2>Customer Menu</h2>
        <div class="menu-item"><a href="UserDashboardServlet"><i class="icon-dashboard"></i> Dashboard</a></div>
        <div class="menu-item"><a href="CustomerItemsServlet"><i class="icon-menu"></i> Browse Menu</a></div>
        <div class="menu-item"><a href="${pageContext.request.contextPath}/view-order"><i class="icon-orders"></i> My Orders</a></div>

        <div class="menu-item"><a href="EditProfileServlet"><i class="icon-profile"></i> Profile Settings</a></div>
        <div class="menu-item"><a href="${pageContext.request.contextPath}/LogoutServlet"><i class="icon-logout"></i> Logout</a></div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <% if (request.getAttribute("successMessage") != null) { %>
        <div class="alert alert-success"><%= request.getAttribute("successMessage") %></div>
        <% } %>
        <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("errorMessage") %></div>
        <% } %>

        <!-- Welcome Card with Enhanced Profile -->
        <div class="welcome-card">
            <h2>Welcome back, <%= user.getName() %></h2>
            <div class="user-profile">
                <div class="profile-image">
                    <img src="data:image/jpeg;base64,<%= base64Image != null ? base64Image : "" %>"
                         alt="Profile Picture"
                         onerror="this.src='${pageContext.request.contextPath}/assets/images/default-profile.svg'">
                    <div class="profile-status" title="Active"></div>
                </div>
                <div class="profile-details">
                    <p><strong>Email:</strong> <%= user.getEmail() %></p>
                    <p><strong>Member Since:</strong> January 2023</p>
                    <p><strong>Loyalty Points:</strong> 1,250</p>
                    <p><strong>Status:</strong> <span style="color: var(--success-color);">Gold Member</span></p>
                </div>
            </div>
        </div>

        <!-- Stats Cards -->
        <div class="stats-grid">
            <div class="stat-card">
                <i class="icon-coffee"></i>
                <h3><%= orderCount %></h3>
                <p>Total Orders</p>
            </div>
            <div class="stat-card">
                <i class="icon-favorites"></i>
                <h3>12</h3>
                <p>Favorite Items</p>
            </div>
            <div class="stat-card">
                <i class="icon-points"></i>
                <h3>1,250</h3>
                <p>Loyalty Points</p>
            </div>
            <div class="stat-card">
                <i class="icon-orders-count"></i>
                <h3>3</h3>
                <p>Active Orders</p>
            </div>
        </div>

        <!-- Recommended Items Section -->
        <div class="recommended-section">
            <div class="section-header">
                <h2>Recommended For You</h2>
                <a href="CustomerItemsServlet" class="btn btn-outline">View Full Menu</a>
            </div>

            <div class="items-grid">
                <% if (recommendedItems != null && !recommendedItems.isEmpty()) { %>
                <% for (itemsModel item : recommendedItems) { %>
                <div class="item-card">
                    <a href="ItemDetailServlet?id=<%= item.getId() %>">
                        <div class="item-image">
                            <% if (item.getImage() != null && item.getImage().length > 0) { %>
                            <img src="data:image/jpeg;base64,<%= java.util.Base64.getEncoder().encodeToString(item.getImage()) %>"
                                 alt="<%= item.getName() %>">
                            <% } else { %>
                            <img src="${pageContext.request.contextPath}/assets/images/default-food.jpg"
                                 alt="<%= item.getName() %>">
                            <% } %>
                        </div>
                        <div class="item-details">
                            <h3 class="item-name"><%= item.getName() %></h3>
                            <p class="item-price">$<%= String.format("%.2f", item.getPrice()) %></p>
                            <span class="item-category"><%= item.getCategory().name() %></span>
                        </div>
                    </a>
                </div>
                <% } %>
                <% } else { %>
                <p>No recommendations available. Start ordering to get personalized recommendations!</p>
                <% } %>
            </div>
        </div>
    </div>
</div>

<footer>
    <p>&copy; 2025 Bean & Bloom - Cafe Management System</p>
</footer>
</body>
</html>