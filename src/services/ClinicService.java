package services;

import models.Appointment;
import models.Doctor;
import models.Patient;
import exceptions.InvalidInputException;
import exceptions.AppointmentConflictException;
import utils.DatabaseHelper;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ClinicService implements IClinicService {
    private List<Patient> patients = new ArrayList<>();
    private List<Doctor> doctors = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();
    private Random random = new Random();

    public ClinicService() {
        DatabaseHelper.initializeDatabase();
        loadData();
    }

    @Override
    public void addPatient(String name, String ageStr, String phoneNumber) throws InvalidInputException {
        if (name == null || name.trim().isEmpty())
            throw new InvalidInputException("Name cannot be empty.");
        if (phoneNumber == null || phoneNumber.trim().isEmpty())
            throw new InvalidInputException("Phone number cannot be empty.");

        int age;
        try {
            age = Integer.parseInt(ageStr.trim());
            if (age < 0 || age > 150)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Age must be a valid number between 0 and 150.");
        }

        int randomId = 1000 + random.nextInt(9000);
        String patientId = "P" + randomId;

        Patient p = new Patient(patientId, name, age, phoneNumber);
        patients.add(p);
        DatabaseHelper.addPatient(p);

    }

    @Override
    public void updatePatient(String id, String name, String ageStr, String phoneNumber) throws InvalidInputException {
        if (name == null || name.trim().isEmpty())
            throw new InvalidInputException("Name cannot be empty.");
        if (phoneNumber == null || phoneNumber.trim().isEmpty())
            throw new InvalidInputException("Phone number cannot be empty.");

        int age;
        try {
            age = Integer.parseInt(ageStr.trim());
            if (age < 0 || age > 150)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Age must be a valid number between 0 and 150.");
        }

        Patient patient = patients.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
        if (patient == null)
            throw new InvalidInputException("Patient not found.");

        patient.setName(name);
        patient.setAge(age);
        patient.setPhoneNumber(phoneNumber);
        DatabaseHelper.updatePatient(patient);

    }

    @Override
    public void deletePatient(String id) throws InvalidInputException {
        boolean removed = patients.removeIf(p -> p.getId().equals(id));
        if (!removed)
            throw new InvalidInputException("Patient not found.");
        appointments.removeIf(a -> a.getPatientId().equals(id));
        DatabaseHelper.deletePatient(id);
        DatabaseHelper.deleteAppointmentsByPatient(id);

    }

    @Override
    public void addDoctor(String name, String specialty, String phoneNumber) throws InvalidInputException {
        if (name == null || name.trim().isEmpty())
            throw new InvalidInputException("Name cannot be empty.");
        if (specialty == null || specialty.trim().isEmpty())
            throw new InvalidInputException("Specialty cannot be empty.");
        if (phoneNumber == null || phoneNumber.trim().isEmpty())
            throw new InvalidInputException("Phone number cannot be empty.");

        int randomId = 1000 + random.nextInt(9000);
        String doctorId = "D" + randomId;

        Doctor d = new Doctor(doctorId, name, specialty, phoneNumber);
        doctors.add(d);
        DatabaseHelper.addDoctor(d);

    }

    @Override
    public void updateDoctor(String id, String name, String specialty, String phoneNumber)
            throws InvalidInputException {
        if (name == null || name.trim().isEmpty())
            throw new InvalidInputException("Name cannot be empty.");
        if (specialty == null || specialty.trim().isEmpty())
            throw new InvalidInputException("Specialty cannot be empty.");
        if (phoneNumber == null || phoneNumber.trim().isEmpty())
            throw new InvalidInputException("Phone number cannot be empty.");

        Doctor doctor = doctors.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
        if (doctor == null)
            throw new InvalidInputException("Doctor not found.");

        doctor.setName(name);
        doctor.setSpecialty(specialty);
        doctor.setPhoneNumber(phoneNumber);
        DatabaseHelper.updateDoctor(doctor);

    }

    @Override
    public void deleteDoctor(String id) throws InvalidInputException {
        boolean removed = doctors.removeIf(d -> d.getId().equals(id));
        if (!removed)
            throw new InvalidInputException("Doctor not found.");
        appointments.removeIf(a -> a.getDoctorId().equals(id));
        DatabaseHelper.deleteDoctor(id);
        DatabaseHelper.deleteAppointmentsByDoctor(id);

    }

    @Override
    public void bookAppointment(String patientId, String doctorId, String dateAndTime)
            throws InvalidInputException, AppointmentConflictException {
        if (patientId == null || doctorId == null || dateAndTime == null || dateAndTime.trim().isEmpty()) {
            throw new InvalidInputException("All required fields must be provided.");
        }

        for (Appointment appt : appointments) {
            if (appt.getDoctorId().equals(doctorId) && appt.getDateAndTime().equals(dateAndTime)
                    && appt.getStatus().equals("Scheduled")) {
                throw new AppointmentConflictException(
                        "Doctor already has an appointment booked at this specific time.");
            }
        }

        int randomId = 10000 + random.nextInt(90000);
        String appointmentId = "A" + randomId;
        Appointment appt = new Appointment(appointmentId, patientId, doctorId, dateAndTime, "Scheduled");
        appointments.add(appt);
        DatabaseHelper.addAppointment(appt);

    }

    @Override
    public void cancelAppointment(String appointmentId) throws InvalidInputException {
        Appointment appt = findAppointment(appointmentId);
        if (appt != null) {
            appt.setStatus("Cancelled");
            DatabaseHelper.updateAppointment(appt);

        } else {
            throw new InvalidInputException("Appointment ID not found.");
        }
    }

    @Override
    public void rescheduleAppointment(String appointmentId, String newDateAndTime)
            throws InvalidInputException, AppointmentConflictException {
        if (newDateAndTime == null || newDateAndTime.trim().isEmpty()) {
            throw new InvalidInputException("New date and time cannot be blank.");
        }
        Appointment appt = findAppointment(appointmentId);
        if (appt != null) {
            for (Appointment a : appointments) {
                if (a.getDoctorId().equals(appt.getDoctorId()) && a.getDateAndTime().equals(newDateAndTime)
                        && a.getStatus().equals("Scheduled")) {
                    throw new AppointmentConflictException(
                            "Doctor already has an appointment booked at this new time.");
                }
            }
            appt.setDateAndTime(newDateAndTime);
            appt.setStatus("Scheduled");
            DatabaseHelper.updateAppointment(appt);

        } else {
            throw new InvalidInputException("Appointment ID not found.");
        }
    }

    private Appointment findAppointment(String id) {
        for (Appointment a : appointments) {
            if (a.getAppointmentId().equals(id))
                return a;
        }
        return null;
    }

    @Override
    public List<Appointment> getUpcomingAppointmentsForDoctor(String doctorId) {
        return appointments.stream()
                .filter(a -> a.getDoctorId().equals(doctorId) && a.getStatus().equals("Scheduled"))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> getVisitHistoryForPatient(String patientId) {
        return appointments.stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Patient> getAllPatients() {
        return patients;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctors;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointments;
    }

    @Override
    public void loadData() {
        try {
            patients = DatabaseHelper.loadPatients();
            doctors = DatabaseHelper.loadDoctors();
            appointments = DatabaseHelper.loadAppointments();
        } catch (Exception e) {
            System.err.println("Notice: Could not load existing DB data, starting fresh.");
        }
    }

}
