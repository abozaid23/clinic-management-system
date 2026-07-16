package gui;

import exceptions.AppointmentConflictException;
import exceptions.InvalidInputException;
import models.Doctor;
import models.Patient;
import services.IClinicService;

import javax.swing.*;
import java.awt.*;

public class BookAppointmentPanel extends JPanel {
    private IClinicService clinicService;
    private MainFrame parentFrame;
    private JComboBox<Patient> cbPatientsBooking;
    private JComboBox<Doctor> cbDoctorsBooking;
    private JTextField txtDateTimeBooking;

    public BookAppointmentPanel(IClinicService clinicService, MainFrame parentFrame) {
        this.clinicService = clinicService;
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Select Patient:"));
        cbPatientsBooking = new JComboBox<>();
        panel.add(cbPatientsBooking);

        panel.add(new JLabel("Select Doctor:"));
        cbDoctorsBooking = new JComboBox<>();
        panel.add(cbDoctorsBooking);

        panel.add(new JLabel("Date & Time (e.g. 2026-05-01 10:00):"));
        txtDateTimeBooking = new JTextField();
        panel.add(txtDateTimeBooking);

        JButton btnBook = new JButton("Book Appointment");
        panel.add(new JLabel(""));
        panel.add(btnBook);

        btnBook.addActionListener(e -> {
            try {
                Patient p = (Patient) cbPatientsBooking.getSelectedItem();
                Doctor d = (Doctor) cbDoctorsBooking.getSelectedItem();
                if (p == null || d == null) throw new InvalidInputException("Please select a patient and a doctor.");
                
                clinicService.bookAppointment(p.getId(), d.getId(), txtDateTimeBooking.getText());
                JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
                txtDateTimeBooking.setText("");
                parentFrame.refreshData();
            } catch (InvalidInputException | AppointmentConflictException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Booking Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel, BorderLayout.NORTH);
    }

    public void refreshDropdowns() {
        cbPatientsBooking.removeAllItems();
        for (Patient p : clinicService.getAllPatients()) {
            cbPatientsBooking.addItem(p);
        }

        cbDoctorsBooking.removeAllItems();
        for (Doctor d : clinicService.getAllDoctors()) {
            cbDoctorsBooking.addItem(d);
        }
    }
}
