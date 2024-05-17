package DrukmakoriSivatag;

import java.util.ArrayList;

/**
 * A pumpák víztartályának a reprezentációja.
 * Ebben kerül eltárolásra a pumpában lévő víz mennyisége,
 * és ez az osztály felelős ennek a mennyiségnek a megváltoztatásáért is.
 */
public class PumpTank {

    int storedWater;
    int capacity;

    /**
     * A PumpTank osztály konstruktora.
     */
    public PumpTank() { }

    /**
     * Az objektum állapotát egy megfelelő formátumú stringbe írja.
     * @return az objektum string formában
     */
    @Override
    public String toString() {

        String output = "PumpTank " + Main.proto.getByObject(this) + " storedWater:";
        output = output.concat(Integer.toString(storedWater));
        output = output.concat(";capacity:");
        output = output.concat(Integer.toString(capacity));

        return output;
    }

    /**
     * Fájl input szerint bekonfigurálja az objektumot
     * @param options: fájl-ból az inputok
     */
    public static void setup(Object obj, ArrayList<String> options) {

        ((PumpTank)obj).storedWater = Integer.parseInt(options.get(0));
        ((PumpTank)obj).capacity = Integer.parseInt(options.get(1));
    }

    /**
     * A storedWater-t növeli a paraméterként kapott értékkel a kapacitásáig.
     * Visszatér azzal, hogy mennyi vizet tudott sikeresen hozzáadni.
     * @param qty: viz mennyiseg
     * @return: mennyi vizet tarolt el
     */
    public int addWater(int qty) {

        if(storedWater + qty <= capacity) {

            setStoredWater(storedWater + qty);
            return qty;
        }
        else {

            int addedWater = capacity - storedWater;
            setStoredWater(capacity);

            return addedWater;
        }
    }

    /**
     * A storedWater értékét csökkenti a paraméterként kapott qty értékkel.
     0 alá nem mehet.
     * @param qty: eltavolitando viz mennyiseg
     */
    public void removeWater(int qty) {

        if(storedWater - qty < 0) { setStoredWater(0); }
        else { setStoredWater(storedWater - qty); }
    }

    // getters and setters

    /**
     * A viz mennyiseg setter-e
     * @param qty: uj viz mennyiseg
     */
    public void setStoredWater(int qty) {

        this.storedWater = qty;
    }
}
