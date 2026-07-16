package main;

import gui.MainFrame;

import javax.swing.SwingUtilities;

public class ClinicApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
