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
import java.util.List;

public class VillagerEMSController {
    @FXML private VBox sidebar;
    @FXML private ImageView villagerImage;
    @FXML private Label villagerName;
    @FXML private Label villagerType;
    @FXML private Label villagerVillage;
    @FXML private Label villagerLevel;
    @FXML private Label villagerSpecialty;
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
    private TextFlow reportFlow;

    private ObservableList<VillagerEmployee> villagers = FXCollections.observableArrayList();
    private int nextId = 1006;

    @FXML
    private TextField searchField;

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
    public void initialize() {
        setupTableColumns();
        employeeTable.setItems(villagers);
        initializeReportArea();
        addSampleData();
        updateStatistics();
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            updateSidebar(newSel);

            villagerImage.setPreserveRatio(true);
            villagerImage.setFitWidth(180);   // Adjust as needed
            villagerImage.setFitHeight(240);  // Adjust as needed for vertical space
        });
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

    private void initializeReportArea() {
        reportFlow.getChildren().clear();

        // Welcome header
        Text welcome = new Text("📜 Welcome to the Minecraft Village Workforce Ledger\n");
        welcome.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-fill: #388E3C;");
        reportFlow.getChildren().add(welcome);

        // Divider
        Text divider = new Text("========================================\n");
        divider.setStyle("-fx-fill: #BDBDBD;");
        reportFlow.getChildren().add(divider);

        // Initial stats line
        String stats = String.format(
                "📊 Village Stats:\n" +
                        "   • Villagers: 0\n" +
                        "   • Farmers: 0\n" +
                        "   • Blacksmiths: 0\n" +
                        "   • Butchers: 0\n" +
                        "   • Clerics: 0\n" +
                        "   • Librarians: 0\n" +
                        "   • Emeralds: 💎 0.00\n"
        );
        Text statsText = new Text(stats);
        statsText.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px; -fx-fill: #333;");
        reportFlow.getChildren().add(statsText);

        // No villagers message
        Text noVillagers = new Text("\nNo villagers registered yet. Use the buttons above to recruit!\n");
        noVillagers.setStyle("-fx-font-style: italic; -fx-fill: #888;");
        reportFlow.getChildren().add(noVillagers);

        // Boot time
        Text bootTime = new Text("\n⏰ System booted at: " + getCurrentTimestamp() + "\n");
        bootTime.setStyle("-fx-fill: #555;");
        reportFlow.getChildren().add(bootTime);

        // End divider
        Text endDivider = new Text("========================================\n\n");
        endDivider.setStyle("-fx-fill: #BDBDBD;");
        reportFlow.getChildren().add(endDivider);
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
    private Dialog<VillagerEmployee> createAddVillagerDialog() {
        Dialog<VillagerEmployee> dialog = new Dialog<>();
        dialog.setTitle("Add Villager");
        dialog.setHeaderText("Enter new villager details:");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = createVillagerForm(null); // Pass null for a blank form
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return extractVillagerFromForm(grid);
            }
            return null;
        });

        return dialog;
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
        appendWorkforceReport(villagers);
    }

    @FXML
    private void onRefresh() {
        employeeTable.refresh();
        updateStatistics();
        appendLog(
                "Table Refreshed",
                String.format("Refreshed at: %s", getCurrentTimestamp()),
                "🔄"
        );
    }

    private void appendWorkforceReport(ObservableList<VillagerEmployee> villagers) {
        Text header = new Text("\n📊 Workforce Report\n");
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-fill: #1976D2;");

        Text divider = new Text("----------------------------------------\n");
        divider.setStyle("-fx-fill: #90A4AE;");

        StringBuilder report = new StringBuilder();
        for (VillagerEmployee v : villagers) {
            String specialty = "";
            if (v instanceof FarmerVillager) {
                specialty = "Crop: " + ((FarmerVillager) v).getCropSpecialty();
            } else if (v instanceof BlacksmithVillager) {
                specialty = "Specialty: " + ((BlacksmithVillager) v).getSpecialty();
            } else if (v instanceof ButcherVillager) {
                specialty = "Specialty: " + ((ButcherVillager) v).getSpecialty();
            } else if (v instanceof ClericVillager) {
                specialty = "Specialty: " + ((ClericVillager) v).getSpecialty();
            } else if (v instanceof LibrarianVillager) {
                specialty = "Knowledge: " + ((LibrarianVillager) v).getKnowledgeArea();
            }
            report.append(String.format(
                    "• %s (%s)\n   - Village: %s\n   - Level: %d\n   - %s\n   - Salary: 💎 %.2f\n\n",
                    v.getName(), v.getProfession(), v.getVillage(), v.getExperienceLevel(), specialty, v.computeSalary()
            ));
        }
        Text body = new Text(report.toString());
        body.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");

        reportFlow.getChildren().addAll(header, divider, body, divider);
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

        TextField nameField = new TextField();
        TextField villageField = new TextField();
        Spinner<Integer> levelSpinner = new Spinner<>(1, 10, 1);
        TextField rateField = new TextField();
        Spinner<Integer> hoursSpinner = new Spinner<>(20, 60, 40);
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Farmer", "Blacksmith", "Butcher", "Cleric", "Librarian");
        TextField specialtyField = new TextField();

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

        grid.add(new Label("Villager Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Village:"), 0, 1);
        grid.add(villageField, 1, 1);

        grid.add(new Label("XP Level:"), 0, 2);
        grid.add(levelSpinner, 1, 2);

        grid.add(new Label("Profession:"), 0, 3);
        grid.add(typeCombo, 1, 3);

        grid.add(new Label("Specialty/Knowledge:"), 0, 4);
        grid.add(specialtyField, 1, 4);

        grid.add(new Label("Emerald Rate (hr):"), 0, 5);
        grid.add(rateField, 1, 5);

        grid.add(new Label("Hours Worked:"), 0, 6);
        grid.add(hoursSpinner, 1, 6);

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateStatistics() {
        if (villagers.isEmpty()) {
            return;
        }

        long farmers = villagers.stream().filter(e -> e instanceof FarmerVillager).count();
        long blacksmiths = villagers.stream().filter(e -> e instanceof BlacksmithVillager).count();
        long butchers = villagers.stream().filter(e -> e instanceof ButcherVillager).count();
        long clerics = villagers.stream().filter(e -> e instanceof ClericVillager).count();
        long librarians = villagers.stream().filter(e -> e instanceof LibrarianVillager).count();
        double totalEmeralds = villagers.stream().mapToDouble(VillagerEmployee::computeSalary).sum();

        String stats = String.format("📊 §aVillage Stats:§r %d Villagers (§2%d Farmers§r, §8%d Blacksmiths§r, §c%d Butchers§r, §5%d Clerics§r, §9%d Librarians§r) | §eEmeralds:§r 💎 %.2f\n",
                villagers.size(), farmers, blacksmiths, butchers, clerics, librarians, totalEmeralds);

        // Remove previous stats line if present
        StringBuilder newText = new StringBuilder();
        boolean replaced = false;
        for (javafx.scene.Node node : reportFlow.getChildren()) {
            if (node instanceof Text) {
                String text = ((Text) node).getText();
                if (text.contains("📊 §aVillage Stats:") && !replaced) {
                    newText.append(stats);
                    replaced = true;
                } else {
                    newText.append(text);
                }
            }
        }
        if (!replaced) {
            newText.append(stats);
        }
        reportFlow.getChildren().clear();
        reportFlow.getChildren().add(new Text(newText.toString()));
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void addSampleData() {
        FarmerVillager farmer = new FarmerVillager(1001, "Bob the Farmer", "Oakville", 3, 15.0, "Wheat");
        farmer.setHoursWorked(40);
        villagers.add(farmer);

        BlacksmithVillager blacksmith = new BlacksmithVillager(1002, "Iron Mike", "Stonehaven", 5, 2500.0, 40, "Weapons");
        villagers.add(blacksmith);

        ButcherVillager butcher = new ButcherVillager(1003, "Chop Suey", "Meatville", 2, 18.0, 38, "Beef");
        villagers.add(butcher);

        ClericVillager cleric = new ClericVillager(1004, "Healer Joe", "Sanctuary", 4, 20.0, 40, "Healing");
        villagers.add(cleric);

        LibrarianVillager librarian = new LibrarianVillager(1005, "Booker T.", "Librarytown", 3, 19.0, 40, "History");
        villagers.add(librarian);

        reportFlow.getChildren().add(new Text("✅ §aSample villagers spawned in the village!§r\n"));
        reportFlow.getChildren().add(new Text("   - Bob the Farmer (Lvl 3, Wheat)\n"));
        reportFlow.getChildren().add(new Text("   - Iron Mike (Lvl 5, Weapons)\n"));
        reportFlow.getChildren().add(new Text("   - Chop Suey (Lvl 2, Beef)\n"));
        reportFlow.getChildren().add(new Text("   - Healer Joe (Lvl 4, Healing)\n"));
        reportFlow.getChildren().add(new Text("   - Booker T. (Lvl 3, History)\n\n"));
    }

    private void appendLog(String title, String message, String type) {
        Text header = new Text("\n" + type + " " + title + "\n");
        header.setStyle("-fx-font-weight: bold; -fx-fill: #2e7d32;");

        Text divider = new Text("----------------------------------------\n");
        divider.setStyle("-fx-fill: #888;");

        Text body = new Text(message + "\n");
        body.setStyle("-fx-font-family: 'Courier New';");

        Text footer = new Text("----------------------------------------\n");
        footer.setStyle("-fx-fill: #888;");

        reportFlow.getChildren().addAll(header, divider, body, footer);
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

}