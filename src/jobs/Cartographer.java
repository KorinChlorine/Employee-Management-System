package jobs;

public class Cartographer implements Job {
    private int mapsMade;

    public Cartographer() {
        this.mapsMade = 3;
    }

    public int getMapsMade() { return mapsMade; }

    public void setMapsMade(int mapsMade) {
        this.mapsMade = mapsMade;
    }

    public String makeMap() {
        return "Draws map number " + (mapsMade + 1);
    }

    @Override
    public String getName() { return "Cartographer"; }
    
    @Override
    public String getSkill() { return "Drawing and selling maps."; }
}
