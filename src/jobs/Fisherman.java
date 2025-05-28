package jobs;

public class Fisherman implements Job {
    private int fishCaught;

    public Fisherman() {
        this.fishCaught = 12;
    }

    public int getFishCaught() { return fishCaught; }

    public void setFishCaught(int fish) {
        this.fishCaught = fish;
    }

    public String catchFish() {
        return "Catches a fish. Total: " + ++fishCaught;
    }
    @Override
    public String getName() { return "Fisherman"; }
    @Override
    public String getSkill() { return "Fishing and net crafting."; }
}
