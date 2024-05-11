import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class SaboteurController extends SpriteController{

    private Saboteur object;
    private String choice;
    public SaboteurController(Saboteur s){
        menu = new SaboteurMenu(this);
        object = s;

    }
    /**
     * Ez a függvény jeleníti meg a megfelelő menüt
     * (leszármazottakban megvalósítva).
     */
    protected void displayMenu(){
        menu.showInput(object.getView().posX, object.getView().posY);
    }
    public void gotChoice(String c){
        choice = c;
        switch(choice){
            case "Move":
                break;
            case "Damage":
                object.damagePipe();
                break;
            case "Redirect":
                break;
            case "Make Sticky":
                object.makeSticky();
                break;
            case "Make Slippery":
                object.makeSlippery();
                break;
            case "":
                break;
        }
    }

    public void gotInput(ArrayList<String> input){
        switch(choice) {
            case "Move":
                object.move(Integer.parseInt(input.get(0)));
                break;
            case "Redirect":
                object.redirectPump(Integer.parseInt(input.get(0)), Integer.parseInt(input.get(1)));
                break;
        }
    }

    /**
     * Hívása esetén megjeleníti a hozzá tartozó
     * menüt, majd a menüből történt választás után végrehajtja a kiválasztott akciót.
     * (leszármazottakban megvalósítva).
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e){
        displayMenu();
    }
}
