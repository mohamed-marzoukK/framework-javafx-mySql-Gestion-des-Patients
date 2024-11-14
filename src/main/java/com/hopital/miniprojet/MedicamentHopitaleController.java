package com.hopital.miniprojet;

import com.hopital.miniprojet.data.HopitaleUtil;
import com.hopital.miniprojet.data.Medicament;
import com.hopital.miniprojet.data.MedicamentUtil;
import com.hopital.miniprojet.data.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.List;

public class MedicamentHopitaleController {

    @FXML
    private TextField refField;
    @FXML
    private TextField libelleField;
    @FXML
    private TextField prixField;
    @FXML
    private TableView<Medicament> medicamentTable;
    @FXML
    private TableColumn<Medicament, Integer> refColumn;
    @FXML
    private TableColumn<Medicament, String> libelleColumn;
    @FXML
    private TableColumn<Medicament, Double> prixColumn;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;


    private ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        refColumn.setCellValueFactory(new PropertyValueFactory<>("ref"));
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));


        ecouteurs();
        remplir();

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
        medicamentTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2)
                    showMedicamentDetails();
            }
        });

    }



    private void remplir() {
        ObservableList<Medicament> listMedicament = MedicamentUtil.getMedicament();
        medicamentTable.setItems(listMedicament);

    }
    private void showMedicamentDetails() {
        TablePosition position = medicamentTable.getSelectionModel().getSelectedCells().get(0);
        if (position.getRow() >= 0) {
            Medicament medicamentSelectionne = medicamentTable.getItems().get(position.getRow());

            refField.setText(Integer.toString(medicamentSelectionne.getRef()));
            libelleField.setText(medicamentSelectionne.getLibelle());
            prixField.setText(Double.toString(medicamentSelectionne.getPrix()));

        } else {
            clearFields();
        }
    }

    private void handleAjouter() {
        try {
            int ref = Integer.parseInt(refField.getText());
            String libelle = libelleField.getText();
            double prix = Double.parseDouble(prixField.getText());

            Medicament medicament = new Medicament(ref, libelle, prix);
            MedicamentUtil.addMedicament(medicament);
            remplir();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ajout du médicament.", e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format de saisie incorrect.", "Veuillez vérifier les champs de saisie.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }




    private void handleModifier() {
        Medicament selectedMedicament = medicamentTable.getSelectionModel().getSelectedItem();
        if (selectedMedicament != null) {
            try {
                int ref = selectedMedicament.getRef();
                String newLibelle = libelleField.getText();
                double newPrix = Double.parseDouble(prixField.getText());

                // Met à jour les valeurs du médicament sélectionné
                selectedMedicament.setLibelle(newLibelle);
                selectedMedicament.setPrix(newPrix);

                // Met à jour le médicament dans la base de données
                MedicamentUtil.updateMedicament(selectedMedicament);
                remplir();
                 clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la modification du médicament.", e.getMessage());
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Format de saisie incorrect.", "Veuillez vérifier les champs de saisie.");
            }
        } else {
            showAlert("Aucune sélection", "Aucun médicament sélectionné", "Veuillez sélectionner un médicament à modifier.");
        }
    }

    @FXML
    private void handleSupprimer() {
        Medicament selectedMedicament = medicamentTable.getSelectionModel().getSelectedItem();
        if (selectedMedicament != null) {
            try {
                MedicamentUtil.deleteMedicament(selectedMedicament.getRef());
                remplir();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    private void clearFields() {
        refField.clear();
        libelleField.clear();
        prixField.clear();

        medicamentTable.getSelectionModel().clearSelection();
    }

}
