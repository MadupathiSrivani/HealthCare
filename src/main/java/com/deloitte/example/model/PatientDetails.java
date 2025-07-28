package com.deloitte.example.model;

import java.sql.Date;

public class PatientDetails {
    private int patientId;
    private String userid;
    private String patientType;
    private Date dob;
    private String disease;
    private String previousHistory;
    private String doctorType;
    private Date admittedDate;

    // Getters and setters...
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public String getUserid() { return userid; }
    public void setUserid(String userid) { this.userid = userid; }
    public String getPatientType() { return patientType; }
    public void setPatientType(String patientType) { this.patientType = patientType; }
    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }
    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }
    public String getPreviousHistory() { return previousHistory; }
    public void setPreviousHistory(String previousHistory) { this.previousHistory = previousHistory; }
    public String getDoctorType() { return doctorType; }
    public void setDoctorType(String doctorType) { this.doctorType = doctorType; }
    public Date getAdmittedDate() { return admittedDate; }
    public void setAdmittedDate(Date admittedDate) { this.admittedDate = admittedDate; }
}
