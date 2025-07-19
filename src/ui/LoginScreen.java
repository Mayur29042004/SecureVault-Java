package ui;

import auth.LoginManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginScreen {
    private VBox layout;

    public LoginScreen() {
        layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #121212; -fx-padding: 40;");

        Text title = new Text("🔐 SecureVault Login");
        title.setStyle("-fx-font-size: 20px; -fx-fill: #00bcd4;");

        TextField username = new TextField();
        username.setPromptText("Username");
        username.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-border-color: #333;");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-border-color: #333;");

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        loginBtn.setStyle("-fx-background-color: #00bcd4; -fx-text-fill: white;");
        registerBtn.setStyle("-fx-background-color: #333; -fx-text-fill: white;");

        layout.getChildren().addAll(title, username, password, loginBtn, registerBtn);

        // Action listeners
        loginBtn.setOnAction(e -> {
            try {
                String user = username.getText();
                String pass = password.getText();
                boolean success = LoginManager.login(user, pass);
                if (success) {
                    DashboardScreen dashboard = new DashboardScreen();
                    dashboard.show((Stage) layout.getScene().getWindow());
                } else {
                    showAlert("Login Failed", "Invalid credentials.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        registerBtn.setOnAction(e -> {
            String user = username.getText();
            String pass = password.getText();
            boolean success = LoginManager.register(user, pass);
            showAlert("Registration", success ? "✅ User registered!" : "⚠ User already exists.");
        });
    }

    public VBox getLayout() {
        return layout;
    }

    public void show(Stage stage) {
        Scene scene = new Scene(getLayout(), 400, 300);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}