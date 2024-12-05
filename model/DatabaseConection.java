package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConection {
    private static final String URL = "jdbc:sqlite:clinic_manager.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS patients (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    birth_date DATE NOT NULL,
                    blood_type TEXT NOT NULL,
                    dni TEXT NOT NULL
                );
                CREATE TABLE IF NOT EXISTS appointments (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    patient_id INTEGER,
                    date TEXT NOT NULL,
                    specialty TEXT NOT NULL,
                    FOREIGN KEY (patient_id) REFERENCES patients(id)
                );
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
