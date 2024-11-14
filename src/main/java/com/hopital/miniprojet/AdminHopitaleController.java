package com.hopital.miniprojet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminHopitaleController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authenticate(username, password)) {
            // Identifiants corrects, ouvrir la page d'accueil de l'administrateur
            try {
                GestionHopitaleApplication.showGestionHopitaleView();
                // Fermer la fenêtre d'authentification
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Identifiants incorrects, afficher une alerte
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur d'authentification");
            alert.setHeaderText(null);
            alert.setContentText("Nom d'utilisateur ou mot de passe incorrect.");
            alert.showAndWait();
        }
    }
    private boolean authenticate(String login, String password) {
        // URL de la base de données
        String jdbcUrl = "jdbc:mysql://localhost:3306/miniprojethopitale";
        String dbUser = "root";
        String dbPassword = "";

        String sql = "SELECT * FROM personnel WHERE login = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, login);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return true; // Identifiants corrects
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Identifiants incorrects
    }

}
