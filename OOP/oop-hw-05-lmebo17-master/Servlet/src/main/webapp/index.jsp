<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
</head>
<body>
<h1>Welcome to my Website</h1>
<p>Please log in.</p>
<form action="LoginServlet" method="post">
    <label for="username">User Name:</label>
    <input type="text" id="username" name="name" required><br>
    <label for="password">Password:</label>
    <input type="password" id="password" name="pw" required><br>
    <input type="submit" value="Login">
</form>
<p><a href="Create.jsp">Create a New Account</a></p>
</body>
</html>