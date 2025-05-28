package biomes;

public class Snowy implements Biome {
    private int frostLevel;

    public Snowy() {
        this.frostLevel = 80;
    }

    public int getFrostLevel() {
        return frostLevel;
    }

    public void setFrostLevel(int frostLevel) {
        this.frostLevel = frostLevel;
    }

    public String describeFrost() {
        return "High frost levels: " + frostLevel + "%";
    }
    @Override
    public String getName() {
        return "Snow";
    }
    @Override
    public String getAppearance() {
        return "Covered in thick wool and scarves.";
    }
}
