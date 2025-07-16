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

import java.net.URL;
import java.util.List;

public class DashboardScreen {

    public static void show(Stage stage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("🔐 SecureVault Dashboard");
        title.setStyle("-fx-font-size: 22px; -fx-text-fill: #3f51b5;");

        // Input fields
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);

        Label siteLabel = new Label("🌐 Website:");
        TextField siteField = new TextField();
        siteField.setPromptText("e.g. Gmail");

        Label passLabel = new Label("🔑 Password:");
        TextField passField = new TextField();
        passField.setPromptText("Enter password");

        form.add(siteLabel, 0, 0);
        form.add(siteField, 1, 0);
        form.add(passLabel, 0, 1);
        form.add(passField, 1, 1);

        // TableView setup
        TableView<VaultEntry> tableView = new TableView<>();

        TableColumn<VaultEntry, String> siteCol = new TableColumn<>("🌐 Site");
        siteCol.setCellValueFactory(data -> data.getValue().siteProperty());

        TableColumn<VaultEntry, String> passCol = new TableColumn<>("🔑 Password");
        passCol.setCellValueFactory(data -> data.getValue().passwordProperty());

        tableView.getColumns().addAll(siteCol, passCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No saved passwords yet."));
        tableView.setPrefHeight(200);

        // Buttons
        Button addBtn = new Button("➕ Add");
        Button searchBtn = new Button("🔍 Search");
        Button deleteBtn = new Button("❌ Delete");
        Button undoBtn = new Button("↩ Undo");
        Button logoutBtn = new Button("🚪 Logout");

        HBox buttonBox = new HBox(10, addBtn, searchBtn, deleteBtn, undoBtn, logoutBtn);
        buttonBox.setAlignment(Pos.CENTER);

        Label output = new Label("");
        output.setStyle("-fx-text-fill: green;");

        layout.getChildren().addAll(title, form, buttonBox, tableView, output);

        Scene scene = new Scene(layout, 600, 500);

        // ✅ Safe stylesheet load (optional)
        try {
            URL cssUrl = DashboardScreen.class.getResource("/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.out.println("⚠ styles.css not found.");
            }
        } catch (Exception e) {
            System.out.println("⚠ Error loading styles.css: " + e.getMessage());
        }

        stage.setScene(scene);
        stage.setTitle("SecureVault");
        stage.show();

        // Button actions
        addBtn.setOnAction(e -> {
            String site = siteField.getText().trim();
            String password = passField.getText().trim();
            if (!site.isEmpty() && !password.isEmpty()) {
                VaultManager.addPassword(site, password);
                output.setText("✅ Added password for: " + site);
                refreshTable(tableView);
                siteField.clear();
                passField.clear();
            } else {
                output.setText("⚠ Enter both site and password.");
            }
        });

        searchBtn.setOnAction(e -> {
            String site = siteField.getText().trim();
            if (!site.isEmpty()) {
                String result = VaultManager.getPassword(site);
                output.setText(result != null ? "🔍 Password: " + result : "❌ No entry found.");
            } else {
                output.setText("⚠ Enter a site to search.");
            }
        });

        deleteBtn.setOnAction(e -> {
            String site = siteField.getText().trim();
            if (!site.isEmpty()) {
                VaultManager.deletePassword(site);
                output.setText("🗑 Deleted: " + site);
                refreshTable(tableView);
            } else {
                output.setText("⚠ Enter a site to delete.");
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