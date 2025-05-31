package com.example.villagerems;

// ClericVillager.java - Subclass
public class ClericVillager extends VillagerEmployee implements EmployeeActions {
    private double stipend;
    private String temple;
    private int healingSessionsPerformed;
    private int potionsBrewed;

    public ClericVillager(int employeeId, String name, String village, int experienceLevel,
                          double stipend, String temple) {
        super(employeeId, name, village, experienceLevel);
        this.stipend = stipend;
        this.temple = temple;
        this.healingSessionsPerformed = 0;
        this.potionsBrewed = 0;
    }

    public double getMonthlyRate() {
        return stipend;
    }

    public String getSpecialty() {
        return temple;
    }

    @Override
    public String getProfession() { return "Cleric"; }

    @Override
    public String getWorkstation() { return "Brewing Stand"; }

    @Override
    public double computeSalary() {
        double baseSalary = stipend;
        double experienceBonus = baseSalary * (getExperienceLevel() * 0.1);
        double serviceBonus = (healingSessionsPerformed * 20) + (potionsBrewed * 8);
        return baseSalary + experienceBonus + serviceBonus;
    }

    @Override
    public String submitReport() {
        return getName() + " at " + temple + " has performed " + healingSessionsPerformed +
                " healing sessions and brewed " + potionsBrewed + " potions. Spiritual guidance level: " +
                (getExperienceLevel() * 18) + "%";
    }

    @Override
    public void levelUp() {
        setExperienceLevel(getExperienceLevel() + 1);
        stipend += 100.0;
    }

    @Override
    public String getJobDescription() {
        return "Provides spiritual guidance and healing services at " + temple + ".";
    }

    // Getters and Setters
    public double getStipend() { return stipend; }
    public void setStipend(double stipend) { this.stipend = stipend; }
    public String getTemple() { return temple; }
    public void setTemple(String temple) { this.temple = temple; }
    public int getHealingSessionsPerformed() { return healingSessionsPerformed; }
    public void setHealingSessionsPerformed(int healingSessionsPerformed) { this.healingSessionsPerformed = healingSessionsPerformed; }
    public int getPotionsBrewed() { return potionsBrewed; }
    public void setPotionsBrewed(int potionsBrewed) { this.potionsBrewed = potionsBrewed; }
}