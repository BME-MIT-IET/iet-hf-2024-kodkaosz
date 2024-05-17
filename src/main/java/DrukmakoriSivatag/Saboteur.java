package DrukmakoriSivatag;

import java.util.ArrayList;
import java.util.Arrays;

public class Saboteur extends Person {
    /**
     * Az osztály konstruktora.
     */
    public Saboteur(){}

    /**
     *
     * Statikus függvény.
     * A paraméterként kapott objektum állapotát beállítja a megadott ArrayListben szereplő értékeknek megfelelően.
     *
     * @param obj Az a Person objektum, amelynek az állapotát be szeretnénk állítani.
     * @param params A parancsban megadott paraméterek.
     */

    static void setup(Object obj, ArrayList<String> params){
        Person.setup(obj, new ArrayList<>(params));
    }

    /**
     * Visszatér az objektum aktuális állapotának a leírásával.
     * @return Az objektum állapota egy Stringbe leírva.
     */
    @Override
    public String toString(){
        String output = "Saboteur " + Main.proto.getByObject(this) + " stuckCounter:"+ stuckCounter
                + ";element:" + Main.proto.getByObject(element);
        return output;
    }

    /**
     * Meghívja az element attribútum makeSlippery nevű
     * függvényét. Ha a játékos pont egy csövön van, akkor csúszósra állítja a csövet
     * egy tetszőleges ideig.
     */
    @Override
    public boolean makeSlippery(){
        return element.makeSlippery();
    }
}
