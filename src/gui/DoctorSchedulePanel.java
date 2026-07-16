package gui;

import models.Appointment;
import models.Doctor;
import services.IClinicService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DoctorSchedulePanel extends JPanel {
    private IClinicService clinicService;
    private JComboBox<Doctor> cbDoctorsSchedule;
    private JTextArea txtDoctorSchedule;

    public DoctorSchedulePanel(IClinicService clinicService) {
        this.clinicService = clinicService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initUI();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Doctor: "));
        cbDoctorsSchedule = new JComboBox<>();
        topPanel.add(cbDoctorsSchedule);

        txtDoctorSchedule = new JTextArea();
        txtDoctorSchedule.setEditable(false);
        
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(txtDoctorSchedule), BorderLayout.CENTER);

        cbDoctorsSchedule.addActionListener(e -> {
            Doctor d = (Doctor) cbDoctorsSchedule.getSelectedItem();
            if (d != null) {
                List<Appointment> apps = clinicService.getUpcomingAppointmentsForDoctor(d.getId());
                StringBuilder sb = new StringBuilder();
                for (Appointment a : apps) sb.append(a.toString()).append("\n");
                txtDoctorSchedule.setText(sb.toString());
            } else {
                txtDoctorSchedule.setText("");
            }
        });
    }

    public void refreshDropdowns() {
        cbDoctorsSchedule.removeAllItems();
        for (Doctor d : clinicService.getAllDoctors()) {
            cbDoctorsSchedule.addItem(d);
        }
    }
}
