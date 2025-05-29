package com.example.villagerems;

// FarmerVillager.java - Subclass
public class FarmerVillager extends VillagerEmployee implements EmployeeActions {
    private double hourlyRate;
    private int hoursWorked;
    private String cropSpecialty;

    public FarmerVillager(int employeeId, String name, String village, int experienceLevel,
                          double hourlyRate, String cropSpecialty) {
        super(employeeId, name, village, experienceLevel);
        this.hourlyRate = hourlyRate;
        this.cropSpecialty = cropSpecialty;
        this.hoursWorked = 40; // Default work week
    }

    @Override
    public String getProfession() { return "Farmer"; }

    @Override
    public String getWorkstation() { return "Composter"; }

    @Override
    public double computeSalary() {
        double baseSalary = hourlyRate * hoursWorked;
        double experienceBonus = baseSalary * (getExperienceLevel() * 0.1);
        return baseSalary + experienceBonus;
    }

    @Override
    public String submitReport() {
        return getName() + " has harvested " + cropSpecialty + " crops. Weekly yield: " +
                (getExperienceLevel() * 50) + " units.";
    }

    @Override
    public void levelUp() {
        setExperienceLevel(getExperienceLevel() + 1);
        hourlyRate += 2.0; // Salary increase with level
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
}