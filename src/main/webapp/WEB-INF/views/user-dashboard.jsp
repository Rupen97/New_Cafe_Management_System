<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.demo.models.UserModel" %>
<%
    UserModel user = (UserModel) request.getAttribute("user");
    String base64Image = (String) request.getAttribute("base64Image");
%>
<html>
<head>
    <title>Customer Dashboard - Advanced Programming and Technologies</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/edit-user.css">
    <style>
        :root {
            --primary-color: #4f46e5; /* Updated primary color */
            --secondary-color: #6b7280;
            --light-gray: #f9fafb;
            --border-color: #e5e7eb;
            --success-color: #10b981;
            --warning-color: #f59e0b;
            --danger-color: #ef4444;
        }

        .sidebar {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            padding: 1.5rem;
            margin-bottom: 2rem;
        }

        .sidebar h2 {
            font-size: 1.3rem;
            margin-bottom: 1rem;
            color: var(--primary-color); /* Updated to use primary color */
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }

        .menu-item {
            margin-bottom: 1rem;
        }

        .menu-item a {
            color: var(--secondary-color);
            text-decoration: none;
            font-weight: 600;
            font-size: 1rem;
        }

        .menu-item a:hover {
            color: var(--primary-color); /* Updated hover color */
            text-decoration: underline;
        }

        .user-profile {
            display: flex;
            align-items: center;
            gap: 2rem;
            margin-top: 1rem;
        }

        .profile-image img {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #eee;
        }

        .profile-details p {
            margin: 0.5rem 0;
            color: #555;
        }

        .btn-small {
            display: inline-block;
            background: var(--primary-color); /* Updated button color */
            color: white;
            padding: 0.6rem 1.2rem;
            border: none;
            border-radius: 4px;
            font-size: 0.95rem;
            font-weight: 600;
            text-decoration: none;
            transition: all 0.3s ease;
        }

        .btn-small:hover {
            background: #4338ca; /* Darker shade for hover */
            transform: translateY(-1px);
        }

        header h1 {
            color: var(--primary-color); /* Updated header color */
        }
    </style>
</head>
<body>
<header>
    <h1>Customer Dashboard</h1>
    <p>Brew & Beans</p>
</header>

<div class="container clearfix">
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Customer Menu</h2>
        <div class="menu-item"><a href="UserDashboardServlet">Dashboard</a></div>
        <div class="menu-item"><a href="#">My Items</a></div>
        <div class="menu-item"><a href="#">Order History</a></div>
        <div class="menu-item"><a href="EditProfileServlet">Profile Settings</a></div>
        <div class="menu-item"><a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a></div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <% if (request.getAttribute("successMessage") != null) { %>
        <div class="alert alert-success"><%= request.getAttribute("successMessage") %></div>
        <% } %>
        <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("errorMessage") %></div>
        <% } %>

        <!-- Welcome Card -->
        <div class="card">
            <h2>Welcome, <%= user.getName() %></h2>
            <div class="user-profile">
                <div class="profile-image">
                    <img src="data:image/jpeg;base64,<%= base64Image != null ? base64Image : "" %>"
                         alt="Profile Picture"
                         onerror="this.src='${pageContext.request.contextPath}/assets/images/default-profile.svg'">
                </div>
                <div class="profile-details">
                    <p><strong>Email:</strong> <%= user.getEmail() %></p>
                    <p><strong>Role:</strong> Student</p>
                    <p><strong>User ID:</strong> <%= user.getId() %></p>
                </div>
            </div>
        </div>

        <!-- Account Actions -->
        <div class="card">
            <h2>Account Actions</h2>
            <a href="EditProfileServlet" class="btn-small">Edit Profile</a>
        </div>
    </div>
</div>

<footer>
    <p>&copy; 2025 Bean & Bloom - Cafe Management System</p>
</footer>
</body>
</html>