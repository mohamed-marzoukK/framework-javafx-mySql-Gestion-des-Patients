package com.hopital.miniprojet;

import com.hopital.miniprojet.data.HopitaleUtil;
import com.hopital.miniprojet.data.Patient;
import javafx.collections.FXCollections;

import com.hopital.miniprojet.data.HopitaleUtil.PDFGenerator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

public class GestionHopitaleController  implements Initializable {
    @FXML
    private TableView<Patient> patientTableView;


    @FXML
    private TextField cinTextField;

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField prenomTextField;

    @FXML
    private TextField telTextField;

    @FXML
    private RadioButton btnFeminin;
    @FXML
    private RadioButton btnMasculin;
    @FXML
    private ImageView btnAjouter;
    @FXML
    private ImageView btnModifier;
    @FXML
    private ImageView btnValider;
    @FXML
    private ImageView btnAnnuler;
    @FXML
    private ImageView btnImprimer;

    @FXML
    private ImageView btnSuprimer;
    @FXML
    private ToggleGroup toggleRadio;
    @FXML
    private void handleMenuItemGestionMedicaments() {
        try {
            // Charger le fichier FXML pour la gestion des médicaments
            FXMLLoader loader = new FXMLLoader(getClass().getResource("medicament-hopitale.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène et une nouvelle fenêtre pour la gestion des médicaments
            Stage stage = new Stage();
            stage.setTitle("Gestion des Médicaments");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenuItemEffectuerMedicaments() {
        try {
            // Charger le fichier FXML pour la gestion des médicaments
            FXMLLoader loader = new FXMLLoader(getClass().getResource("effectuer-medicament.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène et une nouvelle fenêtre pour la gestion des médicaments
            Stage stage = new Stage();
            stage.setTitle("effectuer des Médicaments");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HopitaleUtil.PDFGenerator PDFGenerator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ajouterColonnes();
        ecouteurs();
        remplir();
        PDFGenerator = new HopitaleUtil.PDFGenerator();
        toggleRadio= new ToggleGroup();
        btnMasculin.setToggleGroup(toggleRadio);
        btnFeminin.setToggleGroup(toggleRadio);
    }
    private void ecouteurs() {
        patientTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2)
                    afficherModifier();
            }
        });
        btnAjouter.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 1)
                    ajouter();
            }
        });
        btnSuprimer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 1)
                    supprimer();
            }
        });
        btnModifier.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 1)
                    modifier();
            }
        });
        btnValider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                validerModification();
            }
        });
        btnAnnuler.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                annulerModification();
            }
        });
        btnImprimer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 1)
                    imprimerPDF();
            }
        });
        }

    private void afficherModifier() {
        TablePosition position = patientTableView.getSelectionModel().getSelectedCells().get(0);
        if (position.getRow() >= 0) {
            Patient patientSelectionne = patientTableView.getItems().get(position.getRow());

            cinTextField.setText(patientSelectionne.getCin());
            nomTextField.setText(patientSelectionne.getNom());
            prenomTextField.setText(patientSelectionne.getPrenom());
            telTextField.setText(patientSelectionne.getTel());

            String sexe = patientSelectionne.getSexe();
            if (sexe != null && !sexe.isEmpty()) {
                if (sexe.equalsIgnoreCase("masculin")) {
                    btnMasculin.setSelected(true);
                    btnFeminin.setSelected(false);
                } else if (sexe.equalsIgnoreCase("feminin")) {
                    btnMasculin.setSelected(false);
                    btnFeminin.setSelected(true);
                }
            }

            // Désactiver le champ cin et activer les autres champs pour modification
            cinTextField.setDisable(true);
            nomTextField.setDisable(false);
            prenomTextField.setDisable(false);
            telTextField.setDisable(false);
            btnMasculin.setDisable(false);
            btnFeminin.setDisable(false);

            // Afficher les boutons de validation et d'annulation
            btnValider.setVisible(true);
            btnAnnuler.setVisible(true);

            // Masquer les autres boutons pour éviter la confusion
            btnAjouter.setVisible(false);
            btnModifier.setVisible(false);
            btnSuprimer.setVisible(false);
            btnImprimer.setVisible(false);
        }
    }

    private void ajouterColonnes() {
        patientTableView.getColumns().clear();
        TableColumn<Patient, String> cinP = new TableColumn<>("Cin");
        TableColumn<Patient, String> nomP = new TableColumn<>("Nom");
        TableColumn<Patient, String> prenomP = new TableColumn<>("Prenom");
        TableColumn<Patient, String> telP = new TableColumn<>("Telephone");
        cinP.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nomP.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomP.setCellValueFactory(new PropertyValueFactory<>("Prenom"));
        telP.setCellValueFactory(new PropertyValueFactory<>("tel"));
        patientTableView.getColumns().addAll(cinP,nomP, prenomP, telP);
    }
    private void remplir() {
        ObservableList<Patient> listPatients = HopitaleUtil.getPatient();
        patientTableView.setItems(listPatients);

    }
    private void ajouter() {
        // Vérification si tous les champs sont remplis
        if (cinTextField.getText().isEmpty() || nomTextField.getText().isEmpty() || prenomTextField.getText().isEmpty() || telTextField.getText().isEmpty()) {
            afficherErreur("Champs incomplets", "Veuillez remplir tous les champs avant d'ajouter un patient.");
            return;
        }

        String sexe;
        if (btnFeminin.isSelected()) {
            sexe = "feminin";
        } else {
            sexe = "masculin";
        }

        if (HopitaleUtil.ajouterPatient(cinTextField.getText(),
                nomTextField.getText(), prenomTextField.getText(), sexe,
                telTextField.getText())) {
            remplir();
            // Effacer les champs après l'ajout
            cinTextField.clear();
            nomTextField.clear();
            prenomTextField.clear();
            telTextField.clear();
        } else {
            // Afficher une erreur en cas d'échec de l'ajout
            afficherErreur(HopitaleUtil.getDernierTitreErreur(), HopitaleUtil.getDernierMessageErreur());
        }
    }

    public static void afficherErreur(String titre, String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setTitle(titre);
        dialog.setHeaderText(message);
        dialog.showAndWait();
    }

    private void supprimer() {
        TablePosition position = patientTableView.getSelectionModel().getSelectedCells().get(0);
        if (position.getRow() >= 0) {
            Patient patientSelectionne = patientTableView.getItems().get(position.getRow());
            String cin = patientSelectionne.getCin();
            if (HopitaleUtil.supprimerPatient(cin))
                remplir();
            else
                afficherErreur(HopitaleUtil.getDernierTitreErreur(), HopitaleUtil.getDernierMessageErreur());
        }
 }
    private void modifier() {
        afficherModifier();
    }
    private void validerModification() {
        String cin = cinTextField.getText();
        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        String tel = telTextField.getText();
        String sexe = btnFeminin.isSelected() ? "feminin" : "masculin";

        if (HopitaleUtil.modifierPatient(cin, nom, prenom, sexe, tel, cin)) {
            remplir();
            cinTextField.clear();
            nomTextField.clear();
            prenomTextField.clear();
            telTextField.clear();
            resetFormState();
        } else {
            afficherErreur(HopitaleUtil.getDernierTitreErreur(), HopitaleUtil.getDernierMessageErreur());
        }
    }

    private void annulerModification() {
        cinTextField.clear();
        nomTextField.clear();
        prenomTextField.clear();
        telTextField.clear();
        resetFormState();
    }
    private void resetFormState() {
        cinTextField.setDisable(false);
        nomTextField.setDisable(false);
        prenomTextField.setDisable(false);
        telTextField.setDisable(false);
        btnMasculin.setDisable(false);
        btnFeminin.setDisable(false);

        btnValider.setVisible(false);
        btnAnnuler.setVisible(false);

        btnAjouter.setVisible(true);
        btnModifier.setVisible(true);
        btnSuprimer.setVisible(true);
        btnImprimer.setVisible(true);
    }
    private void imprimerPDF() {
        TablePosition position = patientTableView.getSelectionModel().getSelectedCells().get(0);
        if (position.getRow() >= 0) {
            Patient patientSelectionne = patientTableView.getItems().get(position.getRow());
            PDFGenerator.generatePatientPDF(patientSelectionne.getCin(), patientSelectionne.getNom(),
                    patientSelectionne.getPrenom(), patientSelectionne.getSexe(), patientSelectionne.getTel());

            String home = System.getProperty("user.home");
            String desktopPath = home + "/Desktop/patient_details.pdf";  // Chemin vers le bureau de l'utilisateur
            afficherAlerte("Impression PDF", "Le PDF a été généré avec succès et enregistré à l'emplacement suivant :\n" + desktopPath);
        }
    }

    private void afficherAlerte(String titre, String contenu) {
        Alert alerte = new Alert(Alert.AlertType.INFORMATION);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(contenu);
        alerte.showAndWait();
    }



}