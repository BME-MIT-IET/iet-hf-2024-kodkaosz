package main.java.DrukmakoriSivatag;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Ez az osztály a pumpákat implementálja. A pumpák a csomópontjai a vízvezeték-rendszernek, ezért akárhányan állhatnak rajta.
 * Amikor a játékos átállítja a pumpát, akkor valójában ennek az osztálynak a felelőssége beállítani,
 * hogy a rá csatlakoztatott csövek közül melyikből, melyikbe pumpáljon.
 */
public class Pump extends PipelineElement implements Tickable {

    boolean isDamaged;
    int toPipeIdx;
    int fromPipeIdx;
    Random random;
    PumpTank pumpTank;

    /**
     * A Pump osztály konstruktora.
     */
    public Pump() { }

    /**
     * Az objektum állapotát egy megfelelő formátumú stringbe írja.
     * @return az objektum string formában
     */
    @Override
    public String toString() {

        String output = "Pump " + Main.proto.getByObject(this) + " neighbors:";
        for(int i = 0; i < neighbors.size(); ++i) {
            
            output = output.concat(Main.proto.getByObject(neighbors.get(i)));
            if(i < neighbors.size() - 1) { output = output.concat(","); }
        }

        output = output.concat(";isPickedUp:");
        if(isPickedUp) { output = output.concat("true"); }
        else { output = output.concat("false"); }

        output = output.concat(";isDamaged:");
        if(isDamaged) { output = output.concat("true"); }
        else { output = output.concat("false"); }

        output = output.concat(";toPipeIdx:");
        output = output.concat(Integer.toString(toPipeIdx));
        output = output.concat(";fromPipeIdx:");
        output = output.concat(Integer.toString(fromPipeIdx));

        return output;
    }

    /**
     * Fájl input szerint bekonfigurálja az objektumot
     * @param options: fájl-ból az inputok
     */
    public static void setup(Object obj, ArrayList<String> options) {

        String[] pipelineElementParams = new String[2];
        pipelineElementParams[0] = options.get(0);
        pipelineElementParams[1] = options.get(1);
        PipelineElement.setup(obj, new ArrayList(Arrays.asList(pipelineElementParams)));

        ((Pump)obj).setPumpTank((PumpTank)Main.proto.getByName(options.get(2)));
        ((Pump)obj).isDamaged = options.get(3) == "true" ? true : false;
        ((Pump)obj).toPipeIdx = Integer.parseInt(options.get(4));
        ((Pump)obj).fromPipeIdx = Integer.parseInt(options.get(5));

        ((Game)Main.proto.getByName("g")).addTickable((Tickable)obj);
    }

    /**
     * Az ősosztálytól örökölt metódus implementációja.
     * A Person osztály azonos paraméterezésű redirectPump függvénye hívja meg.
     * @param firstIdx: Melyik indexű szomszéd irányából follyon a víz
     * @param secondIdx: Melyik indexű szomszéd irányába follyon a víz
     */
    @Override
    public boolean redirect(int firstIdx, int secondIdx) {

        Pipe first = (Pipe)getNeighborElement(firstIdx);
        Pipe second = (Pipe)getNeighborElement(secondIdx);

        boolean isFirstNeighbor = this.isNeighbor(first);
        boolean isSecondNeighbor = this.isNeighbor(second);

        boolean areDifferent = firstIdx == secondIdx ? false : true;
        boolean areNeighbors = isFirstNeighbor && isSecondNeighbor ? true : false;

        if(areDifferent && areNeighbors) {

            this.setFromPipe(firstIdx);
            this.setToPipe(secondIdx);

            return true;
        }

        return false;
    }

    /**
     * A bemeneti csőből a kimenetre való pumpálást valósítja meg.
     * @param from: Melyik PipelineElement-ből folyik a víz
     * @param qty: Mennyi víz folyik
     * @return Mennyi víy folyt
     */
    @Override
    public int propagateWater(PipelineElement from, int qty) {

        if(!isDamaged) { return -1; }
        else {

            int fromIdx = getIndex(from);
            if(fromIdx == fromPipeIdx) {

                int addedWater = pumpTank.addWater(qty);
                Pipe toPipe = (Pipe)getNeighborElement(toPipeIdx);
                int acceptedWater = toPipe.propagateWater(this, qty);
                pumpTank.removeWater(acceptedWater);

                return qty;
            }

            return -1;
        }
    }

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
     * Az ősosztálytól örökölt metódus, egyedül ez az osztály implementálja.
     * A Plumber osztály ezzel megegyező nevű metódusa használja.
     * @param i: Melyik indexű csövet vesszük fel
     * @return: Felvett cső
     */
    @Override
    public Pipe pickUpPipeEnd(int i) {

        Pipe pickedUpPipe = (Pipe)getNeighborElement(i);
        if(this.disconnect(pickedUpPipe)) { return pickedUpPipe; }

        return null;
    }

    /**
     * A cső egyik felvett végének a hálózatba csatlakoztatásáért felelős.
     * Az előző függvényhez hasonlóan csak ez az egy osztály valósítja meg.
     * @param pickedUpPipe: Felvett cső referencia
     * @return: Sikerült-e a felcsatlakoztatás
     */
    @Override
    public boolean connectPipeEnd(Pipe pickedUpPipe) {

        pickedUpPipe.addNeighbor(this);
        pickedUpPipe.setIsPickedUp(false);
        return true;
    }

    /**
     * Mindig true-val tér vissza, mert akárhányan ráléphetnek egyszerre a pumpára.
     * @param p: Játékos referencia
     * @return: Sikerült-e a lépés
     */
    @Override
    public boolean accept(Person p) {

        players.add(p);
        return true;
    }

    /**
     * Az isDamaged értékét true-ra állítja.
     */
    @Override
    public boolean fix() {

        this.setIsDamaged(false);
        return true;
    }

    /**
     * A paraméterrel megegyező indexű elemet eltávolítja a neighbors tömbből, és visszatér az eltávolított csővel.
     * @param idx: Melyik indexű csövet távolítjük el
     * @return
     */
    public Pipe removePipe(int idx) {

        Pipe removedPipe = (Pipe)neighbors.remove(idx);
        return removedPipe;
    }

    /**
     * A Tickable interface által kapott metódus megvalósítása.
     * Minden tick-re meghívja a random nevű attribútum decidePump() metódusát,
     * amely eldönti, hogy elromoljon-e a pumpa. Ez alapján állítja be az isDamaged értékét.
     */
    @Override
    public void tick() {

        boolean decision = this.getRandom().decidePump();
        if(decision) { this.setIsDamaged(true); }
    }

    // getters and setters

    /**
     * From Pipe index setter-e
     * @param index: új érték
     */
    public void setFromPipe(int index) {

        this.fromPipeIdx = index;
    }

    /**
     * To Pipe index setter-e
     * @param index: új érték
     */
    public void setToPipe(int index) {

        this.toPipeIdx = index;
    }

    /**
     * IsDamaged boolean érték setter-e
     * @param value: új érték
     */
    public void setIsDamaged(boolean value) {

        this.isDamaged = value;
    }

    public void setPumpTank(PumpTank pumpTank) {

        this.pumpTank = pumpTank;
    }


}
