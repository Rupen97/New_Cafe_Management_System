<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.demo.models.UserModel" %>
<%@ page import="java.util.Base64" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit User - Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/edit-user.css">
</head>
<body>
<header>
    <h1>Edit User</h1>
    <p>Advanced Programming and Technologies - Itahari International College</p>
</header>

<div class="container">
    <div class="card">
        <a href="AdminDashboardServlet" class="btn">Back to Dashboard</a>

        <form action="UpdateUserServlet" method="post" enctype="multipart/form-data">
            <input type="hidden" name="id" value="${userToEdit.id}">

            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" id="name" name="name" value="${userToEdit.name}" required>
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${userToEdit.email}" required>
            </div>

            <div class="form-group">
                <label for="role">Role:</label>
                <select id="role" name="role" required>
                    <option value="admin" ${userToEdit.role == 'admin' ? 'selected' : ''}>Admin</option>
                    <option value="user" ${userToEdit.role == 'user' ? 'selected' : ''}>User</option>
                </select>
            </div>

            <div class="form-group">
                <label>Current Profile Picture:</label>
                <img src="data:image/jpeg;base64,${base64Image}" alt="Profile Picture"
                     onerror="this.src='${pageContext.request.contextPath}/assets/images/default-profile.svg'"
                     width="100" height="100">
            </div>

            <div class="form-group">
                <label for="image">Change Profile Picture:</label>
                <input type="file" id="image" name="image" accept="image/*">
            </div>

            <button type="submit" class="btn">Update User</button>
        </form>
    </div>
</div>

<footer>
    <p>&copy; 2025 Itahari International College - Advanced Programming and Technologies</p>
</footer>
</body>
</html>