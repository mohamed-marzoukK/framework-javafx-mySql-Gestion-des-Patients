package com.hopital.miniprojet;

import com.hopital.miniprojet.data.Medicament;
import com.hopital.miniprojet.data.Patient;
import com.hopital.miniprojet.data.PatientMed;
import com.hopital.miniprojet.data.PatientMedUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EffectuerMedicamentController {

    @FXML
    private ComboBox<Medicament> refMedComboBox;

    @FXML
    private ComboBox<Patient> cinPatComboBox;

    @FXML
    private TableView<PatientMed> patientMedTable;

    @FXML
    private TableColumn<PatientMed, Integer> refMedColumn;

    @FXML
    private TableColumn<PatientMed, String> cinPatColumn;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnAnnuler;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/miniprojethopitale";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @FXML
    public void initialize() {
        refMedColumn.setCellValueFactory(new PropertyValueFactory<>("refMed"));
        cinPatColumn.setCellValueFactory(new PropertyValueFactory<>("cinPat"));



        ObservableList<PatientMed> patientMeds = PatientMedUtil.getObservablePatientMeds();
        patientMedTable.setItems(patientMeds);
        ecouteurs();


        loadRefMedComboBox();
        loadCinPatComboBox();

    }

    private void ecouteurs() {
        btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                handleAjouter();
            }
        });
        btnModifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                handleModifier();
            }
        });
        btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                handleSupprimer();
            }
        });
        btnAnnuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                handleAnnuler();
            }
        });
        patientMedTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2)
                    showMedicamentDetails();
            }
        });
    }
    private void showMedicamentDetails() {
        TablePosition position = patientMedTable.getSelectionModel().getSelectedCells().get(0);
        if (position.getRow() >= 0) {
            PatientMed row = patientMedTable.getItems().get(position.getRow());
            ;
            refMedComboBox.setValue(findMedicamentByRef(row.getRefMed()));
                cinPatComboBox.setValue(findPatientByCin(row.getCinPat()));

        } else {
            clearFields();
        }
    }
    private Medicament findMedicamentByRef(int refMed) {
        for (Medicament med : refMedComboBox.getItems()) {
            if (med.getRef() == refMed) {
                return med;
            }
        }
        return null; // ou une valeur par défaut si nécessaire
    }
    private Patient findPatientByCin(String cin) {
        for (Patient med : cinPatComboBox.getItems()) {
            if (med.getCin() == cin) {
                return med;
            }
        }
        return null; // ou une valeur par défaut si nécessaire
    }

    private void loadRefMedComboBox() {
        String sql = "SELECT ref, libelle FROM medicament";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            ObservableList<Medicament> refMeds = FXCollections.observableArrayList();
            while (rs.next()) {
                int ref = rs.getInt("ref");
                String libelle = rs.getString("libelle");
                refMeds.add(new Medicament(ref, libelle));
            }
            refMedComboBox.setItems(refMeds);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCinPatComboBox() {
        String sql = "SELECT cin ,nom FROM patient";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            ObservableList<Patient> cinPats = FXCollections.observableArrayList();
            while (rs.next()) {
                String cin = rs.getString("cin");
                String nom = rs.getString("nom");
                cinPats.add(new Patient(cin, nom));
            }
            cinPatComboBox.setItems(cinPats);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void handleAjouter() {
        Medicament selectedMed = refMedComboBox.getValue();
        Patient cinPat = cinPatComboBox.getValue();
        if (selectedMed == null || cinPat == null) {
            showAlert("Erreur", "Veuillez sélectionner un médicament et un patient.");
            return;
        }
        PatientMed newPatientMed = new PatientMed(selectedMed.getRef(), cinPat.getCin());

        try {
            PatientMedUtil.addPatientMed(newPatientMed);
            patientMedTable.getItems().add(newPatientMed);
            clearFields();
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible d'ajouter la relation patient-médicament.");
            e.printStackTrace();
        }
    }


    private void handleModifier() {
        PatientMed selectedPatientMed = patientMedTable.getSelectionModel().getSelectedItem();
        if (selectedPatientMed != null) {
            Medicament selectedMed = refMedComboBox.getValue();
            Patient cinPat = cinPatComboBox.getValue();
            if (selectedMed == null || cinPat == null) {
                showAlert("Erreur", "Veuillez sélectionner un médicament et un patient.");
                return;
            }
            selectedPatientMed.setRefMed(selectedMed.getRef());
            selectedPatientMed.setCinPat(cinPat.getCin());

            try {
                PatientMedUtil.updatePatientMed(selectedPatientMed);
                patientMedTable.refresh();
                clearFields();
            } catch (SQLException e) {
                showAlert("Erreur", "Impossible de modifier la relation patient-médicament.");
                e.printStackTrace();
            }
        } else {
            showAlert("Aucune sélection", "Aucune relation patient-médicament sélectionnée.");
        }
    }


    private void handleSupprimer() {
        PatientMed selectedPatientMed = patientMedTable.getSelectionModel().getSelectedItem();
        if (selectedPatientMed != null) {
            try {
                PatientMedUtil.deletePatientMed(selectedPatientMed.getRefMed());
                patientMedTable.getItems().remove(selectedPatientMed);
                clearFields();
            } catch (SQLException e) {
                showAlert("Erreur", "Impossible de supprimer la relation patient-médicament.");
                e.printStackTrace();
            }
        } else {
            showAlert("Aucune sélection", "Aucune relation patient-médicament sélectionnée.");
        }
    }





    private void handleAnnuler() {
        clearFields();
    }

    private void clearFields() {
        refMedComboBox.getSelectionModel().clearSelection();
        cinPatComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
