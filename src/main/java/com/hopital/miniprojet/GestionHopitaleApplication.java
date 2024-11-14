package com.hopital.miniprojet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GestionHopitaleApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Code pour démarrer l'application principale
        Parent root = FXMLLoader.load(getClass().getResource("admin-hopitale.fxml"));
        primaryStage.setTitle("Authentification Admin");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void showGestionHopitaleView() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(GestionHopitaleApplication.class.getResource("gestion-hopitale.fxml"));
        stage.setTitle("Gestion des Patients");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}