package services;

import models.Appointment;
import models.Doctor;
import models.Patient;
import exceptions.InvalidInputException;
import exceptions.AppointmentConflictException;

import java.util.List;

public interface IClinicService {
    void addPatient(String name, String ageStr, String phoneNumber) throws InvalidInputException;
    void updatePatient(String id, String name, String ageStr, String phoneNumber) throws InvalidInputException;
    void deletePatient(String id) throws InvalidInputException;
    
    void addDoctor(String name, String specialty, String phoneNumber) throws InvalidInputException;
    void updateDoctor(String id, String name, String specialty, String phoneNumber) throws InvalidInputException;
    void deleteDoctor(String id) throws InvalidInputException;
    
    void bookAppointment(String patientId, String doctorId, String dateAndTime) throws InvalidInputException, AppointmentConflictException;
    void cancelAppointment(String appointmentId) throws InvalidInputException;
    void rescheduleAppointment(String appointmentId, String newDateAndTime) throws InvalidInputException, AppointmentConflictException;
    
    List<Appointment> getUpcomingAppointmentsForDoctor(String doctorId);
    List<Appointment> getVisitHistoryForPatient(String patientId);
    
    List<Patient> getAllPatients();
    List<Doctor> getAllDoctors();
    List<Appointment> getAllAppointments();
    
    void loadData();
}
