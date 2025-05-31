package com.example.villagerems;

public class LibrarianVillager extends VillagerEmployee implements EmployeeActions {
    private double hourlyRate;
    private int hoursWorked;
    private String knowledgeArea;
    private int booksManaged;
    private int lecturesGiven;

    public LibrarianVillager(int employeeId, String name, String village, int experienceLevel,
                             double hourlyRate, int hoursWorked, String knowledgeArea) {
        super(employeeId, name, village, experienceLevel);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.knowledgeArea = knowledgeArea;
        this.booksManaged = 0;
        this.lecturesGiven = 0;
    }

    public LibrarianVillager(int employeeId, String name, String village, int experienceLevel,
                             double hourlyRate, String knowledgeArea) {
        this(employeeId, name, village, experienceLevel, hourlyRate, 160, knowledgeArea);
    }

    @Override
    public double getMonthlyRate() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public String getSpecialty() {
        return knowledgeArea;
    }

    @Override
    public String getProfession() { return "Librarian"; }

    @Override
    public String getWorkstation() { return "Lectern"; }

    @Override
    public double computeSalary() {
        double baseSalary = hourlyRate * hoursWorked;
        double experienceBonus = baseSalary * (getExperienceLevel() * 0.12);
        double managementBonus = (booksManaged * 5) + (lecturesGiven * 10);
        return baseSalary + experienceBonus + managementBonus;
    }

    @Override
    public String submitReport() {
        return getName() + " in " + knowledgeArea + " has managed " + booksManaged +
                " books and given " + lecturesGiven + " lectures. Knowledge spread: " +
                (getExperienceLevel() * 20) + "%";
    }

    @Override
    public void levelUp() {
        setExperienceLevel(getExperienceLevel() + 1);
        hourlyRate += 1.5;
    }

    @Override
    public String getJobDescription() {
        return "Manages books and knowledge in " + knowledgeArea + ", and educates villagers.";
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
    public int getLecturesGiven() { return lecturesGiven; }
    public void setLecturesGiven(int lecturesGiven) { this.lecturesGiven = lecturesGiven; }
}