package jobs;

public class Shepherd implements Job {
    private int woolStock;

    public Shepherd() {
        this.woolStock = 20;
    }

    public int getWoolStock() { return woolStock; }

    public void setWoolStock(int woolStock) {
        this.woolStock = woolStock;
    }

    public String shearSheep() {
        return "Shears a sheep, wool stock: " + ++woolStock;
    }
    @Override
    public String getName() { return "Shepherd"; }
    @Override
    public String getSkill() { return "Sheep herding and wool crafting."; }
}
