<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Store.Shop, Store.Product" %>
<!DOCTYPE html>
<%
  Shop shop = (Shop) application.getAttribute(Shop.NAME);
  Product product = shop.getProductWithID(request.getParameter("id"));
%>
<html>
<head>
  <title><%=product.getProductName()%></title>
</head>
<body>
<h1><%=product.getProductName()%></h1>
<img alt="<%=product.getProductName()%>" src="<c:url value='/hw5/Shop/src/main/webapp/store-images/'/>${product.getImage()}" />

<form action="CartServlet" method="post">
  $<%= product.getPrice() %> <input name="productID" type="hidden" value="<%=product.getProductID()%>"/>
  <input type="submit" value="Add to Cart"/>
</form>
</body>
</html>