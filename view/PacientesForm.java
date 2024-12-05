package view;

import controller.PacienteControlador;
import model.Paciente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PacientesForm extends JFrame {
    private JTextField nameField;
    private JTextField birthDateField;
    private JTextField dniField;
    private JComboBox<String> bloodTypeComboBox;
    private JTable patientTable;

    public PacientesForm() {
        setTitle("Gestión de Pacientes");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Formulario para agregar pacientes
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Paciente"));

        nameField = new JTextField();
        birthDateField = new JTextField();
        dniField = new JTextField();
        bloodTypeComboBox = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"});

        JButton addButton = new JButton("Registrar Paciente");
        addButton.addActionListener(this::addPatient);

        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Fecha de Nacimiento (yyyy-MM-dd):"));
        formPanel.add(birthDateField);
        formPanel.add(new JLabel("DNI:"));
        formPanel.add(dniField);
        formPanel.add(new JLabel("Tipo de Sangre:"));
        formPanel.add(bloodTypeComboBox);
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        panel.add(formPanel, BorderLayout.NORTH);

        // Tabla de pacientes
        patientTable = new JTable();
        loadPatients();

        JScrollPane scrollPane = new JScrollPane(patientTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botones para editar y eliminar pacientes
        JPanel buttonPanel = new JPanel();
        JButton editButton = new JButton("Editar Paciente");
        JButton deleteButton = new JButton("Eliminar Paciente");

        editButton.addActionListener(this::editPatient);
        deleteButton.addActionListener(this::deletePatient);

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadPatients() {
        List<Paciente> patients = PacienteControlador.getAllPatients();
        String[] columns = {"ID", "Nombre", "Fecha Nacimiento", "Tipo Sangre", "DNI"};
        String[][] data = new String[patients.size()][5];

        for (int i = 0; i < patients.size(); i++) {
            Paciente patient = patients.get(i);
            data[i][0] = String.valueOf(patient.getId());
            data[i][1] = patient.getName();
            data[i][2] = patient.getBirthDate().toString();
            data[i][3] = patient.getBloodType();
            data[i][4] = patient.getDni();
        }

        patientTable.setModel(new javax.swing.table.DefaultTableModel(data, columns));
    }

    private void addPatient(ActionEvent e) {
        String name = nameField.getText();
        String birthDate = birthDateField.getText();
        String dni = dniField.getText();
        String bloodType = (String) bloodTypeComboBox.getSelectedItem();

        if (name.isEmpty() || birthDate.isEmpty() || dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        try {
            LocalDate parsedBirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            PacienteControlador.addPatient(name, parsedBirthDate, bloodType, dni);
            JOptionPane.showMessageDialog(this, "Paciente registrado exitosamente.");
            loadPatients();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use yyyy-MM-dd.");
        }
    }

    private void editPatient(ActionEvent e) {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente.");
            return;
        }

        int patientId = Integer.parseInt((String) patientTable.getValueAt(selectedRow, 0));
        String currentName = (String) patientTable.getValueAt(selectedRow, 1);
        String currentBirthDate = (String) patientTable.getValueAt(selectedRow, 2);
        String currentDni = (String) patientTable.getValueAt(selectedRow, 3);
        String currentBloodType = (String) patientTable.getValueAt(selectedRow, 4);

        String newName = JOptionPane.showInputDialog(this, "Ingrese el nuevo nombre:", currentName);
        String newBirthDate = JOptionPane.showInputDialog(this, "Ingrese la nueva fecha de nacimiento:", currentBirthDate);
        String newDni = JOptionPane.showInputDialog(this, "Ingrese el nuevo DNI:", currentBloodType);
        String newBloodType = (String) JOptionPane.showInputDialog(this, "Seleccione el nuevo tipo de sangre:", "Tipo de Sangre", JOptionPane.QUESTION_MESSAGE, null, new String[]{"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"}, currentBloodType);

        if (newName == null || newBirthDate == null || newDni == null || newName.isEmpty() || newBirthDate.isEmpty() || newDni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Operación cancelada o campos vacíos.");
            return;
        }

        try {
            LocalDate parsedBirthDate = LocalDate.parse(newBirthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            PacienteControlador.updatePatient(patientId, newName, parsedBirthDate, newBloodType, newDni);
            JOptionPane.showMessageDialog(this, "Paciente actualizado exitosamente.");
            loadPatients();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use yyyy-MM-dd.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el paciente.");
        }
    }

    private void deletePatient(ActionEvent e) {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente.");
            return;
        }

        int patientId = Integer.parseInt((String) patientTable.getValueAt(selectedRow, 0));
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este paciente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            PacienteControlador.deletePatient(patientId);
            JOptionPane.showMessageDialog(this, "Paciente eliminado exitosamente.");
            loadPatients();
        }
    }
}
