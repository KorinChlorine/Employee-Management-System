package com.example.villagerems;

// LibrarianVillager.java - Subclass
public class LibrarianVillager extends VillagerEmployee implements EmployeeActions {
    private double hourlyRate;
    private int hoursWorked;
    private String knowledgeArea;
    private int booksManaged;

    public LibrarianVillager(int employeeId, String name, String village, int experienceLevel,
                             double hourlyRate, String knowledgeArea) {
        super(employeeId, name, village, experienceLevel);
        this.hourlyRate = hourlyRate;
        this.knowledgeArea = knowledgeArea;
        this.hoursWorked = 160; // Default monthly hours
        this.booksManaged = 0;
    }

    @Override
    public double getMonthlyRate() {
        return hourlyRate * hoursWorked;
    }

    // In LibrarianVillager.java
    public void setSpecialty(String specialty) {
        setKnowledgeArea(specialty);
    }
    public String getSpecialty() {
        return getKnowledgeArea();
    }

    @Override
    public String getProfession() { return "Librarian"; }

    @Override
    public String getWorkstation() { return "Lectern"; }

    @Override
    public double computeSalary() {
        double baseSalary = hourlyRate * hoursWorked;
        double knowledgeBonus = baseSalary * (getExperienceLevel() * 0.12);
        double managementBonus = booksManaged * 5;
        return baseSalary + knowledgeBonus + managementBonus;
    }

    @Override
    public String submitReport() {
        return getName() + " has managed " + booksManaged + " books in " + knowledgeArea +
                ". Knowledge level: " + (getExperienceLevel() * 15) + " scrolls mastered.";
    }

    @Override
    public void levelUp() {
        setExperienceLevel(getExperienceLevel() + 1);
        hourlyRate += 1.5;
    }

    @Override
    public String getJobDescription() {
        return "Manages knowledge and books specializing in " + knowledgeArea + ".";
    }

    // Getters and Setters
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    public int getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(int hoursWorked) { this.hoursWorked = hoursWorked; }
    public String getKnowledgeArea() { return knowledgeArea; }
    public void setKnowledgeArea(String knowledgeArea) { this.knowledgeArea = knowledgeArea; }
    public int getBooksManaged() { return booksManaged; }
    public void setBooksManaged(int booksManaged) { this.booksManaged = booksManaged; }
}