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

        // Buttons container
        HBox buttons = new HBox(10, addBtn, deleteBtn, undoBtn, searchBtn);
        buttons.setAlignment(Pos.CENTER);

        // Output label
        Label output = new Label();

// TableView setup
ObservableList<VaultEntry> passwordList = FXCollections.observableArrayList(VaultManager.getAllPasswords());
TableView<VaultEntry> table = new TableView<>();
TableColumn<VaultEntry, String> siteCol = new TableColumn<>("Website");
siteCol.setCellValueFactory(new PropertyValueFactory<>("site"));
TableColumn<VaultEntry, String> passCol = new TableColumn<>("Password");
passCol.setCellValueFactory(new PropertyValueFactory<>("password"));
table.getColumns().addAll(siteCol, passCol);
table.setItems(passwordList); // ← Attach the list
table.setPrefHeight(150);

// Add password action
addBtn.setOnAction(e -> {
    String site = siteField.getText();
    String pass = passField.getText();
    System.out.println("UI Add Clicked: " + site + " - " + pass);
    if (!site.isEmpty() && !pass.isEmpty()) {
        VaultManager.addPassword(site, pass);
        output.setText("✅ Password saved!");

        // ⬇ Refresh the table with updated data
        passwordList.setAll(VaultManager.getAllPasswords());

        // Optional: clear fields
        siteField.clear();
        passField.clear();
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
            output.setText("↩ Last deleted password restored!");
            passwordList.setAll(VaultManager.getAllPasswords());
        });

        // Add table and buttons to layout
        root.getChildren().addAll(title, siteField, passField, table, buttons, output);

        // Logout button
        Button logoutBtn = new Button("🔙 Logout");
        logoutBtn.setOnAction(e -> {
            LoginScreen login = new LoginScreen();
            Scene loginScene = new Scene(login.getLayout(), 400, 300);
            stage.setScene(loginScene);
        });
        buttons.getChildren().add(logoutBtn);

        //search button
        searchBtn.setOnAction(e -> {
    String site = siteField.getText().toLowerCase(); // normalize
    String result = VaultManager.getPassword(site);

    if (!site.isEmpty()) {
        output.setText(result.equals("Not Found")
            ? "🔍 No entry found."
            : "🔐 Password: " + result);
    } else {
        output.setText("⚠ Please enter a website to search.");
    }
});

        // Set Scene
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Dashboard - SecureVault");
        stage.show();
    }
}