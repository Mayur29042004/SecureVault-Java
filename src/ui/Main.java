package ui;

import auth.LoginManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginManager.loadUsers(); // Load existing users

        LoginScreen login = new LoginScreen();
        Scene scene = new Scene(login.getLayout(), 400, 300);

        primaryStage.setTitle("SecureVault - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}