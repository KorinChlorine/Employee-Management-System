import biomes.*;
import java.awt.*;
import javax.swing.*;
import jobs.*;

public class VillagerSelector extends JFrame {
    private JComboBox<String> biomeCombo;
    private JComboBox<String> jobCombo;
    private JButton showButton;

    private VillagerImageViewer viewer;

    private final String[] jobs = {
        "Unemployed", "Nitwit", "Armorer", "Butcher", "Cartographer", "Cleric",
        "Farmer", "Fisherman", "Fletcher", "Leatherworker", "Librarian",
        "Mason", "Shepherd", "Toolsmith", "Weaponsmith"
    };

    private final String[] biomes = {
        "Plains", "Desert", "Savanna", "Taiga", "Snowy", "Swamp", "Jungle"
    };

    public VillagerSelector() {
        setTitle("Villager Selector");
        setLayout(new FlowLayout());

        biomeCombo = new JComboBox<>(biomes);
        jobCombo = new JComboBox<>(jobs);
        showButton = new JButton("Show Villager");

        add(new JLabel("Select Biome:"));
        add(biomeCombo);
        add(new JLabel("Select Job:"));
        add(jobCombo);
        add(showButton);

        viewer = new VillagerImageViewer();

        showButton.addActionListener(e -> {
            String selectedBiome = (String) biomeCombo.getSelectedItem();
            String selectedJob = (String) jobCombo.getSelectedItem();

            // Create biome and job objects based on selection
            Biome biomeObj = createBiome(selectedBiome);
            Job jobObj = createJob(selectedJob);

            // Show villager image and details
            viewer.updateVillager(selectedBiome, selectedJob);

            // Display some unique info from biome and job
            String info = "<html>Biome info: " + biomeObj.getAppearance() + "<br>" +
            "Job info: " + jobObj.getSkill() + "</html>";
            viewer.showInfo(info);
        });

        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Factory methods for biome and job
    private Biome createBiome(String biomeName) {
        switch (biomeName.toLowerCase()) {
            case "plains": return new Plains();
            case "desert": return new Desert();
            case "savanna": return new Savanna();
            case "taiga": return new Taiga();
            case "snowy": return new Snowy();
            case "swamp": return new Swamp();
            case "jungle": return new Jungle();
            default: return new DefaultBiome();
        }
    }

    private Job createJob(String jobName) {
        switch (jobName.toLowerCase()) {
            case "armorer": return new Armorer();
            case "butcher": return new Butcher();
            case "cartographer": return new Cartographer();
            case "cleric": return new Cleric();
            case "farmer": return new Farmer();
            case "fisherman": return new Fisherman();
            case "fletcher": return new Fletcher();
            case "leatherworker": return new Leatherworker();
            case "librarian": return new Librarian();
            case "mason": return new Mason();
            case "shepherd": return new Shepherd();
            case "toolsmith": return new Toolsmith();
            case "weaponsmith": return new Weaponsmith();
            case "nitwit": return new Nitwit();
            case "unemployed": return new Unemployed();
            default: return new Job() {
                public String getName() { return "Unknown"; }
                public String getSkill() { return "No skill info"; }
            };
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VillagerSelector::new);
    }
}
