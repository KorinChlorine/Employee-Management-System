package jobs;

public class Weaponsmith implements Job {
    private int weaponsSold;

    public Weaponsmith() {
        this.weaponsSold = 4;
    }

    public int getWeaponsSold() { return weaponsSold; }

    public void setWeaponsSold(int weaponsSold) {
        this.weaponsSold = weaponsSold;
    }

    public String sellWeapon() {
        return "Sells a sword.";
    }
    @Override
    public String getName() { return "Weaponsmith"; }
    @Override
    public String getSkill() { return "Forging and selling weapons."; }
}
