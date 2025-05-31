package com.example.villagerems;
import java.util.concurrent.ThreadLocalRandom;
public class BlacksmithVillager extends VillagerEmployee implements EmployeeActions {
    private double hourlyRate;
    private int hoursWorked;
    private String specialty;
    private int itemsCrafted;

    public BlacksmithVillager(int employeeId, String name, String village, int experienceLevel,
                              double hourlyRate, int hoursWorked, String specialty) {
        super(employeeId, name, village, experienceLevel);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.specialty = specialty;
        this.itemsCrafted = ThreadLocalRandom.current().nextInt(5, 11);
    }

    public BlacksmithVillager(int employeeId, String name, String village, int experienceLevel,
                              double hourlyRate, String specialty) {
        this(employeeId, name, village, experienceLevel, hourlyRate, 160, specialty);
    }

    @Override
    public double getMonthlyRate() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public String getSpecialty() {
        return specialty;
    }

    @Override
    public String getProfession() { return "Blacksmith"; }

    @Override
    public String getWorkstation() { return "Smithing Table"; }

    @Override
    public double computeSalary() {
        double baseSalary = hourlyRate * hoursWorked;
        double experienceBonus = baseSalary * (getExperienceLevel() * 0.15);
        double productivityBonus = itemsCrafted * 10;
        return baseSalary + experienceBonus + productivityBonus;
    }

    @Override
    public String submitReport() {
        return getName() + " from " + specialty + " department has crafted " +
                itemsCrafted + " items this month. Forge efficiency: " +
                (getExperienceLevel() * 20) + "%";
    }

    @Override
    public void levelUp() {
        setExperienceLevel(getExperienceLevel() + 1);
        hourlyRate += 2.0;
    }

    @Override
    public String getJobDescription() {
        return "Creates tools and weapons in the " + specialty + " department.";
    }

    // Getters and Setters
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    public int getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(int hoursWorked) { this.hoursWorked = hoursWorked; }
    public String getSpecialtyField() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public int getItemsCrafted() { return itemsCrafted; }
    public void setItemsCrafted(int itemsCrafted) { this.itemsCrafted = itemsCrafted; }
}