import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A ciszterna grafikus megjelenítéséért felelős osztály.
 */
public class WaterTankView extends SpriteView {
    /**
     * A ciszternát ábrázoló kép.
     */
    private static Image image;

    static {
        try {
            image = ImageIO.read(new File("kepek/watertank.png")).getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Referencia arra az objektumra, amelyiknek a megjelenítéséért felelős az osztály adott példánya.
     */
    private WaterTank object;
    public JPanel waterTankPanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, posX, posY, null);
        }
    };

    /**
     * Konstruktor.
     * @param x X koordináta
     * @param y Y koordináta
     * @param wt a megjeleníteni kívánt WaterTank
     */
    public WaterTankView(int x, int y, WaterTank wt) {
        posX = x;
        posY = y;
        object = wt;

        waterTankPanel.setSize(new Dimension(500, 500));
        waterTankPanel.setOpaque(false);
        waterTankPanel.setVisible(true);
        MainWindow.getInstance().getCanvas().add(waterTankPanel, BorderLayout.CENTER);

        update();
    }

    /**
     * A modell valamilyen eleme hívja meg rajta, ezzel jelezve, hogy
     * változás történt, ezután meghívja a tartalmazó panel repaint() függvényét, ezzel kirajzolva a változást.
     */
    public void update() {
        waterTankPanel.repaint();
    }
}
