package main.java.DrukmakoriSivatag;

import java.util.ArrayList;

/**
 * A ciszterna megvalósítása. Eltárolja és jelenti a Gamenek, hogy mennyi víz van benne, ez a vízmennyiség
 * csökkenhet is, ugyanis el tud szivárogni a víz, ha van olyan Pipe rákötve, amelynek szabad a vége.
 */
public class WaterTank extends PipelineElement implements Tickable {
    /**
     * A ciszternában lévő víz mennyisége.
     */
    private int waterLevel;
    /**
     * A véletlenszerűen bekövetkező eseményeket létrehozó Random objektum.
     */
    private final Random random = new Random();
    /**
     * Referencia az egyetlen Game objektumra.
     */
    //private final Game game = (Game)Main.proto.getByName("g");

    /**
     * A WaterTank paraméter nélüli konstruktora.
     */
    public WaterTank() {}

    //
    // teszt dolog
    //
    /**
     * Akkor tér vissza true-val, ha sikeresen eltávolította a paraméterben megadott elemet a neighbor tömbből.
     * Ez akkor lehet sikeres, hogyha nem olyan csövet akar lecsatlakoztatni, amelyikből, vagy amelyikbe pumpál.
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

    /**
     * Egy teljes cső felvételét megvalósító függvény.
     * @param i: Hányadik csövet szeretné lecsatlakoztatni
     * @return sikerült-e lecsatlakoztatni
     */
    public Pipe pickUpPipe(int i){
        Pipe pickedUpPipe = (Pipe)getNeighborElement(i);
        if (this.disconnect(pickedUpPipe) && pickedUpPipe.getOtherNeighbor(this).disconnect(pickedUpPipe)) {
            pickedUpPipe.setIsPickedUp(true);
            return pickedUpPipe;
        }

        return null;
    }
    //
    //tesztresz vege
    //
    /**
     * Fájl input szerint beállítja a kapott objektumot.
     * @param obj a beállítani kívánt objektum
     * @param params a paramétereket stringként a megfelelő formában tartalmazó lista
     */
    public static void setup(Object obj, ArrayList<String> params) {
        ArrayList<String> pipelineElementParams = new ArrayList<>();
        pipelineElementParams.add(params.get(0));
        pipelineElementParams.add(params.get(1));
        PipelineElement.setup(obj, pipelineElementParams);

        ((WaterTank)obj).waterLevel = Integer.parseInt(params.get(2));
        ((Game)Main.proto.getByName("g")).addFinishedWater(((WaterTank) obj).waterLevel);

        ((Game)Main.proto.getByName("g")).addTickable((Tickable)obj);
    }

    /**
     * Az objektum állapotát egy megfelelő formátumú stringbe írja.
     * @return az objektum string formában
     */
    @Override
    public String toString() {
        String output = "WaterTank " + Main.proto.getByObject(this) + " neighbors:";
        for (int i = 0; i < neighbors.size(); i++) {
            output = output.concat(Main.proto.getByObject(neighbors.get(i)));
            if (i < neighbors.size() - 1)
                output = output.concat(",");
        }

        output = output.concat(";isPickedUp:");
        if (isPickedUp) output = output.concat("true");
        else output = output.concat("false");

        output = output.concat(";waterLevel:" + waterLevel);

        return output;
    }

    /**
     * Létrehoz egy új pumpát a ciszternánál.
     * @return a létrejott Pump
     */
    public Pump createPump() {
        Pump p = new Pump();
        Main.proto.addObject("newPump", p);
        return p;
    }

    /**
     * Létrehoz egy új csövet a ciszternánál.
     * @return a létrehozott Pipe
     */
    public Pipe createPipe() {
        Pipe p = new Pipe();
        Main.proto.addObject("newPipe", p);
        p.addNeighbor(((Game)Main.proto.getByName("g")).getDesert());
        p.addNeighbor(this);
        p.setCapacity(5);
        return p;
    }

    /**
     * A WaterTank tick() implementációja. Meghívása után először végigiterál a neighbors tömbjén,
     * az összes elemen meghívja a seepWater(PipelineElement from, int qty), függvényét, önmagát és a
     * tárolt vízmennyiséget megadva paraméternek, majd ennek a visszatérési értékét kivonja a tárolt
     * vízmennyiségből és meghívja a Game removeFinishedWater(int qty) függvényét, szintén az előző függvény
     * visszatérési értékét megadva paraméternek. Ezután a Random decideNewPipe() függvényét hívja, ha az hamis
     * értékkel tér vissza, nem történik semmi, ha igazzal, akkor a Gametől lekéri a Desert referenciáját,
     * létrehoz egy új Pipe objektumot és beállítja a szomszédainak önmagát és a kapott Derest referenciát.
     */
    @Override
    public void tick() {
        //seepwater iteralas neighbors tombon
        for (PipelineElement p : this.neighbors) {
            int seepedWater = p.seepWater(this, waterLevel);
            //((Game)Main.proto.getByName("g")).removeFinishedWater(seepedWater); // nem kell, mert így kétszer hívódna
            waterLevel -= seepedWater;
        }

        //random cső létrehozás
        if (random.decideNewPipe()) {
            Pipe newPipe = createPipe();
        }
    }

    /**
     * a Plumber pickUpPump() függvénye hívja meg, amikor új pumpát szeretne felvenni. Ilyenkor a függvény
     * létrehoz egy új Pump objektumot, majd meghívja a Plumber setPickedUpPump(Pump p) függvényét,
     * paraméterként az új Pumpot megadva.
     * @param p a hívó szerelő
     * @return a felvett pumpa objektum referenciája
     */
    @Override
    public Pump pickUpPump(Person p) {
        Pump newPump = createPump();
        p.setPickedUpPump(newPump);
        return newPump;
    }

    /**
     * Egy hozzá kapcsolódó Pipe hívja meg rajta. Híváskor a saját vízszintjéhez hozzááadja a beérkező (qty)
     * vízmennyiséget, majd a Game osztály addFinishedWater(int qty) függvényét meghívva frissíti a
     * Gameben feljegyzett vízmennyiséget.
     * @param from a hívó
     * @param qty a beérkező vízmennyiség
     * @return a sikeresen fogadott vízmennyiség
     */
    @Override
    public int propagateWater(PipelineElement from, int qty) {
        ((Game)Main.proto.getByName("g")).addFinishedWater(qty);
        setWaterLevel(waterLevel + qty);
        return qty;
    }

    /**
     * Mindig true-val tér vissza, mert akárhányan állhatnak a ciszternán.
     * @param p a hívó, a csőre lépni kívánó karakter
     * @return always true, ide mindig lehet lépni
     */
    @Override
    public boolean accept(Person p) {
        return true;
    }

    /**
     * Setter a waterLevel attribútumhoz.
     * @param waterLevel beállítani kívánt érték.
     */
    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }
}
