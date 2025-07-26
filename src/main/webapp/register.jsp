<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>User Registration</title>
    <link rel="stylesheet" href="style.css">
    
</head>
<body>
<div class="header">
    <h1 class="header-title">Expense Management System</h1>
</div>
<br><br>
 	<div class="main-content">
    <div class="form-container">
        <h2>Register</h2>
 
        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>
        <% if (request.getAttribute("message") != null) { %>
            <div class="message"><%= request.getAttribute("message") %></div>
        <% } %>
<form action="RegisterServlet" method="post">
    First Name: <input type="text" name="firstName" required><br>
    Last Name: <input type="text" name="lastName" required><br>
    DOB: <input type="date" name="dob" required><br>
    Mobile: <input type="text" name="mobile" required maxlength="10" pattern="\d{10}" title="Enter exactly 10 digits"><br>
    ID Proof: <input type="text" name="idProof" required maxlength="12" pattern="[A-Za-z0-9]{12}" title="Enter exactly 12 alphanumeric characters"><br>
    <input type="submit" value="Register">
</form>
  
 
        <div style="text-align:center; margin-top:10px;">
            <a href="login.jsp">Already have an account? Login</a>
        </div>
    </div>
    </div>
</body>
</html>



















