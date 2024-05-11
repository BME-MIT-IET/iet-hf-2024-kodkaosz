import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WaterSourceView extends SpriteView {

    private WaterSource object;
    private JPanel panel;

    /**
     * A vízforrást ábrázoló kép.
     */
    private static Image image;

    /**
     * Betölti a megfelelő képet
     */
    static {

        try {

            image = ImageIO.read(new File("kepek/watersource.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        }
        catch(IOException e) {

            throw new RuntimeException(e);
        }
    }

    public WaterSourceView(int x, int y, WaterSource ws) {

        posX = x;
        posY = y;
        object = ws;

        panel = new JPanel() {

            @Override
            public void paintComponent(Graphics g) {

                g.drawImage(image, posX, posY, null);
            }
        };

        panel.setSize(500, 500);
        panel.setOpaque(false);
        panel.setVisible(true);

        MainWindow.getInstance().getCanvas().add(panel, BorderLayout.CENTER);
        //MainWindow.getInstance().getCanvas().repaint();

        update();
    }

    /**
     * A modell valamilyen eleme hívja meg rajta, ezzel jelezve, hogy
     * változás történt, ezután meghívja a tartalmazó panel repaint() függvényét, ezzel kirajzolva a változást.
     */
    public void update() {
        panel.repaint();
    }
}
