package model;

public class Cita {
    private int id;
    private int patientId;
    private String date;
    private String specialty;

    public Cita(int id, int patientId, String date, String specialty) {
        this.id = id;
        this.patientId = patientId;
        this.date = date;
        this.specialty = specialty;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}
