package gui;

import exceptions.InvalidInputException;
import models.Doctor;
import services.IClinicService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageDoctorsPanel extends JPanel {
    private IClinicService clinicService;
    private MainFrame parentFrame;
    private JTable doctorsTable;
    private DefaultTableModel doctorsTableModel;

    public ManageDoctorsPanel(IClinicService clinicService, MainFrame parentFrame) {
        this.clinicService = clinicService;
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initUI();
    }

    private void initUI() {
        String[] columns = {"Doctor ID", "Name", "Specialty", "Phone Number"};
        doctorsTableModel = new DefaultTableModel(columns, 0);
        doctorsTable = new JTable(doctorsTableModel);
        add(new JScrollPane(doctorsTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField txtId = new JTextField();
        txtId.setEditable(false);
        JTextField txtName = new JTextField();
        JTextField txtSpecialty = new JTextField();
        JTextField txtContact = new JTextField();

        bottomPanel.add(new JLabel("Doctor ID (Auto-generated on Add):"));
        bottomPanel.add(txtId);
        bottomPanel.add(new JLabel("Name:"));
        bottomPanel.add(txtName);
        bottomPanel.add(new JLabel("Specialty:"));
        bottomPanel.add(txtSpecialty);
        bottomPanel.add(new JLabel("Phone Number:"));
        bottomPanel.add(txtContact);

        doctorsTable.getSelectionModel().addListSelectionListener(e -> {
            int row = doctorsTable.getSelectedRow();
            if (row >= 0) {
                txtId.setText((String) doctorsTableModel.getValueAt(row, 0));
                txtName.setText((String) doctorsTableModel.getValueAt(row, 1));
                txtSpecialty.setText((String) doctorsTableModel.getValueAt(row, 2));
                txtContact.setText((String) doctorsTableModel.getValueAt(row, 3));
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add Doctor");
        JButton btnUpdate = new JButton("Update Selected");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnClear = new JButton("Clear Fields");
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        btnAdd.addActionListener(e -> {
            try {
                clinicService.addDoctor(txtName.getText(), txtSpecialty.getText(), txtContact.getText());
                JOptionPane.showMessageDialog(this, "Doctor added successfully!");
                parentFrame.refreshData();
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                if(txtId.getText().isEmpty()) throw new InvalidInputException("No doctor selected.");
                clinicService.updateDoctor(txtId.getText(), txtName.getText(), txtSpecialty.getText(), txtContact.getText());
                JOptionPane.showMessageDialog(this, "Doctor updated successfully!");
                parentFrame.refreshData();
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                if(txtId.getText().isEmpty()) throw new InvalidInputException("No doctor selected.");
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure? This will also delete all their appointments.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    clinicService.deleteDoctor(txtId.getText());
                    JOptionPane.showMessageDialog(this, "Doctor deleted.");
                    parentFrame.refreshData();
                    txtId.setText(""); txtName.setText(""); txtSpecialty.setText(""); txtContact.setText("");
                }
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnClear.addActionListener(e -> {
            txtId.setText(""); txtName.setText(""); txtSpecialty.setText(""); txtContact.setText("");
            doctorsTable.clearSelection();
        });

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(bottomPanel, BorderLayout.CENTER);
        southPanel.add(btnPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
    }

    public void refreshDoctorsData() {
        doctorsTableModel.setRowCount(0);
        for (Doctor d : clinicService.getAllDoctors()) {
            doctorsTableModel.addRow(new Object[]{d.getId(), d.getName(), d.getSpecialty(), d.getPhoneNumber()});
        }
    }
}
