import java.awt.*;
import javax.swing.*;

public class VillagerImageViewer extends JFrame {
    private JLabel label;
    private JLabel imageLabel;
    private JLabel infoLabel;

    public VillagerImageViewer() {
        setTitle("Villager Viewer");
        setSize(300, 450);
        setLayout(new BorderLayout());

        label = new JLabel("", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(label, BorderLayout.NORTH);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(imageLabel, BorderLayout.CENTER);

        infoLabel = new JLabel("", SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        add(infoLabel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void updateVillager(String biome, String job) {
        // (Same proportional scaling + biome fix as before)
        if (biome.equalsIgnoreCase("Snow")) biome = "Snowy";

        label.setText(biome + " - " + job);

        String imageName;
        if (job.equalsIgnoreCase("Unemployed") || job.equalsIgnoreCase("Nitwit")) {
            imageName = "images/" + biome + "_Villager_Base_JE2.png";
        } else {
            imageName = "images/" + biome + "_" + job + ".png";
        }

        ImageIcon icon = new ImageIcon(imageName);
        Image img = icon.getImage();

        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();

        int maxWidth = 200;
        int maxHeight = 200;

        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double scaleFactor = Math.min(widthRatio, heightRatio);

        int newWidth = (int)(originalWidth * scaleFactor);
        int newHeight = (int)(originalHeight * scaleFactor);

        Image scaled = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaled));
    }

    public void showInfo(String text) {
        infoLabel.setText(text);
    }
}
