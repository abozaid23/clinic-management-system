package models;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends Person {
    private String specialty;
    private List<Appointment> appointments;

    public Doctor(String id, String name, String specialty, String phoneNumber) {
        super(id, name, phoneNumber);
        this.specialty = specialty;
        this.appointments = new ArrayList<>();
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void addAppointment(Appointment appt) {
        this.appointments.add(appt);
    }

    @Override
    public String getDetails() {
        return "Doctor ID: " + id + ", Name: " + name + ", Specialty: " + specialty;
    }

    @Override
    public String toString() {
        return name + " - " + specialty;
    }
}
