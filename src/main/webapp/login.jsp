<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Health Care-Login</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="header">
        <h1 class="header-title" >Health Care</h1>
    </div>
    <br><br>
    <div class="main-content">
    <div class="container">
        <h2>Login</h2>
        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
            <div class="error-message"><%= error %></div>
        <% } %>
        <form action="LoginServlet" method="post" class="login-form">
            <div class="form-row">
                <label for="userid">User ID:</label>
                <input type="text" id="userId" name="userId" required>
            </div>
            <div class="form-row">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <input type="submit" value="Login">
        </form>
        <div class="register-link">
            New user? <a href="register.jsp">Register here</a>
        </div>
    </div>
    </div>>
</body>
</html>
 