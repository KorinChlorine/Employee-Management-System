package com.example.villagerems;// com.example.villagerems.VillagerEmployee.java - Base class
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class VillagerEmployee {
    private int employeeId;
    private String name;
    private String village;
    private int experienceLevel;

    public VillagerEmployee(int employeeId, String name, String village, int experienceLevel) {
        this.employeeId = employeeId;
        this.name = name;
        this.village = village;
        this.experienceLevel = experienceLevel;
    }

    // Getters
    public int getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public String getVillage() { return village; }
    public int getExperienceLevel() { return experienceLevel; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setVillage(String village) { this.village = village; }
    public void setExperienceLevel(int experienceLevel) { this.experienceLevel = experienceLevel; }

    // Abstract methods
    public abstract String getProfession();
    public abstract String getWorkstation();

    // JavaFX Properties for TableView
    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    public StringProperty professionProperty() {
        return new SimpleStringProperty(getProfession());
    }

    public StringProperty villageProperty() {
        return new SimpleStringProperty(village);
    }

    public abstract double computeSalary();


    public void setEmployeeId(int id) {
        this.employeeId = id;
    }
}

