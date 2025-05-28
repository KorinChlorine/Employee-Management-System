package biomes;

public class Plains implements Biome {
    private String vegetationType = "Tall Grass";

    public String getVegetationType() {
        return vegetationType;
    }

    public void setVegetationType(String type) {
        this.vegetationType = type;
    }

    @Override
    public String getName() {
        return "Plains";
    }

    @Override
    public String getAppearance() {
        return "Wears green tunic with neutral tones.";
    }

    public String describeFlora() {
        return "Vegetation: " + vegetationType;
    }
}
