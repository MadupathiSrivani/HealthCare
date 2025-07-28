package com.deloitte.example.controller;

import com.deloitte.example.model.PatientDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import com.deloitte.example.util.DBConnection; // Adjust package as needed

@WebServlet("/output")
public class OutputServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        PatientDetails details = (PatientDetails) session.getAttribute("details");
        String userId = (String) session.getAttribute("userId");

        if (details == null || userId == null) {
            response.sendRedirect("patient_details.jsp");
            return;
        }

        // Doctor names and fees
        Map<String, String> doctorNames = new HashMap<>();
        doctorNames.put("Cardiologist", "Dr. Smith");
        doctorNames.put("Nephrologist", "Dr. Johnson");
        doctorNames.put("General", "Dr. Williams");
        doctorNames.put("Pediatrician", "Dr. Brown");

        Map<String, Double> doctorFees = new HashMap<>();
        doctorFees.put("Cardiologist", 1000.0);
        doctorFees.put("Nephrologist", 900.0);
        doctorFees.put("General", 500.0);
        doctorFees.put("Pediatrician", 700.0);

        List<String> medicines = Arrays.asList("Paracetamol", "Ibuprofen", "Amoxicillin");

        // Bill calculation
        double gstRate = 0.1297;
        String doctorType = details.getDoctorType();
        String patientType = details.getPatientType();
        double consultingFee = doctorFees.getOrDefault(doctorType, 500.0);
        double billAmount = 0.0;
        double gst = 0.0;
        double bedCharges = 0.0;
        double medicalBill = 0.0; // Set as needed
        int daysAdmitted = 1;

        if ("IP".equalsIgnoreCase(patientType) && details.getAdmittedDate() != null) {
            java.sql.Date admittedDate = details.getAdmittedDate();
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            long diff = today.getTime() - admittedDate.getTime();
            daysAdmitted = (int) (diff / (1000 * 60 * 60 * 24)) + 1;
            bedCharges = daysAdmitted * 1500.0;
            // Set medicalBill as needed (fixed, or from another source)
            double subtotal = consultingFee + medicalBill + bedCharges;
            gst = subtotal * gstRate;
            billAmount = subtotal + gst;
        } else {
            double subtotal = consultingFee;
            gst = subtotal * gstRate;
            billAmount = subtotal + gst;
        }

        // Set all attributes expected by output.jsp
        request.setAttribute("patientName", userId); // Replace with actual name if available
        request.setAttribute("disease", details.getDisease());
        request.setAttribute("previousHistory", details.getPreviousHistory());
        request.setAttribute("doctorName", doctorNames.getOrDefault(doctorType, "Dr. Default"));
        request.setAttribute("medicines", medicines);
        request.setAttribute("consultingFee", consultingFee);
        request.setAttribute("bedCharges", bedCharges);
        request.setAttribute("medicalBill", medicalBill);
        request.setAttribute("gst", String.format("%.2f", gst));
        request.setAttribute("totalBill", String.format("%.2f", billAmount));
        request.setAttribute("patientType", patientType);

        request.getRequestDispatcher("output.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Retrieve userId from request
        String userId = request.getParameter("userId");

        // 2. Variables to store DB values
        String patientName = "";
        String disease = "";
        String previousHistory = "";
        String doctorType = "";
        String patientType = "";
        String admittedDate = "";
        double medicalBill = 0.0;
        int daysAdmitted = 1; // Default to 1 if not IP

        // 3. Fetch patient details from DB using DBUtil
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT pd.*, u.name as patientName FROM PatientDetails pd JOIN User u ON pd.userId = u.userId WHERE pd.userId = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        patientName = rs.getString("patientName");
                        disease = rs.getString("disease");
                        previousHistory = rs.getString("previous_history");
                        doctorType = rs.getString("doctor_type");
                        patientType = rs.getString("patient_type");
                        admittedDate = rs.getString("admitted_date");
                        // If you have a medical_bill column, use it; else, set as needed
                        // medicalBill = rs.getDouble("medical_bill");
                    }
                }
            }
            // If IP, calculate days admitted (difference between admitted_date and today)
            if ("IP".equalsIgnoreCase(patientType) && admittedDate != null) {
                String sql2 = "SELECT DATEDIFF(CURDATE(), ?) + 1 AS daysAdmitted";
                try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                    ps2.setString(1, admittedDate);
                    try (ResultSet rs2 = ps2.executeQuery()) {
                        if (rs2.next()) {
                            daysAdmitted = rs2.getInt("daysAdmitted");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 4. Predefine doctor names and consulting fees
        Map<String, String> doctorNames = new HashMap<>();
        doctorNames.put("Cardiologist", "Dr. Smith");
        doctorNames.put("Nephrologist", "Dr. Johnson");
        doctorNames.put("General", "Dr. Williams");
        doctorNames.put("Pediatrician", "Dr. Brown");

        Map<String, Double> doctorFees = new HashMap<>();
        doctorFees.put("Cardiologist", 1000.0);
        doctorFees.put("Nephrologist", 900.0);
        doctorFees.put("General", 500.0);
        doctorFees.put("Pediatrician", 700.0);

        // 5. Predefine medicines
        List<String> medicines = Arrays.asList("Paracetamol", "Ibuprofen", "Amoxicillin");

        // 6. Bill calculation
        double gstRate = 0.1297;
        double consultingFee = doctorFees.getOrDefault(doctorType, 500.0);
        double billAmount = 0.0;
        double gst = 0.0;
        double bedCharges = 0.0;

        if ("IP".equalsIgnoreCase(patientType)) {
            bedCharges = daysAdmitted * 1500.0; // Example bed charge per day
            // You may want to set medicalBill based on your business logic or another query
            double subtotal = consultingFee + medicalBill + bedCharges;
            gst = subtotal * gstRate;
            billAmount = subtotal + gst;
        } else {
            double subtotal = consultingFee;
            gst = subtotal * gstRate;
            billAmount = subtotal + gst;
        }

        // 7. Set attributes for JSP
        request.setAttribute("patientName", patientName);
        request.setAttribute("disease", disease);
        request.setAttribute("previousHistory", previousHistory);
        request.setAttribute("doctorName", doctorNames.getOrDefault(doctorType, "Dr. Default"));
        request.setAttribute("medicines", medicines);
        request.setAttribute("consultingFee", consultingFee);
        request.setAttribute("bedCharges", bedCharges);
        request.setAttribute("medicalBill", medicalBill);
        request.setAttribute("gst", String.format("%.2f", gst));
        request.setAttribute("totalBill", String.format("%.2f", billAmount));
        request.setAttribute("patientType", patientType);

        // 8. Forward to JSP
        request.getRequestDispatcher("output.jsp").forward(request, response);
    }
}
