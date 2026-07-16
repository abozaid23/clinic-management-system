package gui;

import services.ClinicService;
import services.IClinicService;

import javax.swing.*;

public class MainFrame extends JFrame {
    private IClinicService clinicService;

    private ManagePatientsPanel managePatientsPanel;
    private ManageDoctorsPanel manageDoctorsPanel;
    private BookAppointmentPanel bookAppointmentPanel;
    private ManageAppointmentsPanel manageAppointmentsPanel;
    private DoctorSchedulePanel doctorSchedulePanel;
    private PatientHistoryPanel patientHistoryPanel;

    public MainFrame() {
        clinicService = new ClinicService();
        setTitle("Clinic Appointment System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        managePatientsPanel = new ManagePatientsPanel(clinicService, this);
        manageDoctorsPanel = new ManageDoctorsPanel(clinicService, this);
        bookAppointmentPanel = new BookAppointmentPanel(clinicService, this);
        manageAppointmentsPanel = new ManageAppointmentsPanel(clinicService, this);
        doctorSchedulePanel = new DoctorSchedulePanel(clinicService);
        patientHistoryPanel = new PatientHistoryPanel(clinicService);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Manage Patients", managePatientsPanel);
        tabbedPane.addTab("Manage Doctors", manageDoctorsPanel);
        tabbedPane.addTab("Book Appointment", bookAppointmentPanel);
        tabbedPane.addTab("Manage Appointments", manageAppointmentsPanel);
        tabbedPane.addTab("Doctor Schedule", doctorSchedulePanel);
        tabbedPane.addTab("Patient History", patientHistoryPanel);

        add(tabbedPane);

        refreshData();
    }

    public void refreshData() {
        managePatientsPanel.refreshPatientsData();
        manageDoctorsPanel.refreshDoctorsData();
        bookAppointmentPanel.refreshDropdowns();
        manageAppointmentsPanel.refreshAppointmentsTable();
        doctorSchedulePanel.refreshDropdowns();
        patientHistoryPanel.refreshDropdowns();
    }
}
