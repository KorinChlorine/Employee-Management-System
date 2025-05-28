package jobs;

public class Farmer implements Job {
    private String favoriteCrop;

    public Farmer() {
        this.favoriteCrop = "Wheat";
    }

    public String getFavoriteCrop() {
        return favoriteCrop;
    }

    public void setFavoriteCrop(String crop) {
        this.favoriteCrop = crop;
    }
    @Override
    public String getName() {
        return "Farmer";
    }
    @Override
    public String getSkill() {
        return "Growing and harvesting crops.";
    }

}
