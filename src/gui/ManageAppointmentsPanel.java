package gui;

import exceptions.AppointmentConflictException;
import exceptions.InvalidInputException;
import models.Appointment;
import services.IClinicService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageAppointmentsPanel extends JPanel {
    private IClinicService clinicService;
    private MainFrame parentFrame;
    private JTable appointmentsTable;
    private DefaultTableModel appointmentsTableModel;

    public ManageAppointmentsPanel(IClinicService clinicService, MainFrame parentFrame) {
        this.clinicService = clinicService;
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initUI();
    }

    private void initUI() {
        String[] columns = {"Appt ID", "Patient ID", "Doctor ID", "Date Time", "Status"};
        appointmentsTableModel = new DefaultTableModel(columns, 0);
        appointmentsTable = new JTable(appointmentsTableModel);
        add(new JScrollPane(appointmentsTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField txtAppointmentIdManage = new JTextField();
        txtAppointmentIdManage.setEditable(false);
        JTextField txtNewDateTimeManage = new JTextField();

        bottomPanel.add(new JLabel("Selected Appt ID:"));
        bottomPanel.add(txtAppointmentIdManage);

        bottomPanel.add(new JLabel("New Date & Time (For Reschedule):"));
        bottomPanel.add(txtNewDateTimeManage);

        JPanel btns = new JPanel(new FlowLayout());
        JButton btnCancel = new JButton("Cancel Appointment");
        JButton btnReschedule = new JButton("Reschedule Appointment");
        btns.add(btnCancel);
        btns.add(btnReschedule);
        
        appointmentsTable.getSelectionModel().addListSelectionListener(e -> {
            int row = appointmentsTable.getSelectedRow();
            if (row >= 0) {
                txtAppointmentIdManage.setText((String) appointmentsTableModel.getValueAt(row, 0));
                txtNewDateTimeManage.setText((String) appointmentsTableModel.getValueAt(row, 3));
            }
        });

        btnCancel.addActionListener(e -> {
            try {
                if (txtAppointmentIdManage.getText().isEmpty()) throw new InvalidInputException("No appointment selected.");
                clinicService.cancelAppointment(txtAppointmentIdManage.getText());
                JOptionPane.showMessageDialog(this, "Appointment Cancelled Successfully.");
                parentFrame.refreshData();
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnReschedule.addActionListener(e -> {
            try {
                if (txtAppointmentIdManage.getText().isEmpty()) throw new InvalidInputException("No appointment selected.");
                clinicService.rescheduleAppointment(txtAppointmentIdManage.getText(), txtNewDateTimeManage.getText());
                JOptionPane.showMessageDialog(this, "Appointment Rescheduled Successfully.");
                parentFrame.refreshData();
            } catch (InvalidInputException | AppointmentConflictException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(bottomPanel, BorderLayout.CENTER);
        southPanel.add(btns, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
    }

    public void refreshAppointmentsTable() {
        appointmentsTableModel.setRowCount(0);
        for (Appointment a : clinicService.getAllAppointments()) {
            appointmentsTableModel.addRow(new Object[]{
                    a.getAppointmentId(),
                    a.getPatientId(),
                    a.getDoctorId(),
                    a.getDateAndTime(),
                    a.getStatus()
            });
        }
    }
}
