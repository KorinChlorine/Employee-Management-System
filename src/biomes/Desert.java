package biomes;

public class Desert implements Biome {
    private int temperature = 45;

    public int getTemperature() { return temperature; }

    public void setTemperature(int temp) {
        this.temperature = temp;
    }

    public String describeClimate() {
        return "Hot and dry with temperature " + temperature + "°C.";
    }
    @Override
    public String getName() { return "Desert"; }
    @Override
    public String getAppearance() { return "Wears light robes and head coverings."; }
}
