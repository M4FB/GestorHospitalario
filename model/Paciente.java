package model;

import java.time.LocalDate;

public class Paciente {
    private int id;
    private String name;
    private LocalDate birthDate;
    private String bloodType;
    private String dni;

    public Paciente(int id, String name, LocalDate birthDate, String bloodType, String dni) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.bloodType = bloodType;
        this.dni = dni;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public boolean isMinor() {
        return birthDate.isAfter(LocalDate.now().minusYears(18));
    }
}
