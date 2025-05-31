package com.example.villagerems;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadLocalRandom;
// FarmerVillager.java - Subclass
public class FarmerVillager extends VillagerEmployee implements EmployeeActions {
    private double hourlyRate;
    private int hoursWorked;
    private String cropSpecialty;
    private int cropsHarvested;

    public FarmerVillager(int employeeId, String name, String village, int experienceLevel,
                          double hourlyRate, String cropSpecialty) {
        super(employeeId, name, village, experienceLevel);
        this.hourlyRate = hourlyRate;
        this.cropSpecialty = cropSpecialty;
        this.hoursWorked = 160; // Default monthly hours
        this.cropsHarvested = ThreadLocalRandom.current().nextInt(5, 11);
    }

    @Override
    public double getMonthlyRate() {
        return hourlyRate * hoursWorked;
    }

    public void setSpecialty(String specialty) {
        setCropSpecialty(specialty);
    }
    public String getSpecialty() {
        return getCropSpecialty();
    }

    @Override
    public String getProfession() { return "Farmer"; }

    @Override
    public String getWorkstation() { return "Composter"; }

    @Override
    public double computeSalary() {
        double baseSalary = hourlyRate * hoursWorked;
        double experienceBonus = baseSalary * (getExperienceLevel() * 0.1);
        double yieldBonus = getExperienceLevel() * 25;
        return baseSalary + experienceBonus + yieldBonus;
    }

    @Override
    public String submitReport() {
        return getName() + " has harvested " + cropsHarvested + " " + cropSpecialty +
                " crops this month. Yield: " + (getExperienceLevel() * 200) + " units.";
    }

    @Override
    public void levelUp() {
        setExperienceLevel(getExperienceLevel() + 1);
        hourlyRate += 2.0;
    }

    @Override
    public String getJobDescription() {
        return "Cultivates " + cropSpecialty + " and manages farmland for the village.";
    }

    // Getters and Setters
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    public int getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(int hoursWorked) { this.hoursWorked = hoursWorked; }
    public String getCropSpecialty() { return cropSpecialty; }
    public void setCropSpecialty(String cropSpecialty) { this.cropSpecialty = cropSpecialty; }
    public int getCropsHarvested() { return cropsHarvested; }
    public void setCropsHarvested(int cropsHarvested) { this.cropsHarvested = cropsHarvested; }
}