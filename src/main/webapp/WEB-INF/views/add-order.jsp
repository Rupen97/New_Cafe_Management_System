<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Confirm Order</title>
</head>
<body>
<h2>Confirm Order</h2>

<c:if test="${not empty item}">
    <form action="AddOrderServlet" method="get">
        <input type="hidden" name="action" value="add" />
        <input type="hidden" name="itemId" value="${item.id}" />

        <p><strong>Item:</strong> ${item.name}</p>
        <p><strong>Description:</strong> ${item.description}</p>
        <p><strong>Price (per unit):</strong> $${item.price}</p>
        <p><strong>Status:</strong> ${item.status}</p>

        <label for="quantity"><strong>Quantity:</strong></label>
        <input type="number" name="quantity" id="quantity" value="1" min="1" required />

        <br><br>
        <button type="submit">Place Order</button>
        <a href="CustomerItemsServlet">Cancel</a>
    </form>
</c:if>

<c:if test="${empty item}">
    <p>Item not found.</p>
    <a href="CustomerItemsServlet">Back to Items</a>
</c:if>
</body>
</html>
