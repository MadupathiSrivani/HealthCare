package com.deloitte.example.dao;

import com.deloitte.example.model.PatientDetails;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDetailsDAO {
    private Connection conn;

    public PatientDetailsDAO(Connection conn) {
        this.conn = conn;
    }

    // Add new patient details
    public void addPatientDetails(PatientDetails details) throws SQLException {
    	String sql = "INSERT INTO PatientDetails (userId, patient_type, dob, disease, previous_history, doctor_type, admitted_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
    	try (PreparedStatement stmt = conn.prepareStatement(sql)) {
    	    stmt.setString(1, details.getUserid());
    	    stmt.setString(2, details.getPatientType());
    	    stmt.setDate(3, details.getDob());
    	    stmt.setString(4, details.getDisease());
    	    stmt.setString(5, details.getPreviousHistory());
    	    stmt.setString(6, details.getDoctorType());
    	    if (details.getAdmittedDate() != null) {
    	        stmt.setDate(7, details.getAdmittedDate());
    	    } else {
    	        stmt.setNull(7, java.sql.Types.DATE);
    	    }
    	    stmt.executeUpdate();
    	}

    }

    // Get all patient details for a user
    public List<PatientDetails> getPatientDetailsByUser(String userid) throws SQLException {
        List<PatientDetails> list = new ArrayList<>();
        String sql = "SELECT * FROM PatientDetails WHERE userId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userid);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PatientDetails pd = new PatientDetails();
                    pd.setPatientId(rs.getInt("patient_id"));
                    pd.setUserid(rs.getString("userId"));
                    pd.setPatientType(rs.getString("patient_type"));
                    pd.setDob(rs.getDate("dob"));
                    pd.setDisease(rs.getString("disease"));
                    pd.setPreviousHistory(rs.getString("previous_history"));
                    pd.setDoctorType(rs.getString("doctor_type"));
                    pd.setAdmittedDate(rs.getDate("admitted_date"));
                    list.add(pd);
                }
            }
        }
        return list;
    }

    // Optionally: Get a single patient record by patient_id
    public PatientDetails getPatientDetailsById(int patientId) throws SQLException {
        String sql = "SELECT * FROM PatientDetails WHERE patient_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PatientDetails pd = new PatientDetails();
                    pd.setPatientId(rs.getInt("patient_id"));
                    pd.setUserid(rs.getString("userId"));
                    pd.setPatientType(rs.getString("patient_type"));
                    pd.setDob(rs.getDate("dob"));
                    pd.setDisease(rs.getString("disease"));
                    pd.setPreviousHistory(rs.getString("previous_history"));
                    pd.setDoctorType(rs.getString("doctor_type"));
                    pd.setAdmittedDate(rs.getDate("admitted_date"));
                    return pd;
                }
            }
        }
        return null;
    }

    // Optionally: Delete a patient record
    public void deletePatientDetails(int patientId) throws SQLException {
        String sql = "DELETE FROM PatientDetails WHERE patient_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            stmt.executeUpdate();
        }
    }
}
