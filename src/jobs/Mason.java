package jobs;

public class Mason implements Job {
    private int bricksMade;

    public Mason() {
        this.bricksMade = 50;
    }

    public int getBricksMade() { return bricksMade; }

    public void setBricksMade(int bricksMade) {
        this.bricksMade = bricksMade;
    }

    public String craftBrick() {
        return "Molds bricks. Total: " + ++bricksMade;
    }
    @Override
    public String getName() { return "Mason"; }
    @Override
    public String getSkill() { return "Stone and brick crafting."; }
}
