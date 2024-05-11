/**
 * SpriteView osztály
 * Absztrakt ősosztály, amely leszármazottjai
 * felelősek egyes pályaelemek megjelenítéséért.
 */
public abstract class SpriteView {
    /**
     * Ahhoz az pályaelemhez tartozó vezérlő, amelyet ábrázol
     */
    private SpriteController controller;
    /**
     *  A pályaelemet tartalmazó kép pozíciójának X koordinátája.
     */
    protected int posX;
    /**
     * A pályaelemet tartalmazó kép pozíciójának Y koordinátája.
     */
    protected int posY;

    /**
     * A modell valamilyen eleme hívja meg rajta, ezzel jelezve, hogy
     * változás történt, ezután visszahívja az adott elemen a state() függvényét, majd az ebből
     * visszakapott paraméterek alapján frissíti a megjelenítést.
     */
    public void update(){

    }
}
