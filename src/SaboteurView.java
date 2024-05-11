import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SaboteurView extends SpriteView{
    Saboteur object;
    private JPanel panel;
    private JButton button;

    private SaboteurController controller;
    /**
     * A szabotőrt ábrázoló kép.
     */
    private static Image image;

    static {
        try {
            image = ImageIO.read(new File("kepek/saboteur.png"));
            image = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public SaboteurView(Saboteur s) {
        object = s; //TODO uncomment
        posX = object.getPipelineElement().getView().posX;
        posY = object.getPipelineElement().getView().posY;
        controller = new SaboteurController(object);
        panel = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                //g.drawImage(image, posX, posY, null);
            }
        };
        panel.setSize(new Dimension(500, 500));
        panel.setLayout(null);
        button = new JButton();
        button.setFocusable(false);
        button.setIcon(new ImageIcon(image));
        button.setBounds(posX, posY, 40, 40);
        button.addActionListener(controller);
        panel.add(button);
        panel.setOpaque(false);
        panel.setVisible(true);
        MainWindow.getInstance().getCanvas().add(panel, BorderLayout.CENTER);
        MainWindow.getInstance().getCanvas().validate();

        panel.repaint();
    }

    /**
     * A modell valamilyen eleme hívja meg rajta, ezzel jelezve, hogy
     * változás történt, ezután visszahívja az adott elemen a state() függvényét, majd az ebből
     * visszakapott paraméterek alapján frissíti a megjelenítést.
     */
    public void update() {

        posX = object.getPipelineElement().getView().posX;
        posY = object.getPipelineElement().getView().posY;
        button.setBounds(posX, posY, 40, 40);
        panel.repaint();
    }
}
