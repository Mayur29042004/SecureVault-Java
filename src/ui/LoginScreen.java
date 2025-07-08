package ui;

import auth.LoginManager;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class LoginScreen {
    private VBox layout;

    public LoginScreen() {
        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Text title = new Text("🔐 SecureVault Login");

        TextField username = new TextField();
        username.setPromptText("Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        layout.getChildren().addAll(title, username, password, loginBtn, registerBtn);

        // Action placeholders
        loginBtn.setOnAction(e -> {
    String user = username.getText();
    String pass = password.getText();
    boolean success = LoginManager.login(user, pass);
    System.out.println(success ? "Login successful!" : "Invalid credentials.");
});

registerBtn.setOnAction(e -> {
    String user = username.getText();
    String pass = password.getText();
    boolean success = LoginManager.register(user, pass);
    System.out.println(success ? "User registered!" : "User already exists.");
});
       
    }

    public VBox getLayout() {
        return layout;
    }
}