package jobs;

public class Cleric implements Job {
    private int potionCount;

    public Cleric() {
        this.potionCount = 10;
    }

    public int getPotionCount() { return potionCount; }

    public void setPotionCount(int potionCount) {
        this.potionCount = potionCount;
    }

    public String brewPotion() {
        return "Brews healing potion. Total: " + ++potionCount;
    }
    @Override
    public String getName() { return "Cleric"; }
    @Override
    public String getSkill() { return "Potion brewing and enchantments."; }
}
