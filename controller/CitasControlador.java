package controller;

import model.Cita;
import model.DatabaseConection;
import packageObserver.*;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitasControlador {

    private static NotificacionManager notificationManager;

    public static void setNotificationManager(NotificacionManager manager) {
        notificationManager = manager;
    }

    public static List<Cita> getAllAppointments() {
        List<Cita> citas = new ArrayList<>();
        try (Connection conn = DatabaseConection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM appointments")) {
            while (rs.next()) {
                citas.add(new Cita(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getString("date"),
                        rs.getString("specialty")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return citas;
    }

    public static void addAppointment(Cita cita) {
        try (Connection conn = DatabaseConection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO appointments (patient_id, date, specialty) VALUES (?, ?, ?)")) {
            stmt.setInt(1, cita.getPatientId());
            stmt.setString(2, cita.getDate());
            stmt.setString(3, cita.getSpecialty());
            stmt.executeUpdate();

            notificationManager.notifyObservers("Nueva cita registrada para el paciente ID: " + cita.getPatientId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAppointment(Cita cita) {
        try (Connection conn = DatabaseConection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE appointments SET date = ?, specialty = ? WHERE id = ?")) {
            stmt.setString(1, cita.getDate());
            stmt.setString(2, cita.getSpecialty());
            stmt.setInt(3, cita.getId());
            stmt.executeUpdate();

            notificationManager.notifyObservers("Cita editada para el paciente ID: " + cita.getPatientId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAppointment(int id) {
        try (Connection conn = DatabaseConection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM appointments WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            notificationManager.notifyObservers("Una cita fue removida exitosamente para el paciente ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getPatientNameById(int patientId) {
        try (Connection conn = DatabaseConection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT name FROM patients WHERE id = ?")) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Desconocido";
    }
}
