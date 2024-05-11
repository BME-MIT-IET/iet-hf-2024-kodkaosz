import java.util.ArrayList;
import java.util.List;

/**
 * Sprite osztály
 * Az MVC minta modell részéért felelős absztrakt ősosztály, függvényei segítségével
 * kapcsolatot teremt a megjelenítéssel.
 */
public abstract class Sprite {
    /**
     * SpriteView leszármazottak kollekciója, ezeket tudja
     * frissíteni a modell, amikor valamilyen változás történik benne
     */
   private List<SpriteView> views = new ArrayList<>();

    /**
     * default konstruktor
     */
    public Sprite(){

    }

    /**
     * Hozzáad egy újabb view osztályt a views
     * kollekciójához.
     * @param ctrl új view
     */
    public void addView(SpriteView ctrl){

        views.add(ctrl);
    }

}
