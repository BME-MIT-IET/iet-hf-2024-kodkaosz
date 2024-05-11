import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DesertView extends SpriteView {

    private Desert object;


    /**
     * A sivatagot ábrázoló kép.
     */
    private static BufferedImage image;

    /**
     * Betölti a megfelelő képet
     */
    static {

        try {

            image = ImageIO.read(new File("kepek/desert.png"));
        }
        catch(IOException e) {

            throw new RuntimeException(e);
        }
    }

    public DesertView(Desert d) {

        posX = 0;
        posY = 0;
        object = d;
    }

    /**
     * A modell valamilyen eleme hívja meg rajta, ezzel jelezve, hogy
     * változás történt, ezután visszahívja az adott elemen a state()
     * függvényét, majd az ebből visszakapott paraméterek alapján frissíti
     * a megjelenítést.
     */
    public void update() { }
}
