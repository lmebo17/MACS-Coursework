<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Store.Shop" %>
<%@ page import="Store.Product" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>

    <title>Student Store</title>
</head>
<body>
<h1>Student Store</h1>
<p>Items available:</p>

<ul>
    <%
        Shop shop = (Shop) application.getAttribute(Shop.NAME);
        ArrayList<Product> productList = shop.listAllProducts();
        for (Product product : productList) {
            String id = product.getProductID();
            String name = product.getProductName();
    %>
    <li><a href="ProductPage.jsp?id=<%=id%>"><%=name%></a></li>
    <% } %>
</ul>
</body>
</html>