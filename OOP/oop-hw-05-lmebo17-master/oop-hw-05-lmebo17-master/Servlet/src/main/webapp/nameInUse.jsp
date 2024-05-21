<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Username Already Taken</title>
</head>
<body>
<h1>The username "<%= request.getParameter("name") %>" is being used</h1>
<p>Please, choose another username and password.</p>
<form action="RegisterServlet" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="name" required><br>
    <label for="password">Password:</label>
    <input type="password" id="password" name="pw" required><br>
    <input type="submit" value="Create Account">
</form>
</body>
</html>