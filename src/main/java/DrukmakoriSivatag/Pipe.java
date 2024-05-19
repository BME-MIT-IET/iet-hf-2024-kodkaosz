package main.java.DrukmakoriSivatag;

import java.util.ArrayList;

/**
 * A csövek megvalósításáért felelős osztály. Eltárolásra kerül a csövek kapacitása, vízszintje, hogy lyukasak-e,
 * illetve, hogy foglalt-e, vagyis, hogy áll-e rajta már játékos. Amennyiben egy szerelő szeretné az egyik
 * végét egy másik pumpához csatlakoztatni, annak a végrehajtása ezen osztályon belül valósul meg.
 * Ugyanez vonatkozik arra is, ha a szabotőr szeretné kilyukasztani. Az egyik szomszédja ismeretében le lehet
 * kérdezni a másikat. Az ősosztályán keresztül örökölt accept metódust felüldefiniálja, hogy eldöntse,
 * hogy léphet-e rá játékos. Ugyanígy a víz továbbítása és elszivárgása is örökölt felelősség.
 */
public class Pipe extends PipelineElement implements Tickable {
    /**
     * A cső kapacitása, ennyi víz fér bele.
     */
    private int capacity;
    /**
     * Az aktuálisan a csőben lévő víz mennyisége.
     */
    private int waterLevel;
    /**
     * Igaz érték esetén a cső lyukas.
     */
    private boolean isDamaged;
    /**
     * Igaz érték esetén a cső csúszós.
     */
    private boolean isSlippery;
    /**
     * Igaz értéke esetén a cső ragadós.
     */
    private boolean isSticky;
    /**
     * Egy counter, 0 értéke esetén a cső kilyukasztható, bármi más esetben nem.
     */
    private int fixedTime;

    /**
     * Fájl input szerint beállítja a kapott objektumot.
     *
     * @param obj    a beállítani kívánt objektum
     * @param params a paramétereket stringként a megfelelő formában tartalmazó lista
     */
    public static void setup(Object obj, ArrayList<String> params) {
        ArrayList<String> pipelineElementParams = new ArrayList<>();
        pipelineElementParams.add(params.get(0));
        pipelineElementParams.add(params.get(1));
        PipelineElement.setup(obj, pipelineElementParams);

        ((Pipe) obj).capacity = Integer.parseInt(params.get(2));
        ((Pipe) obj).waterLevel = Integer.parseInt(params.get(3));
        ((Pipe) obj).isDamaged = (params.get(4)).equals("true");
        ((Pipe) obj).isSlippery = (params.get(5)).equals("true");
        ((Pipe) obj).fixedTime = Integer.parseInt(params.get(6));
        ((Pipe) obj).isSticky = params.get(7).equals("true");

        ((Game) Main.proto.getByName("g")).addTickable((Tickable) obj);
    }

    /**
     * Az objektum állapotát egy megfelelő formátumú stringbe írja.
     *
     * @return az objektum string formában
     */
    @Override
    public String toString() {
        String output = "Pipe " + Main.proto.getByObject(this) + " neighbors:";
        for (int i = 0; i < neighbors.size(); i++) {
            output = output.concat(Main.proto.getByObject(neighbors.get(i)));
            if (i != neighbors.size() - 1)
                output = output.concat(",");
        }

        output = output.concat(";isPickedUp:");
        if (isPickedUp) output = output.concat("true");
        else output = output.concat("false");

        output = output.concat(";capacity:" + capacity);

        output = output.concat(";waterLevel:" + waterLevel);

        output = output.concat(";isDamaged:");
        if (isDamaged) output = output.concat("true");
        else output = output.concat("false");

        output = output.concat(";isSlippery:");
        if (isSlippery) output = output.concat("true");
        else output = output.concat("false");

        output = output.concat(";fixedTime:" + fixedTime);

        output = output.concat(";isSticky:");
        if (isSticky) output = output.concat("true");
        else output = output.concat("false");

        return output;
    }

    /**
     * A Pipe tick implementációja. Hívásakor ha a cső lyukas, meghívódik a Game addLostWater(int qty) függvénye,
     * paraméternek a csőben lévő vízmennyiséget (waterLevel) kell megadni.
     */
    @Override
    public void tick() {
        if (isDamaged) {
            ((Game) Main.proto.getByName("g")).addLostWater(waterLevel);
            waterLevel = 0;
        }

        if (fixedTime > 0) fixedTime--;
    }

    /**
     * Ha a csövön áll egy szabotőr, aki szeretné kilyukasztani a csövet, akkor hívja meg az ő damagePipe()
     * függvénye ezt a metódust. Saját magán meghívja a setIsDamaged(bool d) metódust true paraméterrel,
     * ezzel átállítva az isDamaged logikai változó értékét true-ra.
     */
    public void damageElementSaboteur() {
        this.setIsDamaged(true);
    }

    /**
     * A víz folyásának a megvalósítása. Egy If-Else szerkezetbe van beágyazva a függvény törzse. Az If ág akkor fut
     * le, hogyha lyukas a cső, vagyis, ha az isDamaged értéke true. Ekkor nem befolyik a csőbe a víz, hanem a lyukon
     * keresztül kifolyik belőle. Ilyenkor a Game objektum addLostWater(int qty) függvénye kerül ezesetben meghívásra,
     * paraméterként pedig a qty paraméter és a capacity tagváltozó minimumát kapja meg. Erre azért van szükség, mert
     * nem folyhat ki belőle több víz, mint amennyi van benne. A függvény visszatérési értéke ugyanez a
     * min(qty, capacity) lesz. Az Else ág pedig az az eset, amikor a pumpa sikeresen tud a csőbe vizet pumpálni,
     * mivel az nem lyukas. Elsőként meghívásra kerül a csövön a getOtherNeighbor(PipelineElement element) metódus,
     * az eredeti függvényhívásban kapott from int-tel paraméterként. Ez visszaadja azt a PipelineElement objektumot,
     * amelyik szomszédja a csőnek, de nem egyezik meg a from-mal. Vagyis ez az az elem, amelyikbe kerül a csőből a víz.
     * Ezt követően a setWaterLevel(int qty) függvény segítségével új értéket ad a vízszintnek. A qty az a befolyt víz,
     * a waterlevel meg az eddigi vízszint, így ezeknek az összege kellene, hogy legyen az új vízszint, ez viszont
     * meghaladhatja a kapacitás mértékét. Ezért a setWaterLevel paramétere a kapacitás és az előbb említett összeg
     * minimuma lesz, vagyis min(qty+waterlevel, capacity). Miután a csőbe eljutott a víz, az egyből továbbításra is
     * kerül a túlsó végén lévő PipelineElement objektumba. Ehhez a korábban kapott otherNeighbor-on hívja meg a
     * propagateWater metódust. Az első paramétere a Pipe objektum referenciája, a második pedig a waterLevel. A
     * waterLevelből annyi vizet fogad el az elem, hogy az ő új vízszintje ne haladja meg a kapacitását. Visszatérési
     * értékként megadja, hogy mennyi vizet fogadott el. Erre azért van szükség, mert ennyi víz fogyott el a csőből,
     * tehát ismételten állítani kell a vízszinten, ezúttal a setWaterLevel(waterLevel-waterAccepted) függvényhívással.
     * A legkülső függvény is visszatér, a waterAccepted+waterLevel összeggel.
     *
     * @param from a hívó
     * @param qty  az átadni kívánt vízmennyiség
     * @return a továbbítódótt/kifolyt vízmennyiség
     */
    @Override
    public int propagateWater(PipelineElement from, int qty) {
        if (isDamaged) {
            return Math.min(qty, capacity);
        } else {
            PipelineElement temp = (PipelineElement) this.getOtherNeighbor(from);
            this.setWaterLevel(Math.min(qty + waterLevel, capacity));
            int acceptedWater = temp.propagateWater(this, waterLevel);
            this.setWaterLevel(waterLevel - acceptedWater);
            return waterLevel;
        }
    }

    /**
     * Azt az információt használjuk ki, hogy a csöveknek pontosan két szomszédja van.
     * Megvizsgálja mindkét szomszédot, és azzal tér vissza, amelyik nem egyezik meg a függvényparaméterrel.
     *
     * @param element a hívó szomszéd
     * @return a paraméterben megadottal ellentételes végen lévő szomszéd
     */
    public PipelineElement getOtherNeighbor(PipelineElement element) {
        if (neighbors.size() == 1) return neighbors.get(0);
        return neighbors.get(0) == element ? neighbors.get(1) : neighbors.get(0);
    }

    /**
     * Igaz vagy hamis értékkel tér vissza, attól függően, hogy áll-e valaki a csövön.
     *
     * @return foglalt-e a cső
     */
    public boolean isNavigable() {
        return this.players.isEmpty();
    }

    /**
     * A Plumber osztály addPump() metódusa hívja meg, az ősosztálytól örökölt függvény implementálása.
     * A szomszéd lekérdezése által kapott pump_2 Pump objektumnak meghívja a disconnect(Pipe p) függvényét,
     * az elementtel, mint paraméter. Ez egy logikai változóval tér vissza, hogy sikeres volt-e a művelet vagy sem.
     * Csak akkor fut le a függvény maradék része, ha ez true-val tért vissza. A cső kettévágásához létre
     * kell hozni egy új csövet, amelynek a bemenete a most lerakott pumpa lesz, a kimenete pedig az a pumpa,
     * ami az eddigi cső végére volt csatlakoztatva. Az eredeti vízszint fele kerül mindkét csőbe így.
     * Tehét a pipelineElement létrehoz egy új Pipe-ot, majd ennek, és az elementnek
     * (Vagyis az eddig is meglévő csőnek) meghívja a setWaterLevel(int qty) függvényét paraméterként
     * a már lekérdezett waterLevel (wl a változó neve) felét megadva. Az element neighbor tömbjéből
     * eltávolítja az 1-es indexű elemet, majd a helyére hozzáadja a pickedUpPump változót.
     * Az új csőnek még nincs egy szomszédja se, ezért először hozzáadja a neighbors-hoz a pickedUpPump-ot,
     * majd az eddig is meglévő pumpát. A pickedUpPump-ot pedig null-al kell egyenlővé tenni,
     * hogy ne lehessen ugyanazt a pumpát végtelenszer lerakni.
     *
     * @param p a lerakni kívánt új pumpa
     * @return a művelet sikeressége
     */
    @Override
    public boolean split(Pump p) {
        Pump neighbor0 = (Pump) neighbors.get(0);

        // oldPipe lecsatlakoztatása neighbor0-ról
        if (neighbor0.disconnect(this)) {
            //newPipe létrehozása
            Pipe newPipe = new Pipe();
            Main.proto.addObject("newPipe", newPipe);

            //newPipe szomszédai: p és neighbor0
            p.addNeighbor(newPipe);
            neighbor0.addNeighbor(newPipe);

            //uj pumpa to es from idx
            p.redirect(0, 1);

            //newPipe és oldPipe vízszint beállításai
            newPipe.setWaterLevel(this.capacity / 2);
            this.setWaterLevel(this.capacity / 2);

            //oldPipe szomszédai: neighbor1 (vele nem valtoztattunk semmit) és p
            this.neighbors.remove(0);
            p.addNeighbor(this);

            return true;
        }
        return false;
    }

    /**
     * Ez a függvény azt mondja meg, hogy megadott vízmennyiségből a hívott félen mennyi szivárogna (seep) el
     * a talajba, amiatt, hogy annak szabad a vége. Meghívja a getOtherNeighbor(PipelineElement element)
     * függvényt a from paraméterrel, ezzel megkapja azt a szomszédot, amelyik a másik végén van.
     * Ennek az objektumnak meghívja a seepWater(PipelineElement from, int qty) függvényét,
     * azonban az eredetileg kapott qty paraméter helyett a qty és a capacity tagváltozó minimumát adja át.
     * A metódus visszatér azzal a mennyiséggel amennyi az adott objektumon keresztül el tudott szivárogni
     * a külső függvényhívás szintén ugyanezzel az értékkel tér vissza.
     *
     * @param from az elem, amelyik meghívja a függvényt
     * @param qty  az átadni kívánt vízmennyiség
     */
    @Override
    public int seepWater(PipelineElement from, int qty) {
        return getOtherNeighbor(from).seepWater(from, Math.min(qty, capacity));
    }

    /**
     * Az ősből örökölt függvény, nincs megvalósítva.
     */
    public void disconnect() {
    }

    /**
     * Az ősosztály azonos nevű függvényének a felüldefiniálása.
     * Ha a players tömb nem üres, akkor nem fogadja be, false-al tér vissza, mivel a csövön
     * egy időben csak egy ember állhat. Ellenkező esetben nem áll senki a csövön, szóval
     * befogadja a játékost. Amennyiben befogadja a játékost, akkor hozzáadja a Person
     * objektumot a players tömbhöz, és visszatér true-val, hogy a Person is tudja, hogy
     * beadhatja-e a csövet a setElement(PipelineElement element) függvénynek.
     *
     * @param p a hívó
     * @return a lépés művelet sikeressége
     */
    @Override
    public boolean accept(Person p) {
        if (this.players.isEmpty()) {
            players.add(p);
            return true;
        } else {
            return false;
        }
    }

    /**
     * cső befoltozásának a megvalósítása. Ha az isDamaged változó értéke
     * true, vagyis a cső valóban lyukas, csak akkor hajtódnak végre az objektum állapotát
     * állító események. Ekkor a setIsDamaged által false-ra állítja az isDamaged
     * tagváltozót, a Random osztály decideFixedTime() függvényével pedig eldönti, hogy
     * mennyi ideig nem lehet kilyukasztani a csövet újra. A decideFixedTime egy int-tel tért
     * vissza, ezt átadjuk a setFixedTime függvénynek, ezzel frissítve a fixedTime változó
     * értékét.
     */
    @Override
    public boolean fix() {
        if (isDamaged) {
            setIsDamaged(false);
            setFixedTime(random.decideFixedTime());
            return true;
        }
        return false;
    }

    /**
     * Az ősből örökölt függvény, üres, csak a Pump definiálja.
     */
    public void pickUpPipeEnd() {
    }

    /**
     * Az ősből örökölt függvény, üres, csak a Pump definiálja.
     */
    public void connectPipeEnd() {
    }

    /**
     * Hozzáadja a neighbors tömbjéhez a paraméterben megadott elemet,
     * majd felveszi szomszédnak önmagát a paraméter tömbjébe.
     *
     * @param element az elem, amivel szomszédsági viszonyt szeretnénk kezdeményezni
     */
    @Override
    public void addNeighbor(PipelineElement element) {
        this.neighbors.add(element);
        element.neighbors.add(this);
        //element.addNeighbor(this);
    }

    /**
     * Ha a csövön áll egy játékos, aki szeretné kilyukasztani a csövet, akkor hívja meg az ő damagePipe()
     * függvénye ezt a metódust. Ha a fixedTiem attribútum értéke 0, akkor saját magán meghívja a
     * setIsDamaged(bool d) metódust true paraméterrel, ezzel átállítva az isDamaged logikai változó
     * értékét true-ra. Különben nem történik változás az objektum állapotában.
     */
    @Override
    public boolean damageElement() {
        if (fixedTime == 0) {
            setIsDamaged(true);
            return true;
        }
        return false;
    }

    /**
     * Felüldefiniálja a PipelineElement makeSlippery metódusát.
     * Egy játékos makeSlippery függvénye végrehajtja ezt a metódust, ha épp egy csövön
     * áll. A Pipe objektumnak hívja meg a setIsSlippery függvényét, true paraméterrel.
     */
    @Override
    public boolean makeSlippery() {
        setIsSlippery(true);
        return true;
    }

    /**
     * Felüldefiniálja a PipelineElement makeSticky metódusát. Egy
     * játékos makeStickyfüggvénye végrehajtja ezt a metódust, ha épp egy csövön áll. A
     * Pipe objektumnak hívja meg a setIsSticky függvényét, true paraméterrel.
     */
    @Override
    public boolean makeSticky() {
        setIsSticky(true);
        return true;
    }

    /**
     * Csak ez az osztály implementálja nem üres függvénytörzzsel. Az isSticky változó értékével tér vissza.
     *
     * @return isSticky
     */
    @Override
    public boolean getIsSticky() {
        return isSticky;
    }

    /**
     * Csak ez az osztály implementálja nem üres függvénytörzzsel.
     * A isSlippery változó értékével tér vissza.
     *
     * @return isSlippery
     */
    @Override
    public boolean getIsSlippery() {
        return isSlippery;
    }

    /**
     * Csak ez az osztály implementálja nem üres függvénytörzzsel. A
     * isSticky változó értékékét modósítja.
     */
    @Override
    public void setIsSticky(boolean sticky) {
        isSticky = sticky;
    }

    /**
     * Getter a capacity attribútumhoz.
     *
     * @return capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Getter a waterLevel attribútumohz.
     *
     * @return waterLevel
     */
    public int getWaterLevel() {
        return waterLevel;
    }

    /**
     * Setter a waterLevel attribútumhoz.
     *
     * @param waterLevel beállítani kívánt érték
     */
    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    /**
     * Getter az isDamaged attribútumhoz.
     *
     * @return isDamaged
     */
    public boolean isDamaged() {
        return isDamaged;
    }

    /**
     * Setter az isDamaged attribútumhoz.
     *
     * @param damaged true/false
     */
    public void setIsDamaged(boolean damaged) {
        isDamaged = damaged;
    }

    /**
     * A fixedTime attribútumhoz tartozó setter.
     *
     * @param fixedTime a beállítani kívént érték
     */
    public void setFixedTime(int fixedTime) {
        this.fixedTime = fixedTime;
    }

    /**
     * Setter az isSlippery adattaghoz.
     *
     * @param slippery true / false
     */
    public void setIsSlippery(boolean slippery) {
        isSlippery = slippery;
    }

    /**
     * Setter a capacity adattaghoz.
     *
     * @param capacity a beállítani kívánt érték
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFixedTime() {
        return this.fixedTime;
    }

    public boolean getIsPickedUp() {
        return this.isPickedUp;
    }
}