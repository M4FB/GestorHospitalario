package controller;

import model.DatabaseConection;
import model.Paciente;
import packageObserver.*;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class PacienteControlador {

    private static NotificacionManager notificationManager;

    public static void setNotificationManager(NotificacionManager manager) {
        notificationManager = manager;
    }

    public static void addPatient(String name, LocalDate birthDate, String bloodType, String dni) {
        try (Connection conn = DatabaseConection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO patients (name, birth_date, blood_type, dni) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setDate(2, Date.valueOf(birthDate));
            stmt.setString(3, bloodType);
            stmt.setString(4, dni);
            stmt.executeUpdate();

            notificationManager.notifyObservers("Nuevo paciente registrado: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Paciente> getAllPatients() {
        List<Paciente> patients = new ArrayList<>();
        try (Connection conn = DatabaseConection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM patients")) {

            while (rs.next()) {
                Date dbDate = rs.getDate("birth_date");
                LocalDate birthDate = dbDate.toLocalDate();

                patients.add(new Paciente(
                        rs.getInt("id"),
                        rs.getString("name"),
                        birthDate,
                        rs.getString("blood_type"),
                        rs.getString("dni")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public static void updatePatient(int id, String name, LocalDate birthDate, String bloodType, String dni) {
        try (Connection conn = DatabaseConection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE patients SET name = ?, birth_date = ?, blood_type = ?, dni = ? WHERE id = ?")) {
            stmt.setString(1, name);
            stmt.setDate(2, Date.valueOf(birthDate));
            stmt.setString(3, bloodType);
            stmt.setString(4, dni);
            stmt.setInt(5, id);
            stmt.executeUpdate();

            notificationManager.notifyObservers("Un paciente fue editado: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePatient(int id) {
        try (Connection conn = DatabaseConection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM patients WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

            notificationManager.notifyObservers("Un paciente fue eliminado: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
