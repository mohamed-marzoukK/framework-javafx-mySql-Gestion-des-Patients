package com.hopital.miniprojet.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


public class HopitaleUtil {
    private static String dernierTitreErreur = "";
    private static String dernierMessageErreur = "";

    private static String JDBC_Driver = "com.mysql.cj.jdbc.Driver";
    private static String DB_Url = "jdbc:mysql://localhost:3306/miniprojethopitale?user=root&password=";

    public static String getDernierTitreErreur() {
        return dernierTitreErreur;
    }
    public static void setDernierTitreErreur(String dernierTitreErreur) {
        HopitaleUtil.dernierTitreErreur = dernierTitreErreur;
    }

    public static String getDernierMessageErreur() {
        return dernierMessageErreur;
    }

    public static void setDernierMessageErreur(String dernierMessageErreur) {
        HopitaleUtil.dernierMessageErreur = dernierMessageErreur;
    }
    public static ObservableList<Patient> getPatient() {
        ObservableList<Patient> liste = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_Driver);
            conn = DriverManager.getConnection(DB_Url);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM patient ORDER BY cin";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String cin = rs.getString("cin");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String sexe = rs.getString("sexe");
                String tel = rs.getString("tel");


                Patient patient = new Patient(cin, nom, prenom, sexe,tel);
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
    public static boolean ajouterPatient(String cin, String nom,  String prenom, String sexe, String tel) {
        boolean success = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(JDBC_Driver);
            conn = DriverManager.getConnection(DB_Url);
            String sql = "INSERT INTO patient(cin, nom, prenom,sexe,tel) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1 ,cin);
            stmt.setString(2, nom);
            stmt.setString(3, prenom);
            stmt.setString(4, sexe);
            stmt.setString(5, tel);

            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
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
        return success;
    }
    public static boolean supprimerPatient(String cin) {
        boolean success = false;
        try (Connection conn = DriverManager.getConnection(DB_Url);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM patient WHERE cin = ?")) {

            Class.forName(JDBC_Driver);
            stmt.setString(1, cin);
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);

        } catch (SQLException se) {
            setDernierTitreErreur("Erreur SQL");
            setDernierMessageErreur(se.getMessage());
        } catch (ClassNotFoundException e) {
            setDernierTitreErreur("Erreur de Driver");
            setDernierMessageErreur(e.getMessage());
        } catch (Exception e) {
            setDernierTitreErreur("Erreur Inattendue");
            setDernierMessageErreur(e.getMessage());
        }
        return success;
    }
    public static boolean modifierPatient(String cin, String nom, String prenom, String sexe, String tel, String cinActuel) {
        boolean success = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(JDBC_Driver);
            conn = DriverManager.getConnection(DB_Url);
            String sql = "UPDATE patient SET cin=?, nom=?, prenom=?, sexe=?, tel=? WHERE cin=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cin);
            stmt.setString(2, nom);
            stmt.setString(3, prenom);
            stmt.setString(4, sexe);
            stmt.setString(5, tel);
            stmt.setString(6, cinActuel); // Ajout du paramètre pour la clause WHERE

            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
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
        return success;
    }
    public static class PDFGenerator {
        public void generatePatientPDF(String cin, String nom, String prenom, String sexe, String tel) {
            String home = System.getProperty("user.home");
            String desktopPath = home + "/Desktop/patient_details.pdf";  // Chemin vers le bureau de l'utilisateur

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);

                    // Positionner chaque ligne de texte
                    float margin = 100;
                    float yPosition = 700;

                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText("CIN: " + cin);
                    contentStream.newLineAtOffset(0, -20);  // Move to next line
                    contentStream.showText("Nom: " + nom);
                    contentStream.newLineAtOffset(0, -20);  // Move to next line
                    contentStream.showText("Prénom: " + prenom);
                    contentStream.newLineAtOffset(0, -20);  // Move to next line
                    contentStream.showText("Sexe: " + sexe);
                    contentStream.newLineAtOffset(0, -20);  // Move to next line
                    contentStream.showText("Téléphone: " + tel);
                    contentStream.endText();
                }

                document.save(desktopPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    }
