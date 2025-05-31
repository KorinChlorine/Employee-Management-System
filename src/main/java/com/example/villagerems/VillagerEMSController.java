package com.example.villagerems;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class VillagerEMSController {

    @FXML
    private TableView<VillagerEmployee> employeeTable;
    @FXML
    private TextArea reportArea;

    private ObservableList<VillagerEmployee> employees = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize your table and data here
        employeeTable.setItems(employees);
        reportArea.setText("📋 Welcome to the Village Employee Management System!");
        addSampleData();
    }

    @FXML
    private void onAddVillager() {
        // Your logic to add a villager
        reportArea.appendText("➕ Add Villager clicked\n");
    }

    @FXML
    private void onEditVillager() {
        reportArea.appendText("✏️ Edit Villager clicked\n");
    }

    @FXML
    private void onDeleteVillager() {
        reportArea.appendText("❌ Remove Villager clicked\n");
    }

    @FXML
    private void onCalculateSalary() {
        reportArea.appendText("💰 Calculate Salary clicked\n");
    }

    @FXML
    private void onGenerateReport() {
        reportArea.appendText("📊 Generate Report clicked\n");
    }

    @FXML
    private void onLevelUp() {
        reportArea.appendText("⬆️ Level Up clicked\n");
    }

    @FXML
    private void onRefresh() {
        employeeTable.refresh();
        reportArea.appendText("🔄 Table refreshed\n");
    }

    private void addSampleData() {
        employees.add(new FarmerVillager(1001, "Bob the Farmer", "Oakville", 3, 15.0, "Wheat"));
        employees.add(new BlacksmithVillager(1002, "Iron Mike", "Stonehaven", 5, 2500.0, "Weapons"));
    }
}
