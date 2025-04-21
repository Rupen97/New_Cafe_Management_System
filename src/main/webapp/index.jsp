<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bean & Bloom</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background-color: #f5f5f5;
            color: #333;
            background-image: url('https://images.unsplash.com/photo-1447933601403-0c6688de566e?ixlib=rb-1.2.1&auto=format&fit=crop&w=1950&q=80');
            background-size: cover;
            background-position: center;
            background-attachment: fixed;
            position: relative;
        }

        body::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.4);
            z-index: -1;
        }

        header {
            background-color: rgba(92, 64, 51, 0.85);
            color: white;
            padding: 20px 0;
            text-align: center;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(5px);
        }

        header h1 {
            margin-bottom: 5px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: calc(100vh - 130px);
        }

        .role-selection {
            max-width: 1000px;
            margin: 0 auto;
            background-color: rgba(255, 255, 255, 0.92);
            border-radius: 15px;
            box-shadow: 0 5px 25px rgba(0, 0, 0, 0.15);
            padding: 40px;
            text-align: center;
            position: relative;
            overflow: hidden;
            backdrop-filter: blur(5px);
        }

        .role-selection h2 {
            color: #5c4033;
            margin-bottom: 20px;
        }

        .role-selection p {
            color: #666;
            margin-bottom: 30px;
        }

        .role-cards {
            display: flex;
            justify-content: center;
            gap: 30px;
            margin-top: 30px;
            flex-wrap: wrap;
        }

        .role-card {
            background-color: #fff;
            border: 1px solid #e0e0e0;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            padding: 25px;
            width: 250px;
            text-align: center;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            cursor: pointer;
        }

        .role-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
        }

        .role-icon {
            font-size: 60px;
            margin-bottom: 20px;
            color: #5c4033;
        }

        .role-card h3 {
            font-size: 22px;
            margin-bottom: 15px;
            color: #5c4033;
        }

        .role-card p {
            color: #666;
            font-size: 14px;
            margin-bottom: 20px;
        }

        .role-btn {
            display: inline-block;
            background-color: #5c4033;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }

        .role-btn:hover {
            background-color: #7d5a50;
        }

        /* New User Registration Card Specific Styling */
        #register-card {
            background: linear-gradient(135deg, #fff 0%, #f9f3ee 50%, #f5e9df 100%);
            border: 2px solid #e0d5c9;
            position: relative;
            transform: translateY(0);
            transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
            width: 280px; /* Slightly wider than other cards */
            padding: 30px 25px;
            z-index: 1;
        }

        #register-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: url('https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80');
            background-size: cover;
            background-position: center;
            opacity: 0.04;
            z-index: -1;
            border-radius: 8px;
        }

        #register-card:hover {
            transform: translateY(-15px) scale(1.03);
            box-shadow: 0 20px 40px rgba(165, 125, 93, 0.2);
            border-color: #d4b59e;
        }

        /* Removed 'New' badge */

        #register-card .role-icon {
            color: #a87b51;
            animation: pulse 2s infinite;
            font-size: 70px;
            margin-bottom: 25px;
            text-shadow: 0 2px 10px rgba(0,0,0,0.1);
            position: relative;
        }

        #register-card .role-icon::after {
            content: '+';
            position: absolute;
            top: 0;
            right: 65px;
            font-size: 30px;
            background-color: #5c4033;
            color: white;
            width: 30px;
            height: 30px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        }

        #register-card h3 {
            color: #5c4033;
            font-weight: bold;
            font-size: 26px;
            margin-bottom: 15px;
            position: relative;
            display: inline-block;
        }

        #register-card h3::after {
            content: '';
            position: absolute;
            bottom: -5px;
            left: 50%;
            transform: translateX(-50%);
            width: 50px;
            height: 2px;
            background-color: #a87b51;
        }

        #register-card p {
            color: #6d4c41;
            font-size: 16px;
            line-height: 1.6;
            margin-bottom: 30px;
        }

        #register-card .role-btn {
            background: linear-gradient(to right, #a87b51, #8c6544);
            box-shadow: 0 4px 15px rgba(0,0,0,0.15);
            border: none;
            padding: 14px 28px;
            font-size: 16px;
            letter-spacing: 1px;
            transition: all 0.3s ease;
            border-radius: 50px;
            position: relative;
            overflow: hidden;
            z-index: 1;
        }

        #register-card .role-btn::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 0%;
            height: 100%;
            background-color: #5c4033;
            transition: all 0.4s ease;
            z-index: -1;
        }

        #register-card .role-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(0,0,0,0.2);
            color: white;
        }

        #register-card .role-btn:hover::before {
            width: 100%;
        }

        @keyframes pulse {
            0% {
                transform: scale(1);
                opacity: 1;
            }
            50% {
                transform: scale(1.08);
                opacity: 0.8;
            }
            100% {
                transform: scale(1);
                opacity: 1;
            }
        }

        .side-image-left {
            position: absolute;
            left: -80px;
            top: 50%;
            transform: translateY(-50%);
            width: 200px;
            height: 100%;
            background-image: url('https://images.unsplash.com/photo-1509042239860-f550ce710b93?ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80');
            background-size: cover;
            background-position: center;
            opacity: 0.3;
            border-radius: 10px;
        }

        .side-image-right {
            position: absolute;
            right: -80px;
            top: 50%;
            transform: translateY(-50%);
            width: 200px;
            height: 100%;
            background-image: url('https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80');
            background-size: cover;
            background-position: center;
            opacity: 0.3;
            border-radius: 10px;
        }

        footer {
            background-color: rgba(92, 64, 51, 0.85);
            color: white;
            text-align: center;
            padding: 15px 0;
            position: fixed;
            bottom: 0;
            width: 100%;
            box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(5px);
        }

        footer p {
            margin: 5px 0;
        }
    </style>
</head>
<body>
<header>
    <h1>Bean & Bloom</h1>
</header>

<div class="container">
    <div class="role-selection">
        <div class="side-image-left"></div>
        <div class="side-image-right"></div>
        <h2>Welcome to Bean & Bloom</h2>
        <p>Please select your role to continue</p>

        <div class="role-cards">
            <div class="role-card" id="customer-card">
                <div class="role-icon">☕</div>
                <h3>Welcome Back</h3>
                <p>Access your account to place orders and view your favorites</p>
                <a href="./LoginServlet?role=customer" class="role-btn">Sign In</a>
            </div>

            <div class="role-card" id="register-card">
                <div class="role-icon">📝</div>
                <h3>New User?</h3>
                <p>Create a new account to start ordering from Bean & Bloom</p>
                <a href="./RegisterServlet" class="role-btn">Register Now</a>
            </div>
        </div>
    </div>
</div>

<footer>
    <p>&copy; 2025 Bean & Bloom </p>
</footer>

<script>
    // Add click event for the entire role card
    document.getElementById('customer-card').addEventListener('click', function(e) {
        if (!e.target.classList.contains('role-btn')) {
            window.location.href = './LoginServlet?role=customer';
        }
    });

    document.getElementById('register-card').addEventListener('click', function(e) {
        if (!e.target.classList.contains('role-btn')) {
            window.location.href = './RegisterServlet';
        }
    });
</script>
</body>
</html>