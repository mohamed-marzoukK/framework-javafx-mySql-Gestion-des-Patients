package com.hopital.miniprojet.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientMedUtil {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/miniprojethopitale";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Méthode pour ajouter une relation patient-médicament
    public static void addPatientMed(PatientMed patientMed) throws SQLException {
        String sql = "INSERT INTO patientmed (refMed, cinPat) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientMed.getRefMed());
            stmt.setString(2, patientMed.getCinPat());
            stmt.executeUpdate();
        }
    }

    // Méthode pour obtenir toutes les relations patient-médicament
    public static List<PatientMed> getAllPatientMeds() throws SQLException {
        List<PatientMed> patientMeds = new ArrayList<>();
        String sql = "SELECT * FROM patientmed";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int refMed = rs.getInt("refMed");
                String cinPat = rs.getString("cinPat");
                PatientMed patientMed = new PatientMed(refMed, cinPat);
                patientMeds.add(patientMed);
            }
        }
        return patientMeds;
    }

    // Méthode pour mettre à jour une relation patient-médicament
    public static void updatePatientMed(PatientMed patientMed) throws SQLException {
        String sql = "UPDATE patientmed SET cinPat = ? WHERE refMed = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientMed.getCinPat());
            stmt.setInt(2, patientMed.getRefMed());
            stmt.executeUpdate();
        }
    }

    // Méthode pour supprimer une relation patient-médicament
    public static void deletePatientMed(int refMed) throws SQLException {
        String sql = "DELETE FROM patientmed WHERE refMed = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, refMed);
            stmt.executeUpdate();
        }
    }



    // Méthode pour obtenir les relations patient-médicament sous forme ObservableList
    public static ObservableList<PatientMed> getObservablePatientMeds() {
        ObservableList<PatientMed> patientMedList = FXCollections.observableArrayList();
        try {
            List<PatientMed> patientMeds = getAllPatientMeds();
            patientMedList.addAll(patientMeds);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientMedList;
    }

}
