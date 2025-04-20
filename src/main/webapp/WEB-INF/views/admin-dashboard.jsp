<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            margin: 0;
            font-family: 'Segoe UI', sans-serif;
            background-color: #f6e9da;
        }
        .sidebar {
            width: 250px;
            height: 100vh;
            background-color: #f8f9fa;
            padding: 20px;
            position: fixed;
        }
        .sidebar a {
            display: block;
            margin-bottom: 15px;
            color: #000;
            text-decoration: none;
        }
        .main {
            margin-left: 250px;
            padding: 30px;
        }
        .card-box {
            border-radius: 10px;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0 0 5px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h6>Admin Panel</h6>
    <a href="dashboard.jsp"><i class="bi bi-house-door"></i> Dashboard</a>
    <a href="menu.jsp"><i class="bi bi-card-list"></i> Menu</a>
    <a href="orders.jsp"><i class="bi bi-bag-check"></i> Orders</a>
</div>

<!-- Main Content -->
<div class="main">
    <h2 class="fw-bold mb-4">Dashboard</h2>
    <div class="row g-4">
        <div class="col-md-3">
            <div class="card-box">
                <h6>Total Orders <i class="bi bi-receipt ms-2"></i></h6>
                <h4 class="fw-bold mt-2">0 orders</h4>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card-box">
                <h6>Total Revenue <i class="bi bi-currency-dollar ms-2"></i></h6>
                <h4 class="fw-bold mt-2">Rs. 0</h4>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card-box">
                <h6>Active Menu <i class="bi bi-cup-hot ms-2"></i></h6>
                <h4 class="fw-bold mt-2">0 items</h4>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card-box">
                <h6>Total Customers <i class="bi bi-people ms-2"></i></h6>
                <h4 class="fw-bold mt-2">0</h4>
            </div>
        </div>
    </div>
</div>

</body>
</html>
