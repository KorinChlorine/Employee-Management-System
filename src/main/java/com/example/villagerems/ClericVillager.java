package com.example.villagerems;

public class ClericVillager extends VillagerEmployee implements EmployeeActions {
    private double hourlyRate;
    private int hoursWorked;
    private String specialty;
    private int healingSessionsPerformed;
    private int potionsBrewed;

    public ClericVillager(int employeeId, String name, String village, int experienceLevel,
                          double hourlyRate, int hoursWorked, String specialty) {
        super(employeeId, name, village, experienceLevel);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.specialty = specialty;
        this.healingSessionsPerformed = 0;
        this.potionsBrewed = 0;
    }

    public ClericVillager(int employeeId, String name, String village, int experienceLevel,
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
    public String getProfession() { return "Cleric"; }

    @Override
    public String getWorkstation() { return "Brewing Stand"; }

    @Override
    public double computeSalary() {
        double baseSalary = hourlyRate * hoursWorked;
        double experienceBonus = baseSalary * (getExperienceLevel() * 0.10);
        double serviceBonus = (healingSessionsPerformed * 20) + (potionsBrewed * 8);
        return baseSalary + experienceBonus + serviceBonus;
    }

    @Override
    public String submitReport() {
        return getName() + " at " + specialty + " has performed " + healingSessionsPerformed +
                " healing sessions and brewed " + potionsBrewed + " potions. Spiritual guidance level: " +
                (getExperienceLevel() * 18) + "%";
    }

    @Override
    public void levelUp() {
        setExperienceLevel(getExperienceLevel() + 1);
        hourlyRate += 2.0;
    }

    @Override
    public String getJobDescription() {
        return "Provides spiritual guidance and healing services at " + specialty + ".";
    }

    // Getters and Setters
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    public int getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(int hoursWorked) { this.hoursWorked = hoursWorked; }
    public String getSpecialtyField() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public int getHealingSessionsPerformed() { return healingSessionsPerformed; }
    public void setHealingSessionsPerformed(int healingSessionsPerformed) { this.healingSessionsPerformed = healingSessionsPerformed; }
    public int getPotionsBrewed() { return potionsBrewed; }
    public void setPotionsBrewed(int potionsBrewed) { this.potionsBrewed = potionsBrewed; }
}