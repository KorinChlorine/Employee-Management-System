package com.example.villagerems;// VillagerEMS.java - Main JavaFX Application
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
        import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.Optional;

public class VillagerEMS extends Application {
    private TableView<VillagerEmployee> employeeTable;
    private ObservableList<VillagerEmployee> employees;
    private int nextEmployeeId = 1001;
    private TextArea reportArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🏘️ Minecraft Villager Employee Management System");

        // Initialize data
        employees = FXCollections.observableArrayList();
        initializeSampleData();

        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setBackground(new Background(new BackgroundFill(
                Color.rgb(139, 69, 19, 0.3), CornerRadii.EMPTY, Insets.EMPTY)));

        // Header
        Label headerLabel = new Label("🏘️ Village Employee Management System");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        headerLabel.setTextFill(Color.DARKGREEN);
        headerLabel.setPadding(new Insets(20));

        // Create main content
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        // Control buttons
        HBox buttonBox = createButtonPanel();

        // Employee table
        employeeTable = createEmployeeTable();

        // Report area
        reportArea = new TextArea();
        reportArea.setPrefRowCount(8);
        reportArea.setEditable(false);
        reportArea.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");
        reportArea.setText("📋 Welcome to the Village Employee Management System!\n" +
                "Select an employee and use the buttons above to manage villagers.\n");

        content.getChildren().addAll(buttonBox, employeeTable,
                new Label("📋 Reports & Information:"), reportArea);

        mainLayout.setTop(headerLabel);
        mainLayout.setCenter(content);

        Scene scene = new Scene(mainLayout, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createButtonPanel() {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        Button addButton = new Button("➕ Add Villager");
        Button editButton = new Button("✏️ Edit Villager");
        Button deleteButton = new Button("❌ Remove Villager");
        Button salaryButton = new Button("💰 Calculate Salary");
        Button reportButton = new Button("📊 Generate Report");
        Button levelUpButton = new Button("⬆️ Level Up");
        Button refreshButton = new Button("🔄 Refresh");

        // Style buttons
        String buttonStyle = "-fx-background-color: #8B4513; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 16 8 16;";
        addButton.setStyle(buttonStyle);
        editButton.setStyle(buttonStyle);
        deleteButton.setStyle(buttonStyle);
        salaryButton.setStyle(buttonStyle);
        reportButton.setStyle(buttonStyle);
        levelUpButton.setStyle(buttonStyle);
        refreshButton.setStyle(buttonStyle);

        // Event handlers
        addButton.setOnAction(e -> showAddEmployeeDialog());
        editButton.setOnAction(e -> editSelectedEmployee());
        deleteButton.setOnAction(e -> deleteSelectedEmployee());
        salaryButton.setOnAction(e -> calculateSalary());
        reportButton.setOnAction(e -> generateReport());
        levelUpButton.setOnAction(e -> levelUpEmployee());
        refreshButton.setOnAction(e -> employeeTable.refresh());

        buttonBox.getChildren().addAll(addButton, editButton, deleteButton,
                salaryButton, reportButton, levelUpButton, refreshButton);
        return buttonBox;
    }

    private TableView<VillagerEmployee> createEmployeeTable() {
        TableView<VillagerEmployee> table = new TableView<>();
        table.setItems(employees);

        TableColumn<VillagerEmployee, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getEmployeeId())));

        TableColumn<VillagerEmployee, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<VillagerEmployee, String> professionCol = new TableColumn<>("Profession");
        professionCol.setCellValueFactory(data -> data.getValue().professionProperty());

        TableColumn<VillagerEmployee, String> villageCol = new TableColumn<>("Village");
        villageCol.setCellValueFactory(data -> data.getValue().villageProperty());

        TableColumn<VillagerEmployee, String> levelCol = new TableColumn<>("Level");
        levelCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getExperienceLevel())));

        TableColumn<VillagerEmployee, String> workstationCol = new TableColumn<>("Workstation");
        workstationCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getWorkstation()));

        table.getColumns().addAll(idCol, nameCol, professionCol, villageCol, levelCol, workstationCol);
        table.setPrefHeight(300);

        return table;
    }

    private void initializeSampleData() {
        employees.add(new FarmerVillager(1001, "Bob the Farmer", "Oakville", 3, 15.0, "Wheat"));
        employees.add(new BlacksmithVillager(1002, "Iron Mike", "Stonehaven", 5, 2500.0, "Weapons"));
        employees.add(new LibrarianVillager(1003, "Sage Alexandria", "Bookton", 4, 18.0, "Enchantments"));
        employees.add(new FarmerVillager(1004, "Carrot Kate", "Greenfield", 2, 12.0, "Carrots"));
        employees.add(new BlacksmithVillager(1005, "Steel Steve", "Forgetown", 6, 3000.0, "Tools"));
        nextEmployeeId = 1006;
    }

    private void showAddEmployeeDialog() {
        Dialog<VillagerEmployee> dialog = new Dialog<>();
        dialog.setTitle("Add New Villager Employee");
        dialog.setHeaderText("Enter villager details:");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        TextField villageField = new TextField();
        TextField levelField = new TextField("1");
        ComboBox<String> professionBox = new ComboBox<>();
        professionBox.getItems().addAll("Farmer", "Blacksmith", "Librarian");
        professionBox.setValue("Farmer");

        TextField specialtyField = new TextField();
        TextField rateField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Village:"), 0, 1);
        grid.add(villageField, 1, 1);
        grid.add(new Label("Level:"), 0, 2);
        grid.add(levelField, 1, 2);
        grid.add(new Label("Profession:"), 0, 3);
        grid.add(professionBox, 1, 3);
        grid.add(new Label("Specialty/Dept:"), 0, 4);
        grid.add(specialtyField, 1, 4);
        grid.add(new Label("Rate/Salary:"), 0, 5);
        grid.add(rateField, 1, 5);

        dialogPane.setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String name = nameField.getText();
                    String village = villageField.getText();
                    int level = Integer.parseInt(levelField.getText());
                    String profession = professionBox.getValue();
                    double rate = Double.parseDouble(rateField.getText());

                    switch (profession) {
                        case "Farmer":
                            return new FarmerVillager(nextEmployeeId++, name, village, level, rate, specialtyField.getText());
                        case "Blacksmith":
                            return new BlacksmithVillager(nextEmployeeId++, name, village, level, rate, specialtyField.getText());
                        case "Librarian":
                            return new LibrarianVillager(nextEmployeeId++, name, village, level, rate, specialtyField.getText());
                    }
                } catch (NumberFormatException e) {
                    showAlert("Invalid input", "Please enter valid numbers for level and rate.");
                }
            }
            return null;
        });

        Optional<VillagerEmployee> result = dialog.showAndWait();
        result.ifPresent(employee -> {
            employees.add(employee);
            reportArea.appendText("✅ Added new villager: " + employee.getName() + "\n");
        });
    }

    private void editSelectedEmployee() {
        VillagerEmployee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a villager to edit.");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Edit Villager: " + selected.getName());

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(selected.getName());
        TextField villageField = new TextField(selected.getVillage());
        TextField levelField = new TextField(String.valueOf(selected.getExperienceLevel()));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Village:"), 0, 1);
        grid.add(villageField, 1, 1);
        grid.add(new Label("Level:"), 0, 2);
        grid.add(levelField, 1, 2);

        dialogPane.setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    selected.setName(nameField.getText());
                    selected.setVillage(villageField.getText());
                    selected.setExperienceLevel(Integer.parseInt(levelField.getText()));
                } catch (NumberFormatException e) {
                    showAlert("Invalid input", "Please enter a valid number for level.");
                }
            }
            return null;
        });

        dialog.showAndWait();
        employeeTable.refresh();
        reportArea.appendText("✏️ Updated villager: " + selected.getName() + "\n");
    }

    private void deleteSelectedEmployee() {
        VillagerEmployee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a villager to remove.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Removal");
        alert.setHeaderText("Remove villager: " + selected.getName() + "?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            employees.remove(selected);
            reportArea.appendText("❌ Removed villager: " + selected.getName() + "\n");
        }
    }

    private void calculateSalary() {
        VillagerEmployee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a villager to calculate salary.");
            return;
        }

        if (selected instanceof EmployeeActions) {
            double salary = ((EmployeeActions) selected).computeSalary();
            reportArea.appendText("💰 " + selected.getName() + "'s salary: " +
                    String.format("%.2f emeralds\n", salary));
        }
    }

    private void generateReport() {
        VillagerEmployee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a villager to generate report.");
            return;
        }

        if (selected instanceof EmployeeActions) {
            String report = ((EmployeeActions) selected).submitReport();
            String jobDesc = ((EmployeeActions) selected).getJobDescription();
            reportArea.appendText("📊 VILLAGER REPORT:\n" + report + "\n" +
                    "Job: " + jobDesc + "\n" +
                    "Workstation: " + selected.getWorkstation() + "\n\n");
        }
    }

    private void levelUpEmployee() {
        VillagerEmployee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a villager to level up.");
            return;
        }

        if (selected instanceof EmployeeActions) {
            int oldLevel = selected.getExperienceLevel();
            ((EmployeeActions) selected).levelUp();
            employeeTable.refresh();
            reportArea.appendText("⬆️ " + selected.getName() + " leveled up from " +
                    oldLevel + " to " + selected.getExperienceLevel() + "!\n");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}