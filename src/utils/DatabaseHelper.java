package utils;

import models.Appointment;
import models.Doctor;
import models.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    // NOTE: You must have the PostgreSQL JDBC driver (.jar) on the classpath.
    // Connection settings are read from environment variables so credentials are
    // never committed. Defaults target a local postgres install.
    //   CLINIC_DB_URL   (default: jdbc:postgresql://localhost:5432/clinic_db)
    //   CLINIC_DB_USER  (default: postgres)
    //   CLINIC_DB_PASSWORD (required)
    private static final String URL = envOr("CLINIC_DB_URL", "jdbc:postgresql://localhost:5432/clinic_db");
    private static final String USER = envOr("CLINIC_DB_USER", "postgres");
    private static final String PASSWORD = envOr("CLINIC_DB_PASSWORD", "");

    private static String envOr(String key, String fallback) {
        String v = System.getenv(key);
        return (v == null || v.isEmpty()) ? fallback : v;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Create Patients table
            String createPatients = "CREATE TABLE IF NOT EXISTS patients (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "age INT, " +
                    "phone_number VARCHAR(50))";
            stmt.execute(createPatients);

            // Create Doctors table
            String createDoctors = "CREATE TABLE IF NOT EXISTS doctors (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "specialty VARCHAR(100), " +
                    "phone_number VARCHAR(50))";
            stmt.execute(createDoctors);

            // Create Appointments table
            String createAppointments = "CREATE TABLE IF NOT EXISTS appointments (" +
                    "appointment_id VARCHAR(50) PRIMARY KEY, " +
                    "patient_id VARCHAR(50), " +
                    "doctor_id VARCHAR(50), " +
                    "date_time VARCHAR(100), " +
                    "status VARCHAR(50))";
            stmt.execute(createAppointments);
        } catch (SQLException e) {
            System.err.println("Could not initialize DB: " + e.getMessage());
        }
    }

    // --- READ METHODS ---
    public static List<Patient> loadPatients() {
        List<Patient> list = new ArrayList<>();
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM patients")) {
            while (rs.next()) {
                list.add(new Patient(rs.getString("id"), rs.getString("name"),
                        rs.getInt("age"), rs.getString("phone_number")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Doctor> loadDoctors() {
        List<Doctor> list = new ArrayList<>();
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM doctors")) {
            while (rs.next()) {
                list.add(new Doctor(rs.getString("id"), rs.getString("name"),
                        rs.getString("specialty"), rs.getString("phone_number")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Appointment> loadAppointments() {
        List<Appointment> list = new ArrayList<>();
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM appointments")) {
            while (rs.next()) {
                list.add(new Appointment(rs.getString("appointment_id"), rs.getString("patient_id"),
                        rs.getString("doctor_id"), rs.getString("date_time"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- ADD METHODS ---
    public static void addPatient(Patient p) {
        String sql = "INSERT INTO patients (id, name, age, phone_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getId());
            pstmt.setString(2, p.getName());
            pstmt.setInt(3, p.getAge());
            pstmt.setString(4, p.getPhoneNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addDoctor(Doctor d) {
        String sql = "INSERT INTO doctors (id, name, specialty, phone_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, d.getId());
            pstmt.setString(2, d.getName());
            pstmt.setString(3, d.getSpecialty());
            pstmt.setString(4, d.getPhoneNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addAppointment(Appointment a) {
        String sql = "INSERT INTO appointments (appointment_id, patient_id, doctor_id, date_time, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getAppointmentId());
            pstmt.setString(2, a.getPatientId());
            pstmt.setString(3, a.getDoctorId());
            pstmt.setString(4, a.getDateAndTime());
            pstmt.setString(5, a.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- UPDATE METHODS ---
    public static void updatePatient(Patient p) {
        String sql = "UPDATE patients SET name = ?, age = ?, phone_number = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getName());
            pstmt.setInt(2, p.getAge());
            pstmt.setString(3, p.getPhoneNumber());
            pstmt.setString(4, p.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateDoctor(Doctor d) {
        String sql = "UPDATE doctors SET name = ?, specialty = ?, phone_number = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, d.getName());
            pstmt.setString(2, d.getSpecialty());
            pstmt.setString(3, d.getPhoneNumber());
            pstmt.setString(4, d.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAppointment(Appointment a) {
        String sql = "UPDATE appointments SET date_time = ?, status = ? WHERE appointment_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getDateAndTime());
            pstmt.setString(2, a.getStatus());
            pstmt.setString(3, a.getAppointmentId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- DELETE METHODS ---
    public static void deletePatient(String id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDoctor(String id) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAppointmentsByPatient(String patientId) {
        String sql = "DELETE FROM appointments WHERE patient_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patientId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAppointmentsByDoctor(String doctorId) {
        String sql = "DELETE FROM appointments WHERE doctor_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doctorId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
