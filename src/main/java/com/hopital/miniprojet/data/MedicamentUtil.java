package com.hopital.miniprojet.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentUtil {
    private static String dernierTitreErreur = "";
    private static String dernierMessageErreur = "";

    private static String JDBC_Driver = "com.mysql.cj.jdbc.Driver";
    private static String DB_Url = "jdbc:mysql://localhost:3306/miniprojethopitale?user=root&password=";

    public static String getDernierTitreErreur() {
        return dernierTitreErreur;
    }
    public static void setDernierTitreErreur(String dernierTitreErreur) {
        MedicamentUtil.dernierTitreErreur = dernierTitreErreur;
    }

    public static String getDernierMessageErreur() {
        return dernierMessageErreur;
    }

    public static void setDernierMessageErreur(String dernierMessageErreur) {
        MedicamentUtil.dernierMessageErreur = dernierMessageErreur;
    }
    private static final String URL = "jdbc:mysql://localhost:3306/miniprojethopitale"; // URL de la base de données
    private static final String USER = "root"; // Nom d'utilisateur de la base de données
    private static final String PASSWORD = ""; // Mot de passe de la base de données

    // Méthode pour ajouter un médicament
    public static void addMedicament(Medicament medicament) throws SQLException {
        String sql = "INSERT INTO medicament (ref, libelle, prix) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, medicament.getRef());
            stmt.setString(2, medicament.getLibelle());
            stmt.setDouble(3, medicament.getPrix());
            stmt.executeUpdate();
        }
    }
    public static ObservableList<Medicament> getMedicament() {
        ObservableList<Medicament> liste = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_Driver);
            conn = DriverManager.getConnection(DB_Url);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM medicament ORDER BY ref";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int ref = rs.getInt("ref");
                String libelle = rs.getString("libelle");
                double prix = rs.getDouble("prix");



                Medicament patient = new Medicament(ref, libelle, prix);
                liste.add(patient);
            }
            rs.close();
        } catch (SQLException se) {
            setDernierTitreErreur("Erreur SQL");
            setDernierMessageErreur(se.getMessage());
        } catch (Exception e) {
            setDernierTitreErreur("Erreur Inattendue");
            setDernierMessageErreur(e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                setDernierTitreErreur("Erreur de Fermeture de Connexion");
                setDernierMessageErreur(se.getMessage());
            }
        }
        return liste;
    }


    // Méthode pour mettre à jour un médicament
    public static void updateMedicament(Medicament medicament) throws SQLException {
        String sql = "UPDATE medicament SET libelle = ?, prix = ? WHERE ref = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, medicament.getLibelle());
            stmt.setDouble(2, medicament.getPrix());
            stmt.setInt(3, medicament.getRef());
            stmt.executeUpdate();
        }
    }

    // Méthode pour supprimer un médicament
    public static void deleteMedicament(int ref) throws SQLException {
        String sql = "DELETE FROM medicament WHERE ref = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ref);
            stmt.executeUpdate();
        }
    }

}
