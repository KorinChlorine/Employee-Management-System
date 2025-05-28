package biomes;

public class Jungle implements Biome {
    private int canopyDensity;

    public Jungle() {
        this.canopyDensity = 95;
    }

    public int getCanopyDensity() {
        return canopyDensity;
    }

    public void setCanopyDensity(int canopyDensity) {
        this.canopyDensity = canopyDensity;
    }

    public String describeCanopy() {
        return "Dense jungle canopy: " + canopyDensity + "% coverage.";
    }

    @Override
    public String getName() {
        return "Jungle";
    }
    @Override
    public String getAppearance() {
        return "Wears leafy clothes and carries vines.";
    }
}
