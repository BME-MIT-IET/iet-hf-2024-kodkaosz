import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class PumpView extends SpriteView {

    private Pump object;
    private JPanel panel;

    /**
     * A pumpa lehetséges állapotait ábrázoló képek.
     */
    private static ArrayList<Image> images = new ArrayList<Image>();

    /**
     * Betölti a megfelelő képeket
     */
    static {

        try {

            Image workingImage = ImageIO.read(new File("kepek/pump.png")).getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH);
            Image notWorkingImage = ImageIO.read(new File("kepek/pump_not_working.png")).getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH);
            images.add(workingImage);
            images.add(notWorkingImage);
        }
        catch(IOException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     * Az aktuális állapotnak megfelelő kép indexe az images listában.
     */
    private int currentImage = 0;

    public PumpView(int x, int y, Pump p) {

        posX = x;
        posY = y;
        object = p;

        panel = new JPanel() {

            @Override
            public void paintComponent(Graphics g) {

                Graphics2D g2d = (Graphics2D) g;
                if(object.isDamaged) { currentImage = 1; }
                else { currentImage = 0; }

                g.drawImage(images.get(currentImage), posX, posY, null);
            }
        };
        panel.setSize(new Dimension(500, 500));
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

        /*
        List<Object> state = object.state();
        if((boolean)state.get(0) == true) { currentImage = 1; }
        else { currentImage = 0; }

        panel.removeAll();
        panel.getGraphics().drawImage(images.get(currentImage), posX, posY, panel);
        MainWindow.getInstance().getCanvas().repaint();
         */
    }
}
