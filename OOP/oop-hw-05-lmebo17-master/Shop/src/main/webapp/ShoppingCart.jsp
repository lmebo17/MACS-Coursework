<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Store.Cart" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="Store.Product" %>
<!DOCTYPE html>

<html>
<head>
  <title> SHOPPING CART </title>
</head>
<body>
<h1> SHOPPING CART </h1>

<form action="CartServlet" method="post">
    <ul>
    <%
        Cart cart = (Cart)request.getSession().getAttribute("CART");
        HashMap<Product, Integer> products = cart.listCart();
        for(Product product : products.keySet()){
            int quantity = products.get(product);
            out.println(
            "<li><p>" +
              "<input type = \"text\" value=" + quantity + " name=" + product.getProductID() + " />"+
              " " + product.getProductName() + " " + product.getPrice() +
              "</p></li>"
            );
        }

    %>
        Total: <%= Math.round(cart.totalPrice() * 100)/100.0 %> <input type="submit" value="UpdateCart">
    </ul>

</form>
<a href="index.jsp"> Continue Shopping</a>
</body>
</html>