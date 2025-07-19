package ui;

import core.VaultEntry;
import core.VaultManager;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class DashboardScreen {
    private static boolean darkMode = true;
    private static String currentTheme = "/dark.css";

    public static void show(Stage stage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        // 🔄 Light/Dark Toggle
        ToggleButton themeToggle = new ToggleButton("🌞 Light / Dark 🌙");
        themeToggle.setOnAction(e -> {
            darkMode = !darkMode;
            currentTheme = darkMode ? "/dark.css" : "/light.css";
            show(stage); // Reload to apply theme
        });

        Label title = new Label("🔐 SecureVault Dashboard");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setStyle("-fx-text-fill: #00BFFF;");

        // 🧾 Input form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);

        Label siteLabel = new Label("🌐 Website:");
        TextField siteField = new TextField();
        siteField.setPromptText("e.g. Gmail");

        Label passLabel = new Label("🔑 Password:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter password");

        CheckBox showPassword = new CheckBox("Show");
        TextField passVisible = new TextField();
        passVisible.setManaged(false);
        passVisible.setVisible(false);

        // Sync show/hide
        passField.textProperty().bindBidirectional(passVisible.textProperty());
        showPassword.setOnAction(e -> {
            boolean show = showPassword.isSelected();
            passField.setVisible(!show);
            passField.setManaged(!show);
            passVisible.setVisible(show);
            passVisible.setManaged(show);
        });

        form.add(siteLabel, 0, 0);
        form.add(siteField, 1, 0);
        form.add(passLabel, 0, 1);
        form.add(passField, 1, 1);
        form.add(passVisible, 1, 1);
        form.add(showPassword, 2, 1);

        // 📋 Table
        TableView<VaultEntry> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No saved passwords yet."));
        tableView.setPrefHeight(200);

        TableColumn<VaultEntry, String> siteCol = new TableColumn<>("🌐 Site");
        siteCol.setCellValueFactory(data -> data.getValue().siteProperty());

        TableColumn<VaultEntry, String> passCol = new TableColumn<>("🔑 Password");
        passCol.setCellValueFactory(data -> data.getValue().passwordProperty());

        tableView.getColumns().addAll(siteCol, passCol);

        // 🔘 Buttons
        Button addBtn = new Button("➕ Add");
        Button searchBtn = new Button("🔍 Search");
        Button deleteBtn = new Button("❌ Delete");
        Button undoBtn = new Button("↩ Undo");
        Button logoutBtn = new Button("🚪 Logout");

        HBox buttonBox = new HBox(10, addBtn, searchBtn, deleteBtn, undoBtn, logoutBtn);
        buttonBox.setAlignment(Pos.CENTER);

        Label output = new Label();
        output.setStyle("-fx-text-fill: #66ff66;");

        layout.getChildren().addAll(themeToggle, title, form, buttonBox, tableView, output);

        Scene scene = new Scene(layout, 650, 500);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(DashboardScreen.class.getResource(currentTheme).toExternalForm());

        stage.setScene(scene);
        stage.setTitle("SecureVault");
        stage.show();

        // 🔧 Actions
        addBtn.setOnAction(e -> {
            String site = siteField.getText();
            String password = passField.isVisible() ? passField.getText() : passVisible.getText();

            if (!site.isEmpty() && !password.isEmpty()) {
                VaultManager.addPassword(site, password);
                output.setText("✅ Added: " + site);
                siteField.clear();
                passField.clear();
                passVisible.clear();
                refreshTable(tableView);
            } else {
                output.setText("⚠ Enter both site and password.");
            }
        });

        searchBtn.setOnAction(e -> {
            String site = siteField.getText();
            if (!site.isEmpty()) {
                String result = VaultManager.getPassword(site);
                output.setText("🔍 Password: " + result);
            } else {
                output.setText("⚠ Enter a site to search.");
            }
        });

        deleteBtn.setOnAction(e -> {
            String site = siteField.getText();
            if (!site.isEmpty()) {
                VaultManager.deletePassword(site);
                output.setText("🗑 Deleted: " + site);
                refreshTable(tableView);
            }
        });

        undoBtn.setOnAction(e -> {
            VaultManager.undoDelete();
            output.setText("↩ Undo complete.");
            refreshTable(tableView);
        });

        logoutBtn.setOnAction(e -> {
            new LoginScreen().show(stage);
        });

        refreshTable(tableView);
    }

    private static void refreshTable(TableView<VaultEntry> table) {
        List<VaultEntry> entries = VaultManager.getAllPasswords();
        table.setItems(FXCollections.observableArrayList(entries));
    }
}