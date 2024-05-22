package main.java.DrukmakoriSivatag;

import java.util.ArrayList;

/**
 * A sivatagot reprezentálja, ahova elfolyik a víz az üres végű csövekből.
 */
public class Desert extends PipelineElement {

    /**
     * Az objektum állapotát egy megfelelő formátumú stringbe írja.
     *
     * @return az objektum string formában
     */
    @Override
    public String toString() {

        String output = "Desert " + Main.proto.getByObject(this) + " neighbors:";
        for (int i = 0; i < neighbors.size(); ++i) {

            output = output.concat(Main.proto.getByObject(neighbors.get(i)));
            if (i < neighbors.size() - 1) {
                output = output.concat(",");
            }
        }

        output = output.concat(";isPickedUp:");
        if (isPickedUp) {
            output = output.concat("true");
        } else {
            output = output.concat("false");
        }

        return output;
    }

    /**
     * Fájl input szerint bekonfigurálja az objektumot
     *
     * @param options: fájl-ból az inputok
     */
    public static void setup(Object obj, ArrayList<String> options) {
        PipelineElement.setup(obj, new ArrayList<>(options));
        ((Game) Main.proto.getByName("g")).setDesert((Desert) obj);
    }

    /**
     * A PipelineElement-től örökölt metódus, mindig false-al tér vissza, mivel nem lehet a sivatagra lépni.
     *
     * @param p: input jatekos
     * @return: mindig false
     */
    @Override
    public boolean accept(Person p) {
        return false;
    }

    /**
     * Visszatér azzal az értékkel, amennyit elfogadott a szivárgásból, és levonja a szerelők pontszámából.
     * Mivel nincs kapacitása, ezért mindig a kapott qty paraméterrel tér vissza.
     *
     * @param from: az elem, amelyik meghívja a függvényt
     * @param qty:  az átadni kívánt vízmennyiség
     * @return
     */
    @Override
    public int seepWater(PipelineElement from, int qty) {
        Game game = (Game) Main.proto.getByName("g");
        game.removeFinishedWater(qty);
        return qty;
    }

    /**
     * Akkor tér vissza true-val, ha sikeresen eltávolította a paraméterben megadott elemet a neighbor tömbből.
     * Ez akkor lehet sikeres, hogyha nem olyan csövet akar lecsatlakoztatni, amelyikből, vagy amelyikbe pumpál.
     *
     * @param p: Melyik csövet csatlakoztatjuk le
     * @return
     */
    @Override
    public boolean disconnect(Pipe p) {
        p.removeNeighbor(this);
        neighbors.remove(p);
        p.setIsPickedUp(true);
        return true;
    }
}
