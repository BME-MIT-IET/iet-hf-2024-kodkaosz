import javax.swing.*;
import java.awt.*;

/**
 * A csövek aktuális állapotának a grafikus megjelenítéséért felelős osztály.
 */
public class PipeView extends SpriteView {
    /**
     * Referencia arra az objektumra, amelyiknek a megjelenítéséért felelős az osztály adott példánya.
     */
    private Pipe object;

    /**
     * Az csövek kirajzolása erre a panelre történik. Felül van definiálva a paintComponent függvénye.
     */
    private JPanel pipePanel;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    /**
     * Konstruktor.
     * @param p megjeleníteni kívánt Pipe
     */
    public PipeView(Pipe p) {
        object = p;
        pipePanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                // szin beallitasa
                int rgb;
                if (object.isDamaged()) {
                    if (object.getWaterLevel() > 0) {
                        rgb = 0xFF0000;
                    }
                    else {
                        rgb = 0x7B0000;
                    }
                }
                else {
                    if (object.getWaterLevel() > 0) {
                        rgb = 0x00B2FF;
                    }
                    else {
                        rgb = 0x00000;
                    }
                }
                // szaggatottság
                BasicStroke line = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
                if (object.getIsSlippery()) {
                    float[] dash = { 15, 15 };
                    line = new BasicStroke(5,
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_ROUND,
                            1.0f,
                            dash,
                            2f);
                }
                else if (object.getIsSticky()) {
                    float[] dash1 = { 5, 5 };
                    line = new BasicStroke(5,
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_ROUND,
                            1.0f,
                            dash1,
                            2f);
                }
                g2d.setColor(new Color(rgb));
                g2d.setStroke(line);

                if (object.neighbors.size() == 2) {
                    x1 = object.getNeighborElement(0).getView().posX;
                    y1 = object.getNeighborElement(0).getView().posY;
                    x2 = object.getNeighborElement(1).getView().posX;
                    y2 = object.getNeighborElement(1).getView().posY;
                    posX = (x1+x2)/2;
                    posY = (y1+y2)/2;
                    g2d.drawLine(x1 + 25, y1 + 25, x2 + 25, y2 + 25);
                }
            }
        };

        pipePanel.setSize(new Dimension(500, 500));
        pipePanel.setOpaque(false);
        pipePanel.setVisible(true);
        MainWindow.getInstance().getCanvas().add(pipePanel, BorderLayout.CENTER);

        update();
    }

    /**
     * A modell valamilyen eleme hívja meg rajta, ezzel jelezve, hogy
     * változás történt, ezután meghívja a tartalmazó panel repaint() függvényét, ezzel kirajzolva a változást.
     */
    public void update() {
        pipePanel.repaint();
    }

}
