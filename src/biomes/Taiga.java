package biomes;

public class Taiga implements Biome {
    private int snowfall;

    public Taiga() {
        this.snowfall = 15;
    }

    public int getSnowfall() {
        return snowfall;
    }

    public void setSnowfall(int snowfall) {
        this.snowfall = snowfall;
    }

    public String describeSnowfall() {
        return "Moderate snowfall of " + snowfall + " cm.";
    }
    @Override
    public String getName() {
        return "Taiga";
    }
    @Override
    public String getAppearance() {
        return "Wears fur-lined coats and mittens.";
    }
}
