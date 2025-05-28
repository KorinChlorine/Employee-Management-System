package jobs;

public class Toolsmith implements Job {
    private int toolsForged;

    public Toolsmith() {
        this.toolsForged = 8;
    }

    public int getToolsForged() { return toolsForged; }

    public void setToolsForged(int toolsForged) {
        this.toolsForged = toolsForged;
    }

    public String forgeTool() {
        return "Forges a pickaxe.";
    }
    @Override
    public String getName() { return "Toolsmith"; }
    @Override
    public String getSkill() { return "Tool creation and repair."; }
}
