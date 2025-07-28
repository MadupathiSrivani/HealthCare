package com.deloitte.example.controller;

import com.deloitte.example.dao.PatientDetailsDAO;
import com.deloitte.example.model.PatientDetails;
import com.deloitte.example.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet("/addAppointment")
public class AddDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to login.jsp when GET request is made
        request.getRequestDispatcher("patient_details.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String userid = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userid == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String patientType = request.getParameter("patient_type");
        Date dob = Date.valueOf(request.getParameter("dob"));
        String disease = request.getParameter("disease");
        String previousHistory = request.getParameter("previous_history");
        String doctorType = request.getParameter("doctor_type");
        String admittedDateStr = request.getParameter("admitted_date");
        Date admittedDate = null;
        if ("IP".equals(patientType) && admittedDateStr != null && !admittedDateStr.isEmpty()) {
            admittedDate = Date.valueOf(admittedDateStr);
        }

        PatientDetails details = new PatientDetails();
        details.setUserid(userid);
        details.setPatientType(patientType);
        details.setDob(dob);
        details.setDisease(disease);
        details.setPreviousHistory(previousHistory);
        details.setDoctorType(doctorType);
        details.setAdmittedDate(admittedDate);

        try (Connection conn = DBConnection.getConnection()) {
            PatientDetailsDAO dao = new PatientDetailsDAO(conn);
            dao.addPatientDetails(details);
            session.setAttribute("details", details);
            response.sendRedirect("output"); 
        } catch (SQLException e) {
            throw new ServletException("DB error", e);
        }
    }
}
