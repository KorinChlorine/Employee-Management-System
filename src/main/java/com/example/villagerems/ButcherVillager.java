package com.example.villagerems;
import java.util.concurrent.ThreadLocalRandom;
public class ButcherVillager extends VillagerEmployee implements EmployeeActions {
    private double hourlyRate;
    private int hoursWorked;
    private String specialty;
    private int animalsProcessed;

    public ButcherVillager(int employeeId, String name, String village, int experienceLevel,
                           double hourlyRate, int hoursWorked, String specialty) {
        super(employeeId, name, village, experienceLevel);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.specialty = specialty;
        this.animalsProcessed = ThreadLocalRandom.current().nextInt(5, 11);
    }

    public ButcherVillager(int employeeId, String name, String village, int experienceLevel,
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
    public String getProfession() { return "Butcher"; }

    @Override
    public String getWorkstation() { return "Smoker"; }

    @Override
    public double computeSalary() {
        double baseSalary = hourlyRate * hoursWorked;
        double experienceBonus = baseSalary * (getExperienceLevel() * 0.11);
        double productionBonus = animalsProcessed * 12;
        return baseSalary + experienceBonus + productionBonus;
    }

    @Override
    public String submitReport() {
        return getName() + " from " + specialty + " department has processed " +
                animalsProcessed + " animals this month. Food safety compliance: " +
                (getExperienceLevel() * 22) + "%";
    }

    @Override
    public void levelUp() {
        setExperienceLevel(getExperienceLevel() + 1);
        hourlyRate += 1.5;
    }

    @Override
    public String getJobDescription() {
        return "Processes meat and specializes in " + specialty + " for village food supply.";
    }

    // Getters and Setters
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    public int getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(int hoursWorked) { this.hoursWorked = hoursWorked; }
    public String getSpecialtyField() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public int getAnimalsProcessed() { return animalsProcessed; }
    public void setAnimalsProcessed(int animalsProcessed) { this.animalsProcessed = animalsProcessed; }
}