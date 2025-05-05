<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>My Orders - Brew & Beans</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer-dashboard.css">
    <style>
        .orders-container {
            padding: 2rem;
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .order-card {
            border: 1px solid #eee;
            border-radius: 8px;
            padding: 1.5rem;
            margin-bottom: 1.5rem;
            transition: all 0.3s ease;
        }
        .order-card:hover {
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            transform: translateY(-3px);
        }
        .order-header {
            display: flex;
            justify-content: space-between;
            border-bottom: 1px solid #eee;
            padding-bottom: 1rem;
            margin-bottom: 1rem;
        }
        .order-items {
            margin-top: 1rem;
        }
        .item-row {
            display: flex;
            justify-content: space-between;
            padding: 0.5rem 0;
            border-bottom: 1px dashed #eee;
        }
        .status {
            padding: 0.3rem 0.8rem;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: bold;
        }
        .status-PENDING { background: #FFF3CD; color: #856404; }
        .status-COMPLETED { background: #D4EDDA; color: #155724; }
        .status-CANCELLED { background: #F8D7DA; color: #721C24; }
        .empty-orders {
            text-align: center;
            padding: 3rem;
            color: #6c757d;
        }
        .order-actions {
            display: flex;
            gap: 1rem;
            margin-top: 1rem;
        }
        .btn {
            padding: 0.5rem 1rem;
            border-radius: 4px;
            text-decoration: none;
            font-weight: 500;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
        }
        .btn-primary {
            background-color: #6c757d;
            color: white;
            border: 1px solid #6c757d;
        }
        .btn-danger {
            background-color: #dc3545;
            color: white;
            border: 1px solid #dc3545;
        }
        .btn-outline {
            background-color: transparent;
            color: #6c757d;
            border: 1px solid #6c757d;
        }
        .btn-sm {
            padding: 0.25rem 0.5rem;
            font-size: 0.875rem;
        }
    </style>
</head>
<body>
<div class="dashboard-container">
    <!-- Include your existing sidebar -->
    <jsp:include page="/WEB-INF/views/partials/sidebar.jsp" />

    <div class="main-content">
        <header class="content-header">
            <h2>My Orders</h2>
            <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">
                <i class="fas fa-plus"></i> New Order
            </a>
        </header>

        <div class="orders-container">
            <c:choose>
                <c:when test="${empty orders}">
                    <div class="empty-orders">
                        <i class="fas fa-clipboard-list fa-3x" style="color:#eee; margin-bottom:1rem;"></i>
                        <h3>No orders yet</h3>
                        <p>You haven't placed any orders with us yet.</p>
                        <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">
                            Browse Our Menu
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${orders}" var="order">
                        <div class="order-card">
                            <div class="order-header">
                                <div>
                                    <h3>Order #${order.id}</h3>
                                    <small class="text-muted">
                                        <fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy hh:mm a"/>
                                    </small>
                                </div>
                                <div>
                                    <span class="status status-${order.status}">
                                            ${order.status}
                                    </span>
                                </div>
                            </div>

                            <div class="order-summary">
                                <div class="summary-row">
                                    <span>Items:</span>
                                    <span>${order.quantity}</span>
                                </div>
                                <div class="summary-row">
                                    <span>Total:</span>
                                    <span>$<fmt:formatNumber value="${order.totalAmount}" minFractionDigits="2"/></span>
                                </div>
                            </div>


                            <div class="order-actions">
                                <c:if test="${order.status == 'PENDING'}">

                                    <form action="${pageContext.request.contextPath}/cancel-order" method="post" style="display: inline;">
                                        <input type="hidden" name="orderId" value="${order.id}">
                                        <button type="submit" class="btn btn-danger btn-sm">
                                            <i class="fas fa-times"></i> Cancel
                                        </button>
                                    </form>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/order-details?id=${order.id}"
                                   class="btn btn-outline btn-sm">
                                    <i class="fas fa-eye"></i> View Details
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>