package com.example.villagerems;

// BlacksmithVillager.java - Subclass
public class BlacksmithVillager extends VillagerEmployee implements EmployeeActions {
    private double salaryPerMonth;
    private String department;
    private int itemsCrafted;

    public BlacksmithVillager(int employeeId, String name, String village, int experienceLevel,
                              double salaryPerMonth, String department) {
        super(employeeId, name, village, experienceLevel);
        this.salaryPerMonth = salaryPerMonth;
        this.department = department;
        this.itemsCrafted = 0;
    }

    @Override
    public String getProfession() { return "Blacksmith"; }

    @Override
    public String getWorkstation() { return "Smithing Table"; }

    @Override
    public double computeSalary() {
        double baseSalary = salaryPerMonth;
        double experienceBonus = baseSalary * (getExperienceLevel() * 0.15);
        double productivityBonus = itemsCrafted * 10;
        return baseSalary + experienceBonus + productivityBonus;
    }

    @Override
    public String submitReport() {
        return getName() + " from " + department + " department has crafted " +
                itemsCrafted + " items this month. Forge efficiency: " +
                (getExperienceLevel() * 20) + "%";
    }

    @Override
    public void levelUp() {
        setExperienceLevel(getExperienceLevel() + 1);
        salaryPerMonth += 200.0;
    }

    @Override
    public String getJobDescription() {
        return "Creates tools and weapons in the " + department + " department.";
    }

    // Getters and Setters
    public double getSalaryPerMonth() { return salaryPerMonth; }
    public void setSalaryPerMonth(double salaryPerMonth) { this.salaryPerMonth = salaryPerMonth; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public int getItemsCrafted() { return itemsCrafted; }
    public void setItemsCrafted(int itemsCrafted) { this.itemsCrafted = itemsCrafted; }
}
