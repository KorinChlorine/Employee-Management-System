package jobs;

public class Leatherworker implements Job {
    private int leatherItems;

    public Leatherworker() {
        this.leatherItems = 5;
    }

    public int getLeatherItems() { return leatherItems; }

    public void setLeatherItems(int leatherItems) {
        this.leatherItems = leatherItems;
    }

    public String craftLeather() {
        return "Crafts a leather tunic.";
    }
    @Override
    public String getName() { return "Leatherworker"; }
    @Override
    public String getSkill() { return "Crafting leather armor and items."; }
}
