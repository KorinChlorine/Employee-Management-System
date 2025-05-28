package jobs;

public class Nitwit implements Job {
    private boolean sleeping;

    public Nitwit() {
        this.sleeping = true;
    }

    public boolean isSleeping() { return sleeping; }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    public String doNothing() {
        return "Does nothing at all.";
    }
    @Override
    public String getName() { return "Nitwit"; }
    @Override
    public String getSkill() { return "No skill whatsoever."; }
}
