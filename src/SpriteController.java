import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public abstract class SpriteController implements ActionListener {
    /**
     * SpriteView leszármazottak kollekciója, ezeket tudja
     * frissíteni a modell, amikor valamilyen változás történik benne.
     */
    private ArrayList<SpriteView> views;
    /**
     * A megjelenítendő menü.
     */
    protected ContextMenu menu;

    public void addView(SpriteView sv){
        views.add(sv);
    }
    /**
     * Ez a függvény jeleníti meg a megfelelő menüt
     * (leszármazottakban megvalósítva).
     */
    protected void displayMenu(){}
    public void gotChoice(String c){}
    public void gotInput(ArrayList<String> input){}
    /**
     * Hívása esetén megjeleníti a hozzá tartozó
     * menüt, majd a menüből történt választás után végrehajtja a kiválasztott akciót.
     * (leszármazottakban megvalósítva).
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e){}
}
