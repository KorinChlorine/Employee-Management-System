package jobs;

public class Armorer implements Job {
    private int armorQuality = 85;

    public int getArmorQuality() {
        return armorQuality;
    }

    public void setArmorQuality(int q) {
        this.armorQuality = q;
    }

    @Override
    public String getName() {
        return "Armorer";
    }

    @Override
    public String getSkill() {
        return "Smithing armor and chainmail.";
    }

    public String forgeArmor() {
        return "Forges strong armor with quality: " + armorQuality;
    }
}
