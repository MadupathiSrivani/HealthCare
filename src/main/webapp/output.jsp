<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Patient Bill Output</title>
    <link rel="stylesheet" href="style.css">
    
</head>
<body>
    <h2>
        Hello, Mr./Mrs. <%= request.getAttribute("patientName") %> is having <%= request.getAttribute("disease") %>
        with previous history of <%= request.getAttribute("previousHistory") %> and are consulting
        <%= request.getAttribute("doctorName") %>.
    </h2>
    
    <h3>Following are the medication you have to follow:</h3>
    <ul>
        <%
            List<String> meds = (List<String>) request.getAttribute("medicines");
            if (meds != null) {
                for (String med : meds) {
        %>
            <li><%= med %></li>
        <%
                }
            }
        %>
    </ul>

    <h3>Bill Details:</h3>
    <ul>
        <li>Consulting Fees: ₹<%= request.getAttribute("consultingFee") %></li>
        <%
            String patientType = (String) request.getAttribute("patientType");
            if ("IP".equalsIgnoreCase(patientType)) { 
        %>
            <li>Bed Charges: ₹<%= request.getAttribute("bedCharges") %></li>
            <li>Medical Bill: ₹<%= request.getAttribute("medicalBill") %></li>
        <%
            }
        %>
        <li>GST (12.97%): ₹<%= request.getAttribute("gst") %></li>
        <li><b>Total Bill Amount: ₹<%= request.getAttribute("totalBill") %></b></li>
    </ul>
</body>
</html>
