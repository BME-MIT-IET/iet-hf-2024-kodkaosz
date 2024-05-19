package main.java.DrukmakoriSivatag;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A vízforrást reprezentálja. A rájuk kötött csövekbe a víz eljuttatása a feladata.
 */
public class WaterSource extends PipelineElement implements Tickable {

    /**
     * Az objektum állapotát egy megfelelő formátumú stringbe írja.
     * @return az objektum string formában
     */
    @Override
    public String toString() {

        String output = "WaterSource " + Main.proto.getByObject(this) + " neighbors:";
        for(int i = 0; i < neighbors.size(); ++i) {

            output = output.concat(Main.proto.getByObject(neighbors.get(i)));
            if(i < neighbors.size() - 1) { output = output.concat(","); }
        }

        output = output.concat(";isPickedUp:");
        if(isPickedUp) { output = output.concat("true"); }
        else { output = output.concat("false"); }

        return output;
    }

    /**
     * Fájl input szerint bekonfigurálja az objektumot
     * @param options: fájl-ból az inputok
     */
    public static void setup(Object obj, ArrayList<String> options) {

        PipelineElement.setup(obj, new ArrayList<>(options));
        ((Game)Main.proto.getByName("g")).addTickable((Tickable)obj);
    }

    /**
     * Végig iterál a neighbors tömbön. Minden tömbelemnek lekérdezi a
     * kapacitását, meghívja a propagateWater(PipelineElement from, int qty) metódusát,
     * önmagát és a lekérdezett kapacitást megadva paraméternek, pontosan annyi vizet juttat
     * a forrás a csőbe, amennyi elfér benne
     */
    @Override
    public void tick() {

        for(PipelineElement pipelineElement : neighbors) {

            int capacity = ((Pipe)pipelineElement).getCapacity();
            pipelineElement.propagateWater(this, capacity);
        }
    }

    /**
     * Mindig false-al tér vissza, hiszen a forrásra nem léphet
     * játékos.
     * @param p: elemre lépni szándékozó játékos
     * @return
     */
    @Override
    public boolean accept(Person p) {

        return false;
    }

    /**
     * mindig 0-val tér vissza, a
     * forrásnak nem lehet vizet továbbítani
     * @param from: a hívó
     * @param qty: az átadni kívánt vízmennyiség
     * @return
     */
    public int propagateWater(PipelineElement from, int qty) {

        return 0;
    }
}
