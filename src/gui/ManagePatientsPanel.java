package gui;

import exceptions.InvalidInputException;
import models.Patient;
import services.IClinicService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManagePatientsPanel extends JPanel {
    private IClinicService clinicService;
    private MainFrame parentFrame;
    private JTable patientsTable;
    private DefaultTableModel patientsTableModel;

    public ManagePatientsPanel(IClinicService clinicService, MainFrame parentFrame) {
        this.clinicService = clinicService;
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initUI();
    }

    private void initUI() {
        String[] columns = {"Patient ID", "Name", "Age", "Phone Number"};
        patientsTableModel = new DefaultTableModel(columns, 0);
        patientsTable = new JTable(patientsTableModel);
        add(new JScrollPane(patientsTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField txtId = new JTextField();
        txtId.setEditable(false);
        JTextField txtName = new JTextField();
        JTextField txtAge = new JTextField();
        JTextField txtContact = new JTextField();

        bottomPanel.add(new JLabel("Patient ID (Auto-generated on Add):"));
        bottomPanel.add(txtId);
        bottomPanel.add(new JLabel("Name:"));
        bottomPanel.add(txtName);
        bottomPanel.add(new JLabel("Age:"));
        bottomPanel.add(txtAge);
        bottomPanel.add(new JLabel("Phone Number:"));
        bottomPanel.add(txtContact);

        patientsTable.getSelectionModel().addListSelectionListener(e -> {
            int row = patientsTable.getSelectedRow();
            if (row >= 0) {
                txtId.setText((String) patientsTableModel.getValueAt(row, 0));
                txtName.setText((String) patientsTableModel.getValueAt(row, 1));
                txtAge.setText((String) patientsTableModel.getValueAt(row, 2).toString());
                txtContact.setText((String) patientsTableModel.getValueAt(row, 3));
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add Patient");
        JButton btnUpdate = new JButton("Update Selected");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnClear = new JButton("Clear Fields");
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        btnAdd.addActionListener(e -> {
            try {
                clinicService.addPatient(txtName.getText(), txtAge.getText(), txtContact.getText());
                JOptionPane.showMessageDialog(this, "Patient added successfully!");
                parentFrame.refreshData();
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                if(txtId.getText().isEmpty()) throw new InvalidInputException("No patient selected.");
                clinicService.updatePatient(txtId.getText(), txtName.getText(), txtAge.getText(), txtContact.getText());
                JOptionPane.showMessageDialog(this, "Patient updated successfully!");
                parentFrame.refreshData();
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                if(txtId.getText().isEmpty()) throw new InvalidInputException("No patient selected.");
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure? This will also delete all their appointments.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    clinicService.deletePatient(txtId.getText());
                    JOptionPane.showMessageDialog(this, "Patient deleted.");
                    parentFrame.refreshData();
                    txtId.setText(""); txtName.setText(""); txtAge.setText(""); txtContact.setText("");
                }
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnClear.addActionListener(e -> {
            txtId.setText(""); txtName.setText(""); txtAge.setText(""); txtContact.setText("");
            patientsTable.clearSelection();
        });

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(bottomPanel, BorderLayout.CENTER);
        southPanel.add(btnPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
    }

    public void refreshPatientsData() {
        patientsTableModel.setRowCount(0);
        for (Patient p : clinicService.getAllPatients()) {
            patientsTableModel.addRow(new Object[]{p.getId(), p.getName(), p.getAge(), p.getPhoneNumber()});
        }
    }
}
