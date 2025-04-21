<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.demo.models.UserModel" %>
<html>
<head>
    <title>Edit Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/edit-user.css">
    <style>
        .password-fields {
            margin-top: 2rem;
            padding-top: 1rem;
            border-top: 1px solid #eee;
        }

        .current-image {
            display: flex;
            align-items: center;
            margin-bottom: 1rem;
        }

        .current-image img {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            margin-right: 1rem;
            object-fit: cover;
        }

        .form-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 1rem;
        }

        .btn-secondary {
            background: #888;
        }

        .btn-secondary:hover {
            background: #666;
        }

        .alert {
            padding: 1rem;
            margin-bottom: 1rem;
            border-radius: 4px;
            font-weight: bold;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
        }

        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
        }

        .main-content {
            margin-left: 220px;
            padding: 2rem;
        }

        .sidebar {
            position: fixed;
            top: 0;
            left: 0;
            width: 200px;
            height: 100%;
            background-color: #2c3e50;
            color: white;
            padding: 2rem 1rem;
            box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
        }

        .sidebar h2 {
            margin-top: 0;
            font-size: 1.4rem;
            margin-bottom: 1.5rem;
        }

        .menu-item {
            margin-bottom: 1rem;
        }

        .menu-item a {
            color: white;
            text-decoration: none;
            font-weight: bold;
            transition: color 0.2s;
        }

        .menu-item a:hover {
            color: #6e8efb;
        }

        small {
            display: block;
            margin-top: 0.5rem;
            color: #888;
        }
    </style>
</head>
<body>
<header>
    <h1>Edit Profile</h1>
    <p>Advanced Programming and Technologies - Itahari International College</p>
</header>

<div class="container clearfix">
    <div class="sidebar">
        <h2>Student Menu</h2>
        <div class="menu-item"><a href="UserDashboardServlet">Dashboard</a></div>
        <div class="menu-item"><a href="#">Items</a></div>
        <div class="menu-item"><a href="#">Orders</a></div>
        <div class="menu-item"><a href="EditProfileServlet">Profile Settings</a></div>
        <div class="menu-item"><a href="${pageContext.request.contextPath}/index.jsp">Logout</a></div>
    </div>

    <div class="main-content">
        <div class="card">
            <h2>Edit Your Profile</h2>

            <% if (request.getSession().getAttribute("success") != null) { %>
            <div class="alert alert-success"><%= request.getSession().getAttribute("success") %></div>
            <% request.getSession().removeAttribute("success"); %>
            <% } %>
            <% if (request.getSession().getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getSession().getAttribute("error") %></div>
            <% request.getSession().removeAttribute("error"); %>
            <% } %>

            <form action="EditProfileServlet" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="name">Full Name:</label>
                    <input type="text" id="name" name="name"
                           value="<%= ((UserModel)request.getAttribute("user")).getName() %>" required>
                </div>

                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email"
                           value="<%= ((UserModel)request.getAttribute("user")).getEmail() %>" required>
                </div>

                <div class="form-group">
                    <label>Profile Picture:</label>
                    <% if (request.getAttribute("base64Image") != null) { %>
                    <div class="current-image">
                        <img src="data:image/jpeg;base64,<%= request.getAttribute("base64Image") %>" alt="Profile">
                        <span>Current Photo</span>
                    </div>
                    <% } %>
                    <input type="file" id="profilePicture" name="profilePicture" accept="image/*">
                    <small>Upload a new image (JPG/PNG, max 5MB)</small>
                </div>

                <div class="password-fields">
                    <h3>Change Password</h3>

                    <div class="form-group">
                        <label for="currentPassword">Current Password:</label>
                        <input type="password" id="currentPassword" name="currentPassword">
                    </div>

                    <div class="form-group">
                        <label for="newPassword">New Password:</label>
                        <input type="password" id="newPassword" name="newPassword">
                        <small>Leave blank to keep current password</small>
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword">Confirm New Password:</label>
                        <input type="password" id="confirmPassword" name="confirmPassword">
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn">Save Changes</button>
                    <a href="UserDashboardServlet" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>

<footer>
    <p>&copy; 2025 Itahari International College - Advanced Programming and Technologies</p>
    <p>Module Leader: Binay Koirala | Module Tutor: Sujan Subedi</p>
</footer>

<script>
    document.querySelector('form').addEventListener('submit', function (e) {
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const currentPassword = document.getElementById('currentPassword').value;

        if (newPassword || confirmPassword || currentPassword) {
            if (!currentPassword) {
                alert('Please enter your current password');
                e.preventDefault();
                return;
            }

            if (!newPassword) {
                alert('Please enter a new password');
                e.preventDefault();
                return;
            }

            if (newPassword.length < 8) {
                alert('Password must be at least 8 characters');
                e.preventDefault();
                return;
            }

            if (newPassword !== confirmPassword) {
                alert('New passwords do not match');
                e.preventDefault();
                return;
            }
        }
    });
</script>
</body>
</html>
