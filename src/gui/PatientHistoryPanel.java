package gui;

import models.Appointment;
import models.Patient;
import services.IClinicService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PatientHistoryPanel extends JPanel {
    private IClinicService clinicService;
    private JComboBox<Patient> cbPatientsHistory;
    private JTextArea txtPatientHistory;

    public PatientHistoryPanel(IClinicService clinicService) {
        this.clinicService = clinicService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initUI();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Patient: "));
        cbPatientsHistory = new JComboBox<>();
        topPanel.add(cbPatientsHistory);

        txtPatientHistory = new JTextArea();
        txtPatientHistory.setEditable(false);
        
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(txtPatientHistory), BorderLayout.CENTER);

        cbPatientsHistory.addActionListener(e -> {
            Patient p = (Patient) cbPatientsHistory.getSelectedItem();
            if (p != null) {
                List<Appointment> apps = clinicService.getVisitHistoryForPatient(p.getId());
                StringBuilder sb = new StringBuilder();
                for (Appointment a : apps) sb.append(a.toString()).append("\n");
                txtPatientHistory.setText(sb.toString());
            } else {
                txtPatientHistory.setText("");
            }
        });
    }

    public void refreshDropdowns() {
        cbPatientsHistory.removeAllItems();
        for (Patient p : clinicService.getAllPatients()) {
            cbPatientsHistory.addItem(p);
        }
    }
}
