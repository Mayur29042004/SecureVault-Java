package ui;

import core.VaultEntry;
import core.VaultManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class DashboardScreen {

    public void showDashboard(Stage stage) {
        // Layouts
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        // Title
        Label title = new Label("🔐 SecureVault Dashboard");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Input fields
        TextField siteField = new TextField();
        siteField.setPromptText("Enter website (e.g., gmail)");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter password");

        // Buttons
        Button addBtn = new Button("➕ Add");
        Button deleteBtn = new Button("❌ Delete");
        Button undoBtn = new Button("↩ Undo Delete");
        Button searchBtn = new Button("🔍 Search");

        // Output label
        Label output = new Label();

        // Add password action
        addBtn.setOnAction(e -> {
            String site = siteField.getText();
            String pass = passField.getText();
            if (!site.isEmpty() && !pass.isEmpty()) {
                VaultManager.addPassword(site, pass);
                output.setText("✅ Password saved!");
            } else {
                output.setText("⚠ Fields cannot be empty.");
            }
        });

        // Delete password
        deleteBtn.setOnAction(e -> {
            String site = siteField.getText();
            VaultManager.deletePassword(site);
            output.setText("❌ Password deleted (if existed).");
        });

        // Undo delete
        undoBtn.setOnAction(e -> {
            VaultManager.undoDelete();
            output.setText("↩ Undo completed.");
        });

        // Search password
        searchBtn.setOnAction(e -> {
            String site = siteField.getText();
            String result = VaultManager.getPassword(site);
            output.setText(result.equals("Not Found") ? "🔍 No entry found." : "🔐 Password: " + result);
        });

        // UI Layout
        HBox buttons = new HBox(10, addBtn, deleteBtn, searchBtn, undoBtn);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, siteField, passField, buttons, output);

        // Set Scene
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Dashboard - SecureVault");
        stage.show();

        // TableView setup
TableView<VaultEntry> table = new TableView<>();
TableColumn<VaultEntry, String> siteCol = new TableColumn<>("Website");
TableColumn<VaultEntry, String> passCol = new TableColumn<>("Password");

siteCol.setCellValueFactory(new PropertyValueFactory<>("site"));
passCol.setCellValueFactory(new PropertyValueFactory<>("password"));

table.getColumns().addAll(siteCol, passCol);
table.setItems(FXCollections.observableArrayList(VaultManager.getAllPasswords()));
table.setPrefHeight(150);

// Add to layout
        root.getChildren().addAll(table);

        // Logout button
        Button logoutBtn = new Button("🔙 Logout");
        logoutBtn.setOnAction(e -> {
            LoginScreen login = new LoginScreen();
            Scene loginScene = new Scene(login.getLayout(), 400, 300);
            Stage stageWindow = (Stage) root.getScene().getWindow();
            stageWindow.setScene(loginScene);
        });
        buttons.getChildren().add(logoutBtn);
    }
}