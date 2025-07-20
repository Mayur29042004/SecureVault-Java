package ui;

import auth.LoginManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static String currentUser = null;

    @Override
    public void start(Stage primaryStage) {
        LoginManager.loadUsers();
        LoginScreen login = new LoginScreen();
        Scene scene = new Scene(login.getLayout(), 400, 300);
        scene.getStylesheets().add("style-dark.css");
        scene.getStylesheets().clear();
        scene.getStylesheets().add("style-light.css");

        primaryStage.setTitle("SecureVault - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}