package biomes;

public class Savanna implements Biome {
    private int grassHeight;

    public Savanna() {
        this.grassHeight = 2;
    }

    public int getGrassHeight() {
        return grassHeight;
    }

    public void setGrassHeight(int grassHeight) {
        this.grassHeight = grassHeight;
    }

    public String describeGrass() {
        return "Tall yellow grass, height: " + grassHeight + " units.";
    }
    @Override
    public String getName() {
        return "Savanna";
    }
    @Override
    public String getAppearance() {
        return "Wears light earth-toned clothing and straw hat.";
    }
}
