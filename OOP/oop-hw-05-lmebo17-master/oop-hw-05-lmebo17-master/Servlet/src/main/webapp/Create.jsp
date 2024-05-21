<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Account</title>
</head>
<body>
<h1>Create New Account</h1>
<p>Please provide a desired username and password.</p>
<form action="RegisterServlet" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="name" required><br>
    <label for="password">Password:</label>
    <input type="password" id="password" name="pw" required><br>
    <input type="submit" value="Create Account">
</form>
</body>
</html>