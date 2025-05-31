package com.example.villagerems;

import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VillagerEMSController {
    @FXML private VBox sidebar;
    @FXML private ImageView villagerImage;
    @FXML private Label villagerName;
    @FXML private Label villagerType;
    @FXML private Label villagerVillage;
    @FXML private Label villagerLevel;
    @FXML private Label villagerSpecialty;
    @FXML private TableView<VillagerEmployee> employeeTable;
    @FXML private TableColumn<VillagerEmployee, Integer> idColumn;
    @FXML private TableColumn<VillagerEmployee, String> nameColumn;
    @FXML private TableColumn<VillagerEmployee, String> villageColumn;
    @FXML private TableColumn<VillagerEmployee, Integer> levelColumn;
    @FXML private TableColumn<VillagerEmployee, Double> salaryColumn;
    @FXML private TableColumn<VillagerEmployee, String> typeColumn;
    @FXML private TextFlow reportFlow;
    @FXML private TextField searchField;
    @FXML private TableColumn<VillagerEmployee, String> workstationColumn;

    private ObservableList<VillagerEmployee> villagers = FXCollections.observableArrayList();
    private int nextId = 1006;

    @FXML
    public void initialize() {
        setupTableColumns();
        employeeTable.setItems(villagers);
        addSampleData();
        initializeReportArea();
        updateStatistics();
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            updateSidebar(newSel);
            villagerImage.setPreserveRatio(true);
            villagerImage.setFitWidth(180);
            villagerImage.setFitHeight(240);
        });
    }

    private void setupTableColumns() {
        idColumn.setText("Villager ID");
        nameColumn.setText("Villager Name");
        villageColumn.setText("Village");
        levelColumn.setText("XP Level");
        salaryColumn.setText("Emeralds/Month");
        typeColumn.setText("Profession");

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
        workstationColumn.setCellValueFactory(cellData -> {
            VillagerEmployee emp = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                    emp instanceof EmployeeActions ? ((EmployeeActions) emp).getWorkstation() : "N/A"
            );
        });

        salaryColumn.setCellFactory(column -> new TableCell<VillagerEmployee, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("💎 " + String.format("%.2f", item));
                }
            }
        });
    }

    private void addSampleData() {
        villagers.addAll(
                new FarmerVillager(nextId++, "Bob", "Plains", 3, 15.0, "Wheat"),
                new BlacksmithVillager(nextId++, "Anna", "Mountains", 4, 20.0, 40, "Tools"),
                new ButcherVillager(nextId++, "Greg", "Savanna", 2, 12.0, 35, "Meat"),
                new ClericVillager(nextId++, "Sister May", "Swamp", 5, 18.0, 38, "Potions"),
                new LibrarianVillager(nextId++, "Eve", "Taiga", 6, 16.0, 40, "Enchantments")
        );
    }

    private void initializeReportArea() {
        reportFlow.getChildren().clear();

        Text welcome = new Text("📜 Welcome to the Minecraft Village Workforce Ledger\n");
        welcome.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-fill: #388E3C;");
        reportFlow.getChildren().add(welcome);

        Text divider = new Text("========================================\n");
        divider.setStyle("-fx-fill: #BDBDBD;");
        reportFlow.getChildren().add(divider);

        if (villagers.isEmpty()) {
            Text noVillagers = new Text("\nNo villagers registered yet. Use the buttons above to recruit!\n");
            noVillagers.setStyle("-fx-font-style: italic; -fx-fill: #888;");
            reportFlow.getChildren().add(noVillagers);
        }

        Text bootTime = new Text("\n⏰ System booted at: " + getCurrentTimestamp() + "\n");
        bootTime.setStyle("-fx-fill: #555;");
        reportFlow.getChildren().add(bootTime);

        Text endDivider = new Text("========================================\n\n");
        endDivider.setStyle("-fx-fill: #BDBDBD;");
        reportFlow.getChildren().add(endDivider);
    }

    private void updateSidebar(VillagerEmployee villager) {
        if (villager == null) {
            sidebar.setVisible(false);
            return;
        }
        sidebar.setVisible(true);
        villagerName.setText(villager.getName());
        villagerType.setText("Type: " + villager.getProfession());
        villagerVillage.setText("Village: " + villager.getVillage());
        villagerLevel.setText("XP Level: " + villager.getExperienceLevel());

        if (villager instanceof FarmerVillager) {
            villagerSpecialty.setText("Crop: " + ((FarmerVillager) villager).getCropSpecialty());
            villagerImage.setImage(new Image(getClass().getResourceAsStream("/images/Farmer.png")));
        } else if (villager instanceof BlacksmithVillager) {
            villagerSpecialty.setText("Specialty: " + ((BlacksmithVillager) villager).getSpecialty());
            villagerImage.setImage(new Image(getClass().getResourceAsStream("/images/Blacksmith.png")));
        } else if (villager instanceof ButcherVillager) {
            villagerSpecialty.setText("Specialty: " + ((ButcherVillager) villager).getSpecialty());
            villagerImage.setImage(new Image(getClass().getResourceAsStream("/images/Butcher.png")));
        } else if (villager instanceof ClericVillager) {
            villagerSpecialty.setText("Specialty: " + ((ClericVillager) villager).getSpecialty());
            villagerImage.setImage(new Image(getClass().getResourceAsStream("/images/Cleric.png")));
        } else if (villager instanceof LibrarianVillager) {
            villagerSpecialty.setText("Knowledge: " + ((LibrarianVillager) villager).getKnowledgeArea());
            villagerImage.setImage(new Image(getClass().getResourceAsStream("/images/Librarian.png")));
        } else {
            villagerSpecialty.setText("");
            villagerImage.setImage(null);
        }
    }

    @FXML
    private void onSearchVillager() {
        String query = searchField.getText().toLowerCase();
        ObservableList<VillagerEmployee> filteredVillagers = villagers.filtered(villager ->
                villager.getName().toLowerCase().contains(query) ||
                        villager.getProfession().toLowerCase().contains(query) ||
                        villager.getVillage().toLowerCase().contains(query)
        );
        employeeTable.setItems(filteredVillagers);
        appendLog(
                "Search Results",
                String.format("• Query: %s\n• Villagers Found: %d", query, filteredVillagers.size()),
                "🔍"
        );
    }

    @FXML
    private void onExportReport() {
        exportReportToFile();
    }

    private void exportReportToFile() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Report");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(getReportFlowText());
                    appendLog(
                            "Report Exported",
                            String.format("Report exported successfully to: %s", file.getAbsolutePath()),
                            "✅"
                    );
                }
            }
        } catch (IOException e) {
            appendLog(
                    "Error",
                    "Failed to export report due to an I/O error.",
                    "❌"
            );
        }
    }

    private String getReportFlowText() {
        StringBuilder sb = new StringBuilder();
        for (javafx.scene.Node node : reportFlow.getChildren()) {
            if (node instanceof Text) {
                sb.append(((Text) node).getText());
            }
        }
        return sb.toString();
    }

    @FXML
    private void onAddVillager() {
        Dialog<VillagerEmployee> dialog = createAddVillagerDialog();
        Optional<VillagerEmployee> result = dialog.showAndWait();

        if (result.isPresent()) {
            VillagerEmployee newVillager = result.get();
            newVillager.setEmployeeId(nextId++);
            villagers.add(newVillager);

            appendLog(
                    "Villager Recruited!",
                    String.format(
                            "• Name: %s\n• Profession: %s\n• Village: %s\n• XP Level: %d\n• Registered at: %s",
                            newVillager.getName(), newVillager.getProfession(), newVillager.getVillage(),
                            newVillager.getExperienceLevel(), getCurrentTimestamp()
                    ),
                    "🟩"
            );
            updateStatistics();
        }
    }

    @FXML
    private void onEditVillager() {
        VillagerEmployee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Villager Selected", "Select a villager to edit their details.");
            return;
        }

        Dialog<VillagerEmployee> dialog = createEditVillagerDialog(selected);
        Optional<VillagerEmployee> result = dialog.showAndWait();

        if (result.isPresent()) {
            VillagerEmployee edited = result.get();

            selected.setName(edited.getName());
            selected.setVillage(edited.getVillage());
            selected.setExperienceLevel(edited.getExperienceLevel());

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
            } else if (selected instanceof ButcherVillager && edited instanceof ButcherVillager) {
                ButcherVillager s = (ButcherVillager) selected, e = (ButcherVillager) edited;
                s.setHourlyRate(e.getHourlyRate());
                s.setHoursWorked(e.getHoursWorked());
                s.setSpecialty(e.getSpecialty());
            } else if (selected instanceof ClericVillager && edited instanceof ClericVillager) {
                ClericVillager s = (ClericVillager) selected, e = (ClericVillager) edited;
                s.setHourlyRate(e.getHourlyRate());
                s.setHoursWorked(e.getHoursWorked());
                s.setSpecialty(e.getSpecialty());
            } else if (selected instanceof LibrarianVillager && edited instanceof LibrarianVillager) {
                LibrarianVillager s = (LibrarianVillager) selected, e = (LibrarianVillager) edited;
                s.setHourlyRate(e.getHourlyRate());
                s.setHoursWorked(e.getHoursWorked());
                s.setKnowledgeArea(e.getKnowledgeArea());
            }

            employeeTable.refresh();
            appendLog(
                    "Villager Data Updated!",
                    String.format(
                            "• Name: %s\n• Profession: %s\n• Village: %s\n• XP Level: %d\n• Updated at: %s",
                            selected.getName(), selected.getProfession(), selected.getVillage(),
                            selected.getExperienceLevel(), getCurrentTimestamp()
                    ),
                    "📝"
            );
            updateStatistics();
        }
    }

    @FXML
    private void onDeleteVillager() {
        VillagerEmployee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Villager Selected", "Select a villager to release from the workforce.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Release Villager");
        confirmation.setHeaderText("Release " + selected.getName() + "?");
        confirmation.setContentText("Are you sure you want to release this villager from duty?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            villagers.remove(selected);
            appendLog(
                    "Villager Released!",
                    String.format(
                            "• Name: %s\n• Profession: %s\n• Released at: %s",
                            selected.getName(), selected.getProfession(), getCurrentTimestamp()
                    ),
                    "🟥"
            );
            updateStatistics();
        }
    }

    @FXML
    private void onLevelUp() {
        VillagerEmployee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Villager Selected", "Select a villager to grant XP.");
            return;
        }

        int oldLevel = selected.getExperienceLevel();
        double oldSalary = selected.computeSalary();

        if (selected instanceof EmployeeActions) {
            ((EmployeeActions) selected).levelUp();
        }
        employeeTable.refresh();
        appendLog(
                "Villager Leveled Up!",
                String.format(
                        "• Name: %s\n• Profession: %s\n• XP: %d → %d\n• Emeralds: 💎 %.2f → 💎 %.2f\n• XP granted at: %s",
                        selected.getName(), selected.getProfession(), oldLevel, selected.getExperienceLevel(),
                        oldSalary, selected.computeSalary(), getCurrentTimestamp()
                ),
                "✨"
        );
    }

    @FXML
    private void onGenerateReport() {
        reportFlow.getChildren().clear();
        initializeReportArea();
        updateStatistics();
        appendWorkforceReport(villagers);
    }

    @FXML
    private void onRefresh() {
        reportFlow.getChildren().clear();
        initializeReportArea();
        updateStatistics();
        employeeTable.refresh();
        appendLog(
                "Table Refreshed",
                String.format("Refreshed at: %s", getCurrentTimestamp()),
                "🔄"
        );
    }

    @FXML
    private void onCalculateSalary() {
        VillagerEmployee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Villager Selected", "Select a villager to calculate their salary.");
            return;
        }
        double salary = selected.computeSalary();
        String message = String.format(
                "• Name: %s\n• Profession: %s\n• XP Level: %d\n• Salary: 💎 %.2f emeralds/month",
                selected.getName(), selected.getProfession(), selected.getExperienceLevel(), salary
        );
        appendLog("Salary Calculation", message, "💰");
    }

    private void appendWorkforceReport(ObservableList<VillagerEmployee> villagers) {
        StringBuilder report = new StringBuilder();
        String divider = "----------------------------------------\n";
        for (VillagerEmployee v : villagers) {
            String specialty;
            if (v instanceof FarmerVillager) {
                specialty = String.format("Crop Specialty   : %s", ((FarmerVillager) v).getCropSpecialty());
            } else if (v instanceof BlacksmithVillager) {
                specialty = String.format("Specialty        : %s", ((BlacksmithVillager) v).getSpecialty());
            } else if (v instanceof ButcherVillager) {
                specialty = String.format("Specialty        : %s", ((ButcherVillager) v).getSpecialty());
            } else if (v instanceof ClericVillager) {
                specialty = String.format("Specialty        : %s", ((ClericVillager) v).getSpecialty());
            } else if (v instanceof LibrarianVillager) {
                specialty = String.format("Knowledge Area   : %s", ((LibrarianVillager) v).getKnowledgeArea());
            } else {
                specialty = "Specialty        : N/A";
            }
            String workstation = (v instanceof EmployeeActions)
                    ? ((EmployeeActions) v).getWorkstation()
                    : "N/A";
            String reportLine = (v instanceof EmployeeActions)
                    ? ((EmployeeActions) v).submitReport()
                    : "N/A";
            report.append(String.format(
                    "• Name           : %-18s\n"
                            + "  Profession     : %-10s\n"
                            + "  Village        : %-15s\n"
                            + "  Level          : %-2d\n"
                            + "  %-25s\n"
                            + "  Salary         : 💎 %8.2f\n"
                            + "  Workstation    : %-18s\n"
                            + "  Report         : %s\n%s",
                    v.getName(),
                    v.getProfession(),
                    v.getVillage(),
                    v.getExperienceLevel(),
                    specialty,
                    v.computeSalary(),
                    workstation,
                    reportLine,
                    divider
            ));
        }
        Text body = new Text(report.toString());
        body.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");
        reportFlow.getChildren().add(body);
    }

    private Dialog<VillagerEmployee> createAddVillagerDialog() {
        Dialog<VillagerEmployee> dialog = new Dialog<>();
        dialog.setTitle("Add Villager");
        dialog.setHeaderText("Enter new villager details:");

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

    // In VillagerEMSController.java

    private GridPane createVillagerForm(VillagerEmployee villager) {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(14);
        grid.setPadding(new Insets(24, 40, 18, 24));
        grid.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #bdbdbd; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Section header
        Label section = new Label("Villager Details");
        section.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #388E3C;");
        grid.add(section, 0, 0, 2, 1);

        // Fields
        Label nameLabel = new Label("Villager Name:");
        nameLabel.setMinWidth(140);
        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Bob");
        nameField.setTooltip(new Tooltip("Enter the villager's name"));

        Label villageLabel = new Label("Village:");
        villageLabel.setMinWidth(140);
        TextField villageField = new TextField();
        villageField.setPromptText("e.g. Plains");
        villageField.setTooltip(new Tooltip("Enter the village name"));

        Label levelLabel = new Label("XP Level:");
        levelLabel.setMinWidth(140);
        Spinner<Integer> levelSpinner = new Spinner<>(1, 10, 1);
        levelSpinner.setEditable(true);
        levelSpinner.setTooltip(new Tooltip("Villager experience level (1-10)"));

        Label typeLabel = new Label("Profession:");
        typeLabel.setMinWidth(140);
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Farmer", "Blacksmith", "Butcher", "Cleric", "Librarian");
        typeCombo.setTooltip(new Tooltip("Select the villager's profession"));

        Label specialtyLabel = new Label("Specialty/Knowledge:");
        specialtyLabel.setMinWidth(140);
        TextField specialtyField = new TextField();
        specialtyField.setPromptText("e.g. Wheat, Tools, Potions");
        specialtyField.setTooltip(new Tooltip("Enter specialty or knowledge area"));

        Label rateLabel = new Label("Emerald Rate (hr):");
        rateLabel.setMinWidth(140);
        TextField rateField = new TextField();
        rateField.setPromptText("e.g. 15.0");
        rateField.setTooltip(new Tooltip("Hourly emerald rate (numeric)"));

        Label hoursLabel = new Label("Hours Worked:");
        hoursLabel.setMinWidth(140);
        Spinner<Integer> hoursSpinner = new Spinner<>(20, 60, 40);
        hoursSpinner.setEditable(true);
        hoursSpinner.setTooltip(new Tooltip("Hours worked per month (20-60)"));

        // Populate fields if editing
        if (villager != null) {
            nameField.setText(villager.getName());
            villageField.setText(villager.getVillage());
            levelSpinner.getValueFactory().setValue(villager.getExperienceLevel());

            if (villager instanceof ClericVillager) {
                ClericVillager cleric = (ClericVillager) villager;
                typeCombo.setValue("Cleric");
                rateField.setText(String.valueOf(cleric.getHourlyRate()));
                hoursSpinner.getValueFactory().setValue(cleric.getHoursWorked());
                specialtyField.setText(cleric.getSpecialty());
            } else if (villager instanceof FarmerVillager) {
                FarmerVillager farmer = (FarmerVillager) villager;
                typeCombo.setValue("Farmer");
                rateField.setText(String.valueOf(farmer.getHourlyRate()));
                hoursSpinner.getValueFactory().setValue(farmer.getHoursWorked());
                specialtyField.setText(farmer.getCropSpecialty());
            } else if (villager instanceof BlacksmithVillager) {
                BlacksmithVillager blacksmith = (BlacksmithVillager) villager;
                typeCombo.setValue("Blacksmith");
                rateField.setText(String.valueOf(blacksmith.getMonthlyRate()));
                hoursSpinner.getValueFactory().setValue(40);
                specialtyField.setText(blacksmith.getSpecialty());
            } else if (villager instanceof ButcherVillager) {
                ButcherVillager butcher = (ButcherVillager) villager;
                typeCombo.setValue("Butcher");
                rateField.setText(String.valueOf(butcher.getHourlyRate()));
                hoursSpinner.getValueFactory().setValue(butcher.getHoursWorked());
                specialtyField.setText(butcher.getSpecialty());
            } else if (villager instanceof LibrarianVillager) {
                LibrarianVillager librarian = (LibrarianVillager) villager;
                typeCombo.setValue("Librarian");
                rateField.setText(String.valueOf(librarian.getHourlyRate()));
                hoursSpinner.getValueFactory().setValue(librarian.getHoursWorked());
                specialtyField.setText(librarian.getKnowledgeArea());
            }
        } else {
            typeCombo.setValue("Farmer");
            rateField.setText("15.0");
            specialtyField.setText("Wheat");
        }

        // Add to grid
        grid.add(nameLabel, 0, 1);      grid.add(nameField, 1, 1);
        grid.add(villageLabel, 0, 2);   grid.add(villageField, 1, 2);
        grid.add(levelLabel, 0, 3);     grid.add(levelSpinner, 1, 3);
        grid.add(typeLabel, 0, 4);      grid.add(typeCombo, 1, 4);
        grid.add(specialtyLabel, 0, 5); grid.add(specialtyField, 1, 5);
        grid.add(rateLabel, 0, 6);      grid.add(rateField, 1, 6);
        grid.add(hoursLabel, 0, 7);     grid.add(hoursSpinner, 1, 7);

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

            if (name.isEmpty() || village.isEmpty()) {
                showAlert("Invalid Input", "Villager Name and Village cannot be empty.");
                return null;
            }

            if (specialty.isEmpty()) {
                specialty = "Farmer".equals(type) ? "Wheat" : "Tools";
            }

            if ("Farmer".equals(type)) {
                FarmerVillager farmer = new FarmerVillager(0, name, village, level, rate, specialty);
                farmer.setHoursWorked(hours);
                return farmer;
            }
            if ("Blacksmith".equals(type)) {
                BlacksmithVillager blacksmith = new BlacksmithVillager(0, name, village, level, rate, hours, specialty);
                return blacksmith;
            }
            if ("Butcher".equals(type)) {
                ButcherVillager butcher = new ButcherVillager(0, name, village, level, rate, hours, specialty);
                return butcher;
            }
            if ("Cleric".equals(type)) {
                ClericVillager cleric = new ClericVillager(0, name, village, level, rate, hours, specialty);
                return cleric;
            }
            if ("Librarian".equals(type)) {
                LibrarianVillager librarian = new LibrarianVillager(0, name, village, level, rate, hours, specialty);
                return librarian;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for emerald rate.");
            return null;
        }
        return null;
    }

    private void appendLog(String title, String message, String emoji) {
        Text header = new Text("\n" + emoji + " " + title + "\n");
        header.setStyle("-fx-font-weight: bold; -fx-fill: #1976D2;");

        Text body = new Text(message + "\n");
        body.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");

        reportFlow.getChildren().addAll(header, body);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void updateStatistics() {
        reportFlow.getChildren().removeIf(node ->
                node instanceof Text && ((Text) node).getText().contains("📊 Village Stats:")
        );

        long farmers = villagers.stream().filter(e -> e instanceof FarmerVillager).count();
        long blacksmiths = villagers.stream().filter(e -> e instanceof BlacksmithVillager).count();
        long butchers = villagers.stream().filter(e -> e instanceof ButcherVillager).count();
        long clerics = villagers.stream().filter(e -> e instanceof ClericVillager).count();
        long librarians = villagers.stream().filter(e -> e instanceof LibrarianVillager).count();
        double totalEmeralds = villagers.stream().mapToDouble(VillagerEmployee::computeSalary).sum();

        Text statsLabel = new Text("📊 Village Stats: ");
        statsLabel.setStyle("-fx-font-weight: bold; -fx-fill: #388E3C;");

        Text total = new Text(villagers.size() + " Villagers (");
        total.setStyle("-fx-font-weight: bold; -fx-fill: #333;");

        Text farmer = new Text(farmers + " Farmers");
        farmer.setStyle("-fx-fill: #388E3C;");

        Text comma1 = new Text(", ");
        Text blacksmith = new Text(blacksmiths + " Blacksmiths");
        blacksmith.setStyle("-fx-fill: #616161;");

        Text comma2 = new Text(", ");
        Text butcher = new Text(butchers + " Butchers");
        butcher.setStyle("-fx-fill: #c62828;");

        Text comma3 = new Text(", ");
        Text cleric = new Text(clerics + " Clerics");
        cleric.setStyle("-fx-fill: #6a1b9a;");

        Text comma4 = new Text(", ");
        Text librarian = new Text(librarians + " Librarians");
        librarian.setStyle("-fx-fill: #1976d2;");

        Text closeParen = new Text(") | ");
        Text emeraldsLabel = new Text("Emeralds: ");
        emeraldsLabel.setStyle("-fx-font-weight: bold; -fx-fill: #fbc02d;");

        Text emeralds = new Text("💎 " + String.format("%.2f", totalEmeralds) + "\n");
        emeralds.setStyle("-fx-fill: #388E3C;");

        int insertIndex = Math.min(2, reportFlow.getChildren().size());
        reportFlow.getChildren().add(insertIndex++, statsLabel);
        reportFlow.getChildren().add(insertIndex++, total);
        reportFlow.getChildren().add(insertIndex++, farmer);
        reportFlow.getChildren().add(insertIndex++, comma1);
        reportFlow.getChildren().add(insertIndex++, blacksmith);
        reportFlow.getChildren().add(insertIndex++, comma2);
        reportFlow.getChildren().add(insertIndex++, butcher);
        reportFlow.getChildren().add(insertIndex++, comma3);
        reportFlow.getChildren().add(insertIndex++, cleric);
        reportFlow.getChildren().add(insertIndex++, comma4);
        reportFlow.getChildren().add(insertIndex++, librarian);
        reportFlow.getChildren().add(insertIndex++, closeParen);
        reportFlow.getChildren().add(insertIndex++, emeraldsLabel);
        reportFlow.getChildren().add(insertIndex, emeralds);
    }
}