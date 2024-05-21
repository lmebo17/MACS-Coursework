<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Invalid Information</title>
</head>
<body>
<h1>Invalid Credentials</h1>
<p>username or password is incorrect. Please try again.</p>
<form action="LoginServlet" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="name" required><br>
    <label for="password">Password:</label>
    <input type="password" id="password" name="pw" required><br>
    <input type="submit" value="Login">
</form>
<p><a href="Create.jsp">Create a New Account</a></p>
</body>
</html>