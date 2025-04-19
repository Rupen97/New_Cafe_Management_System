<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Café Manager</title>
    <link rel="stylesheet" href="./assets/css/index.css">
</head>
<body>
<div class="container">
    <header>
        <div class="logo">
            <h1>Café Manager</h1>
            <div class="coffee-cup">
                <div class="steam"></div>
            </div>
        </div>
        <nav>
            <a href="./LoginServlet" class="btn">Login</a>
            <a href="./RegisterServlet" class="btn">Register</a>
        </nav>
    </header>

    <section class="hero">
        <h2>Manage Your Café With Ease</h2>
        <p>A simple solution for your café management needs</p>
    </section>

    <section class="features">
        <h2>Features</h2>
        <div class="feature-grid">
            <div class="feature-card">
                <div class="feature-icon">📊</div>
                <h3>Inventory Management</h3>
                <p>Track coffee beans, milk, and other supplies</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon">💰</div>
                <h3>Sales Tracking</h3>
                <p>Monitor daily, weekly, and monthly sales</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon">👥</div>
                <h3>Staff Scheduling</h3>
                <p>Manage barista shifts and schedules</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon">🧾</div>
                <h3>Order Processing</h3>
                <p>Take and fulfill customer orders efficiently</p>
            </div>
        </div>
    </section>

    <footer>
        <p>© 2025 Café Manager. All rights reserved.</p>
    </footer>
</div>
</body>
</html>