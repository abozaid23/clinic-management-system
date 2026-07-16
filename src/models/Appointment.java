package models;

import java.io.Serializable;

public class Appointment implements Serializable {
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String dateAndTime;
    private String status;

    public Appointment(String appointmentId, String patientId, String doctorId, String dateAndTime, String status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateAndTime = dateAndTime;
        this.status = status;
    }

    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getDateAndTime() { return dateAndTime; }
    public String getStatus() { return status; }

    public void setDateAndTime(String dateAndTime) { this.dateAndTime = dateAndTime; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return "Appt ID: " + appointmentId + " | Patient: " + patientId + " | Dr: " + doctorId + " | Date: " + dateAndTime + " | Status: " + status;
    }
}
