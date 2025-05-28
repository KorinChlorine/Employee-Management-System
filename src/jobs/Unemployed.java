package jobs;

public class Unemployed implements Job {
    private int daysWithoutJob;

    public Unemployed() {
        this.daysWithoutJob = 3;
    }

    public int getDaysWithoutJob() { return daysWithoutJob; }

    public void setDaysWithoutJob(int days) {
        this.daysWithoutJob = days;
    }

    public String searchForJob() {
        return "Wanders the village in search of a workstation.";
    }
    @Override
    public String getName() { return "Unemployed"; }
    @Override
    public String getSkill() { return "No active role."; }
}
