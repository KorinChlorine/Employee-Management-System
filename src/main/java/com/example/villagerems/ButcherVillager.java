package com.example.villagerems;

// ButcherVillager.java - Subclass
public class ButcherVillager extends VillagerEmployee implements EmployeeActions {
    private double dailyWage;
    private int workDaysPerMonth;
    private String meatSpecialty;
    private int animalsProcessed;

    public ButcherVillager(int employeeId, String name, String village, int experienceLevel,
                           double dailyWage, String meatSpecialty) {
        super(employeeId, name, village, experienceLevel);
        this.dailyWage = dailyWage;
        this.workDaysPerMonth = 22;
        this.meatSpecialty = meatSpecialty;
        this.animalsProcessed = 0;
    }

    @Override
    public double getMonthlyRate() {
        return dailyWage * workDaysPerMonth;
    }

    @Override
    public String getSpecialty() {
        return meatSpecialty;
    }

    @Override
    public String getProfession() {
        return "Butcher";
    }

    @Override
    public String getWorkstation() {
        return "Smoker";
    }

    @Override
    public double computeSalary() {
        double baseSalary = dailyWage * workDaysPerMonth;
        double experienceBonus = baseSalary * (getExperienceLevel() * 0.11);
        double productionBonus = animalsProcessed * 12;
        return baseSalary + experienceBonus + productionBonus;
    }

    @Override
    public String submitReport() {
        return getName() + " has processed " + animalsProcessed + " animals this month. " +
                "Specialty: " + meatSpecialty + ". Food safety compliance: " +
                (getExperienceLevel() * 22) + "%";
    }

    @Override
    public void levelUp() {
        setExperienceLevel(getExperienceLevel() + 1);
        dailyWage += 15.0;
    }

    @Override
    public String getJobDescription() {
        return "Processes meat and specializes in " + meatSpecialty + " for village food supply.";
    }

    // Getters and Setters
    public double getDailyWage() { return dailyWage; }
    public void setDailyWage(double dailyWage) { this.dailyWage = dailyWage; }
    public int getWorkDaysPerMonth() { return workDaysPerMonth; }
    public void setWorkDaysPerMonth(int workDaysPerMonth) { this.workDaysPerMonth = workDaysPerMonth; }
    public String getMeatSpecialty() { return meatSpecialty; }
    public void setMeatSpecialty(String meatSpecialty) { this.meatSpecialty = meatSpecialty; }
    public int getAnimalsProcessed() { return animalsProcessed; }
    public void setAnimalsProcessed(int animalsProcessed) { this.animalsProcessed = animalsProcessed; }
}