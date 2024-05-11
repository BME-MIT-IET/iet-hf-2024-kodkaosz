import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlumberView extends SpriteView{

    private Plumber object;

    private JPanel panel;
    private JButton button;

    private PlumberController controller;
    /**
     * A szerelő lehetséges állapotait ábrázoló képek.
     */
    private static ArrayList<Image> images;
    static {
        try {
            images = new ArrayList<>();
            images.add(ImageIO.read(new File("kepek/plumber.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            images.add(ImageIO.read(new File("kepek/plumber_withpipe.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            images.add(ImageIO.read(new File("kepek/plumber_withpump.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Az aktuális állapotnak megfelelő kép indexe az images listában.
     */
    private int currentImg;

    public PlumberView(Plumber p) {
        object = p;
        posX = object.getPipelineElement().getView().posX;
        posY = object.getPipelineElement().getView().posY;
        currentImg = 0;
        object = p;
        controller = new PlumberController(object);
        panel = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                //g.drawImage(images.get(currentImg), posX, posY, null);
            }
        };
        panel.setSize(new Dimension(500, 500));
        button = new JButton();
        panel.setLayout(null);
        button.setFocusable(false);
        button.setIcon(new ImageIcon(images.get(currentImg)));
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

        //TODO x y valtoztatasa
        posX = object.getPipelineElement().getView().posX;
        posY = object.getPipelineElement().getView().posY;

        if(object.getPickedUpPipe() != null){
            currentImg = 1;
        }
        else if(object.getPickedUpPump() != null){
            currentImg = 2;
        }
        else{
            currentImg = 0;
        }
        button.setIcon(new ImageIcon(images.get(currentImg)));
        button.setBounds(posX, posY, 40, 40);
        panel.repaint();
    }
}
