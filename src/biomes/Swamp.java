package biomes;

public class Swamp implements Biome {
    private int humidity;

    public Swamp() {
        this.humidity = 90;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String describeHumidity() {
        return "Humidity at " + humidity + "% — very damp and foggy.";
    }
    @Override
    public String getName() {
        return "Swamp";
    }
    @Override
    public String getAppearance() {
        return "Wears mossy robes and carries herbs.";
    }
}
