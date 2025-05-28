package jobs;

public class Butcher implements Job {
    private int meatStock;

    public Butcher() {
        this.meatStock = 25;
    }

    public int getMeatStock() {
        return meatStock;
    }

    public void setMeatStock(int stock) {
        this.meatStock = stock;
    }

    public String processMeat() {
        return "Processes " + meatStock + " cuts of meat.";
    }

    @Override
    public String getName() { return "Butcher"; }

    @Override
    public String getSkill() { return "Meat processing and trade."; }
}
