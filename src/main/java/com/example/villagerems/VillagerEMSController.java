package com.example.villagerems;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.villagerems.BlacksmithVillager;

public class VillagerEMSController {

    @FXML
    private TableView<VillagerEmployee> employeeTable;
    @FXML
    private TableColumn<VillagerEmployee, Integer> idColumn;
    @FXML
    private TableColumn<VillagerEmployee, String> nameColumn;
    @FXML
    private TableColumn<VillagerEmployee, String> villageColumn;
    @FXML
    private TableColumn<VillagerEmployee, Integer> levelColumn;
    @FXML
    private TableColumn<VillagerEmployee, Double> salaryColumn;
    @FXML
    private TableColumn<VillagerEmployee, String> typeColumn;
    @FXML
    private TextArea reportArea;

    private ObservableList<VillagerEmployee> employees = FXCollections.observableArrayList();
    private int nextId = 1003;

    @FXML
    public void initialize() {
        setupTableColumns();
        employeeTable.setItems(employees);
        initializeReportArea();
        addSampleData();
        updateStatistics();
    }

    private void initializeReportArea() {
        reportArea.setText("📋 Welcome to the Village Employee Management System!\n");
        reportArea.appendText("Total Employees: 0\n");
        reportArea.appendText("System initialized at: " + getCurrentTimestamp() + "\n\n");
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        villageColumn.setCellValueFactory(new PropertyValueFactory<>("village"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("experienceLevel"));
        salaryColumn.setCellValueFactory(cellData -> {
            VillagerEmployee emp = cellData.getValue();
            return new javafx.beans.property.SimpleDoubleProperty(emp.computeSalary()).asObject();
        });
        typeColumn.setCellValueFactory(cellData -> {
            VillagerEmployee emp = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(emp.getProfession());
        });

        // Format salary column with currency symbol
        salaryColumn.setCellFactory(column -> new TableCell<VillagerEmployee, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("💰 $%.2f", item));
                }
            }
        });
    }

    // FXML Event Handlers - these match the onAction references in the FXML

    @FXML
    private void onAddVillager() {
        Dialog<VillagerEmployee> dialog = createAddVillagerDialog();
        Optional<VillagerEmployee> result = dialog.showAndWait();

        if (result.isPresent()) {
            VillagerEmployee newEmployee = result.get();
            newEmployee.setEmployeeId(nextId++);
            employees.add(newEmployee);

            // Log the addition
            reportArea.appendText("➕ Added new villager: " + newEmployee.getName() + "\n");
            reportArea.appendText("   Type: " + newEmployee.getProfession() + "\n");
            reportArea.appendText("   Village: " + newEmployee.getVillage() + "\n");
            reportArea.appendText("   Level: " + newEmployee.getExperienceLevel() + "\n");
            reportArea.appendText("   Added at: " + getCurrentTimestamp() + "\n\n");

            updateStatistics();
        }
    }

    @FXML
    private void onEditVillager() {
        VillagerEmployee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a villager to edit.");
            return;
        }

        Dialog<VillagerEmployee> dialog = createEditVillagerDialog(selected);
        Optional<VillagerEmployee> result = dialog.showAndWait();

        if (result.isPresent()) {
            VillagerEmployee edited = result.get();

            // Update basic properties
            selected.setName(edited.getName());
            selected.setVillage(edited.getVillage());
            selected.setExperienceLevel(edited.getExperienceLevel());

            // Update specific properties based on type
// Update specific properties based on type
            if (selected instanceof FarmerVillager && edited instanceof FarmerVillager) {
                FarmerVillager s = (FarmerVillager) selected, e = (FarmerVillager) edited;
                s.setHourlyRate(e.getHourlyRate());
                s.setHoursWorked(e.getHoursWorked());
                s.setSpecialty(e.getSpecialty());
            } else if (selected instanceof BlacksmithVillager && edited instanceof BlacksmithVillager) {
                BlacksmithVillager s = (BlacksmithVillager) selected, e = (BlacksmithVillager) edited;
                s.setHourlyRate(e.getHourlyRate());
                s.setHoursWorked(e.getHoursWorked());
                s.setSpecialty(e.getSpecialty());
            } else if (selected instanceof LibrarianVillager && edited instanceof LibrarianVillager) {
                LibrarianVillager s = (LibrarianVillager) selected, e = (LibrarianVillager) edited;
                s.setHourlyRate(e.getHourlyRate());
                s.setHoursWorked(e.getHoursWorked());
                s.setSpecialty(e.getSpecialty());
            }

            employeeTable.refresh();
            reportArea.appendText("✏️ Edited villager: " + selected.getName() + "\n");
            reportArea.appendText("   Updated at: " + getCurrentTimestamp() + "\n\n");
            updateStatistics();
        }
    }

    @FXML
    private void onDeleteVillager() {
        VillagerEmployee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a villager to remove.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Remove Villager");
        confirmation.setContentText("Are you sure you want to remove " + selected.getName() + "?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            employees.remove(selected);
            reportArea.appendText("❌ Removed villager: " + selected.getName() + "\n");
            reportArea.appendText("   Former village: " + selected.getVillage() + "\n");
            reportArea.appendText("   Removed at: " + getCurrentTimestamp() + "\n\n");
            updateStatistics();
        }
    }

    @FXML
    private void onLevelUp() {
        VillagerEmployee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a villager to level up.");
            return;
        }

        int oldLevel = selected.getExperienceLevel();
        double oldSalary = selected.computeSalary();

        // Level up the villager
        if (selected instanceof EmployeeActions) {
            ((EmployeeActions) selected).levelUp();
        }

        employeeTable.refresh();

        reportArea.appendText("⬆️ LEVEL UP! " + selected.getName() + "\n");
        reportArea.appendText("   Level: " + oldLevel + " → " + selected.getExperienceLevel() + "\n");
        reportArea.appendText("   Salary: $" + String.format("%.2f", oldSalary) + " → $" + String.format("%.2f", selected.computeSalary()) + "\n");

        if (selected instanceof FarmerVillager) {
            FarmerVillager farmer = (FarmerVillager) selected;
            reportArea.appendText("   New Hourly Rate: $" + String.format("%.2f", farmer.getHourlyRate()) + "\n");
        } else if (selected instanceof BlacksmithVillager) {
            BlacksmithVillager blacksmith = (BlacksmithVillager) selected;
            reportArea.appendText("   New Monthly Rate: $" + String.format("%.2f", blacksmith.getMonthlyRate()) + "\n");
        }

        reportArea.appendText("   Leveled up at: " + getCurrentTimestamp() + "\n\n");
        updateStatistics();
    }

    @FXML
    private void onCalculateSalary() {
        VillagerEmployee selected = employeeTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            // Calculate total payroll for all employees
            double totalPayroll = employees.stream()
                    .mapToDouble(VillagerEmployee::computeSalary)
                    .sum();

            reportArea.appendText("💰 TOTAL PAYROLL CALCULATION\n");
            reportArea.appendText("============================\n");
            reportArea.appendText("Total Monthly Payroll: $" + String.format("%.2f", totalPayroll) + "\n");
            reportArea.appendText("Total Employees: " + employees.size() + "\n");
            if (!employees.isEmpty()) {
                reportArea.appendText("Average Salary: $" + String.format("%.2f", totalPayroll / employees.size()) + "\n");
            }
            reportArea.appendText("Calculated at: " + getCurrentTimestamp() + "\n\n");
        } else {
            // Calculate salary for selected employee
            double salary = selected.computeSalary();
            reportArea.appendText("💰 SALARY CALCULATION for " + selected.getName() + "\n");
            reportArea.appendText("========================================\n");

            if (selected instanceof FarmerVillager) {
                FarmerVillager farmer = (FarmerVillager) selected;
                reportArea.appendText("Hourly Rate: $" + String.format("%.2f", farmer.getHourlyRate()) + "\n");
                reportArea.appendText("Hours Worked: " + farmer.getHoursWorked() + "\n");
                reportArea.appendText("Experience Level: " + farmer.getExperienceLevel() + "\n");
                reportArea.appendText("Crop Specialty: " + farmer.getCropSpecialty() + "\n");
            } else if (selected instanceof BlacksmithVillager) {
                BlacksmithVillager blacksmith = (BlacksmithVillager) selected;
                reportArea.appendText("Monthly Rate: $" + String.format("%.2f", blacksmith.getMonthlyRate()) + "\n");
                reportArea.appendText("Experience Level: " + blacksmith.getExperienceLevel() + "\n");
                reportArea.appendText("Specialty: " + blacksmith.getSpecialty() + "\n");
            }

            reportArea.appendText("Calculated Salary: $" + String.format("%.2f", salary) + "\n");
            reportArea.appendText("Calculated at: " + getCurrentTimestamp() + "\n\n");
        }
    }

    @FXML
    private void onGenerateReport() {
        reportArea.appendText("📊 COMPREHENSIVE VILLAGE EMPLOYEE REPORT\n");
        reportArea.appendText("=========================================\n");
        reportArea.appendText("Generated: " + getCurrentTimestamp() + "\n\n");

        // Calculate statistics
        long farmers = employees.stream().filter(e -> e instanceof FarmerVillager).count();
        long blacksmiths = employees.stream().filter(e -> e instanceof BlacksmithVillager).count();
        double totalPayroll = employees.stream().mapToDouble(VillagerEmployee::computeSalary).sum();

        // Summary section
        reportArea.appendText("SUMMARY:\n");
        reportArea.appendText("--------\n");
        reportArea.appendText("Total Employees: " + employees.size() + "\n");
        reportArea.appendText("Farmers: " + farmers + "\n");
        reportArea.appendText("Blacksmiths: " + blacksmiths + "\n");
        reportArea.appendText("Total Monthly Payroll: $" + String.format("%.2f", totalPayroll) + "\n");
        if (!employees.isEmpty()) {
            reportArea.appendText("Average Salary: $" + String.format("%.2f", totalPayroll / employees.size()) + "\n");
        }
        reportArea.appendText("\n");

        // Detailed employee list
        if (!employees.isEmpty()) {
            reportArea.appendText("EMPLOYEE DETAILS:\n");
            reportArea.appendText("-----------------\n");
            for (VillagerEmployee emp : employees) {
                reportArea.appendText("ID: " + emp.getEmployeeId() + " | ");
                reportArea.appendText(emp.getName() + " | ");
                reportArea.appendText("Village: " + emp.getVillage() + " | ");
                reportArea.appendText("Level: " + emp.getExperienceLevel() + " | ");
                reportArea.appendText("Profession: " + emp.getProfession() + " | ");
                reportArea.appendText("Salary: $" + String.format("%.2f", emp.computeSalary()) + "\n");

                if (emp instanceof FarmerVillager) {
                    FarmerVillager farmer = (FarmerVillager) emp;
                    reportArea.appendText("   └─ Specialty: " + farmer.getCropSpecialty() +
                            ", Rate: $" + String.format("%.2f", farmer.getHourlyRate()) + "/hr, " +
                            "Hours: " + farmer.getHoursWorked() + "/week\n");
                    reportArea.appendText("   └─ Report: " + farmer.submitReport() + "\n");
                } else if (emp instanceof BlacksmithVillager) {
                    BlacksmithVillager blacksmith = (BlacksmithVillager) emp;
                    reportArea.appendText("   └─ Specialty: " + blacksmith.getSpecialty() +
                            ", Monthly Rate: $" + String.format("%.2f", blacksmith.getMonthlyRate()) + "\n");
                    reportArea.appendText("   └─ Report: " + blacksmith.submitReport() + "\n");
                }
                reportArea.appendText("\n");
            }
        }
        reportArea.appendText("=== End of Report ===\n\n");
    }

    @FXML
    private void onRefresh() {
        employeeTable.refresh();
        updateStatistics();
        reportArea.appendText("🔄 Table refreshed at: " + getCurrentTimestamp() + "\n\n");
    }

    // Dialog creation methods

    private Dialog<VillagerEmployee> createAddVillagerDialog() {
        Dialog<VillagerEmployee> dialog = new Dialog<>();
        dialog.setTitle("Add New Villager");
        dialog.setHeaderText("Enter villager details:");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = createVillagerForm(null);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return extractVillagerFromForm(grid);
            }
            return null;
        });

        return dialog;
    }

    private Dialog<VillagerEmployee> createEditVillagerDialog(VillagerEmployee villager) {
        Dialog<VillagerEmployee> dialog = new Dialog<>();
        dialog.setTitle("Edit Villager");
        dialog.setHeaderText("Edit villager details:");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = createVillagerForm(villager);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return extractVillagerFromForm(grid);
            }
            return null;
        });

        return dialog;
    }

    private GridPane createVillagerForm(VillagerEmployee villager) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Form fields
        TextField nameField = new TextField();
        TextField villageField = new TextField();
        Spinner<Integer> levelSpinner = new Spinner<>(1, 10, 1);
        TextField rateField = new TextField();
        Spinner<Integer> hoursSpinner = new Spinner<>(20, 60, 40);
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Farmer", "Blacksmith");
        TextField specialtyField = new TextField();

        // Populate fields if editing existing villager
        if (villager != null) {
            nameField.setText(villager.getName());
            villageField.setText(villager.getVillage());
            levelSpinner.getValueFactory().setValue(villager.getExperienceLevel());

            if (villager instanceof FarmerVillager) {
                FarmerVillager farmer = (FarmerVillager) villager;
                typeCombo.setValue("Farmer");
                rateField.setText(String.valueOf(farmer.getHourlyRate()));
                hoursSpinner.getValueFactory().setValue(farmer.getHoursWorked());
                specialtyField.setText(farmer.getCropSpecialty());
            } else if (villager instanceof BlacksmithVillager) {
                BlacksmithVillager blacksmith = (BlacksmithVillager) villager;
                typeCombo.setValue("Blacksmith");
                rateField.setText(String.valueOf(blacksmith.getMonthlyRate()));
                hoursSpinner.getValueFactory().setValue(40); // Default hours for display
                specialtyField.setText(blacksmith.getSpecialty());
            }
        } else {
            // Default values for new villager
            typeCombo.setValue("Farmer");
            rateField.setText("15.0");
            specialtyField.setText("Wheat");
        }

        // Add fields to grid
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Village:"), 0, 1);
        grid.add(villageField, 1, 1);
        grid.add(new Label("Experience Level:"), 0, 2);
        grid.add(levelSpinner, 1, 2);
        grid.add(new Label("Type:"), 0, 3);
        grid.add(typeCombo, 1, 3);
        grid.add(new Label("Rate ($/hr or $/month):"), 0, 4);
        grid.add(rateField, 1, 4);
        grid.add(new Label("Hours/Week (Farmers only):"), 0, 5);
        grid.add(hoursSpinner, 1, 5);
        grid.add(new Label("Specialty/Crop:"), 0, 6);
        grid.add(specialtyField, 1, 6);

        // Store fields in grid for later retrieval
        grid.setUserData(new Object[]{nameField, villageField, levelSpinner, rateField, hoursSpinner, typeCombo, specialtyField});

        return grid;
    }

    private VillagerEmployee extractVillagerFromForm(GridPane grid) {
        Object[] fields = (Object[]) grid.getUserData();
        TextField nameField = (TextField) fields[0];
        TextField villageField = (TextField) fields[1];
        Spinner<Integer> levelSpinner = (Spinner<Integer>) fields[2];
        TextField rateField = (TextField) fields[3];
        Spinner<Integer> hoursSpinner = (Spinner<Integer>) fields[4];
        ComboBox<String> typeCombo = (ComboBox<String>) fields[5];
        TextField specialtyField = (TextField) fields[6];

        try {
            String name = nameField.getText().trim();
            String village = villageField.getText().trim();
            int level = levelSpinner.getValue();
            double rate = Double.parseDouble(rateField.getText().trim());
            int hours = hoursSpinner.getValue();
            String type = typeCombo.getValue();
            String specialty = specialtyField.getText().trim();

            // Validation
            if (name.isEmpty() || village.isEmpty()) {
                showAlert("Invalid Input", "Name and Village cannot be empty.");
                return null;
            }

            if (specialty.isEmpty()) {
                specialty = "Farmer".equals(type) ? "Wheat" : "Tools";
            }

            // Create appropriate villager type
            if ("Farmer".equals(type)) {
                FarmerVillager farmer = new FarmerVillager(0, name, village, level, rate, specialty);
                farmer.setHoursWorked(hours);
                return farmer;
            } else if ("Blacksmith".equals(type)) {
                return new BlacksmithVillager(0, name, village, level, rate, specialty);
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for rate.");
            return null;
        }
        return null;
    }

    // Utility methods

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateStatistics() {
        if (employees.isEmpty()) {
            return;
        }

        long farmers = employees.stream().filter(e -> e instanceof FarmerVillager).count();
        long blacksmiths = employees.stream().filter(e -> e instanceof BlacksmithVillager).count();
        double totalPayroll = employees.stream().mapToDouble(VillagerEmployee::computeSalary).sum();

        String stats = String.format("📊 Current Stats: %d Total (%d Farmers, %d Blacksmiths) | Payroll: $%.2f\n",
                employees.size(), farmers, blacksmiths, totalPayroll);

        // Update or append statistics in report area
        String currentText = reportArea.getText();
        if (currentText.contains("📊 Current Stats:")) {
            String[] lines = currentText.split("\n");
            StringBuilder newText = new StringBuilder();
            boolean replaced = false;
            for (String line : lines) {
                if (line.contains("📊 Current Stats:") && !replaced) {
                    newText.append(stats);
                    replaced = true;
                } else {
                    newText.append(line).append("\n");
                }
            }
            reportArea.setText(newText.toString());
        } else {
            reportArea.appendText(stats);
        }
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void addSampleData() {
        // Add sample villagers
        FarmerVillager farmer = new FarmerVillager(1001, "Bob the Farmer", "Oakville", 3, 15.0, "Wheat");
        farmer.setHoursWorked(40);
        employees.add(farmer);

        BlacksmithVillager blacksmith = new BlacksmithVillager(1002, "Iron Mike", "Stonehaven", 5, 2500.0, "Weapons");
        employees.add(blacksmith);

        reportArea.appendText("✅ Sample data loaded successfully!\n");
        reportArea.appendText("   - Bob the Farmer (Level 3, Wheat specialty)\n");
        reportArea.appendText("   - Iron Mike (Level 5, Weapons specialty)\n\n");
    }
}