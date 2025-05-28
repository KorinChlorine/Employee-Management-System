package jobs;

public class Fletcher implements Job {
    private int arrowsCrafted;

    public Fletcher() {
        this.arrowsCrafted = 30;
    }

    public int getArrowsCrafted() { return arrowsCrafted; }

    public void setArrowsCrafted(int count) {
        this.arrowsCrafted = count;
    }

    public String craftArrow() {
        return "Crafts an arrow. Total: " + ++arrowsCrafted;
    }
    @Override
    public String getName() { return "Fletcher"; }
    @Override
    public String getSkill() { return "Arrow and bow crafting."; }
}
