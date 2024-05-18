package main.java.DrukmakoriSivatag;

import java.util.ArrayList;

/**
 *Közös őse a hálózatot alkotó elemeknek, vagyis a ciszternáknak, forrásoknak, a sivatagnak, a
 * csöveknek és a pumpáknak. Absztrakt osztály. Minden közös funkció ezen az osztályon belül
 * van megvalósítva. Ez az osztály tud elfogadni és elutasítani mezőre lépni próbálkozó játékost.
 * Felelőssége még eltávolítani játékost a mezőről, ha az ellép onnan. Ismeri, hogy milyen
 * elemekkel áll szomszédságban, le is lehet kérdezni adott elemről, hogy a szomszédja-e, hozzá
 * lehet adni új szomszédot, és azonosító alapján megkapni már meglévőt. Amikor a játékos
 * átállítja a pumpát, megrongálni a csövet, vagy a cső végét akarja másik csőre átkötni, akkor is
 * ennek az osztálynak hívja meg a megfelelő metódusait, amelyeket a Pipe és a Pump osztály
 * felüldefiniál. Eltárolja azt is, hogy cső esetében fel van-e véve az elem.
 */
public abstract class PipelineElement {
    /**
     * Az elem szomszédjainak a halmaza.
     */
    protected ArrayList<PipelineElement> neighbors;
    /**
     * A játékosok akik rajta állnak az elemen. Az aktív elemek miatt
     * tömbnek kell lennie, hiszen azon több játékos is tartózkodhat egyszerre.
     */
    protected ArrayList<Person> players;
    /**
     * Ennek a segítségével valósítja meg a pumpák egymástól független
     * véletlen időközönkénti elromlását, illetve az új csövek létrehozását.
     */
    protected Random random;

    /**
     * Megmutatja, hogy az elem fel van-e véve
     */
    protected boolean isPickedUp;

    /**
     * A PipelineElement osztály paraméternélküli konstruktora.
     */
    public PipelineElement(){
        neighbors= new ArrayList<>();
        players=new ArrayList<>();
        random= new Random();
        isPickedUp=false;
    }

    /**
     * Beállítja a paraméterként kapott String tömb értékei alapján a tagváltozóit
     * @param params String tömb, a tagváltozók értékével
     */
    static void setup(Object obj, ArrayList<String> params){
        String[] newneighbors = new String[params.get(0).split(",").length];
        newneighbors=params.get(0).split(",");
        if(!newneighbors[0].equals("null")) {
            for (int i = 0; i < newneighbors.length; i++) {
                ((PipelineElement) obj).neighbors.add((PipelineElement) Main.proto.getByName(newneighbors[i]));
            }
        }
        ((PipelineElement)obj).isPickedUp=Boolean.parseBoolean(params.get(1));

    }

    /**
     * toString metódus felüldefiniálása, minden leszármazott implementálja, itt üres függvény
     * @return null
     */
    @Override
    public String toString() {
        return null;
    }

    /**
     * Eltávolítja a paraméterként kapott Person objektumot a players
     * tömb elemei közül.
     * @param p: az eltávolítandó játékos
     */
    public void remove(Person p){
        players.remove(p);
    }

    /**
     * Csak a cső és a pumpa valósítja meg, mivel a többi leszármazott nem tud
     * elromolni/kilyukadni, ezért megjavítani se lehet őket.
     * @return sikeres volt-e a művelet
     */
    public boolean fix(){
        return false;
    }

    /**
     * Végigiterál a neighbors tömbön, és megkeresi,
     * hogy a paraméterként kapott PipelineElement-et tartalmazza-e a tömb.
     * Ha igen, akkor trueval tér vissza, különben a visszatérési érték false.
     * @param element: a keresett elem
     * @return true, ha szomszédja, false, ha nem
     */
    public boolean isNeighbor(PipelineElement element){
        for(int i=0; i<neighbors.size();i++){
            if(neighbors.get(i)==element){
                return true;
            }
        }
        return false;
    }

    /**
     * Megkeresi a neighbors tömbnek a
     * paraméterrel megegyező indexű elemét, és visszatér vele.
     * @param idx: keresett index
     * @return a keresett indexű szomszéd
     */
    public PipelineElement getNeighborElement(int idx){
        if(neighbors.size()>idx){
            return neighbors.get(idx);
        }
        return null;
    }

    /**
     * A szomszédokból álló tömbbe (neighbors) felveszi
     * a paraméterként kapott PipelineElement-et.
     * @param newElement: az új szomszéd
     */
    public  void addNeighbor(PipelineElement newElement){
        for(int i=0; i<neighbors.size();i++){
            if(neighbors.get(i)==newElement){
                return;
            }
        }
        neighbors.add(newElement);
    }

    /**
     * A Person osztály redirectPump metódusa hívja
     * meg, továbbadva azokat a paramétereket amiket kapott.
     * @param firstIdx: Melyik indexű szomszéd irányából follyon a víz
     * @param secondIdx: Melyik indexű szomszéd irányába follyon a víz
     * @return sikeres volt-e a művelet
     */
    public boolean redirect(int firstIdx, int secondIdx){
        return false;
    }


    /**
     * A paraméterként kapott indexű elemet
     * eltávolítja a szomszédokat tároló tömbből.
     * @param element: az eltávolítandó elem
     */
    public void removeNeighbor(PipelineElement element){
        neighbors.remove(element);
    }

    /**
     *A víz mozgatását megvalósító
     * függvény. Nem tartozik hozzá megvalósítás, a leszármazottak implementálják.
     * @param from: a hívó
     * @param qty: az átadni kívánt vízmennyiség
     * @return a továbbítódótt/kifolyt vízmennyiség
     */
    public int propagateWater(PipelineElement from, int qty){
        return 0;
    }

    /**
     * A Person osztály damagePipe() metódusa hívja meg azon a
     * PipelineElement objektumon, amelyiken éppen áll. Ebben az osztályban nem tartozik
     * hozzá implementáció. A játékosok csak csöveket tudnak kilyukasztani, így csak a Pipe
     * osztály valósítja meg nem üres függvényként.
     * @return sikeres volt-e a művelet
     */
    public boolean damageElement(){
        return false;
    }

    /**
     * A szivárgást valósítja meg. A
     * leszármazottak implementálják.
     * @param from: az elem, amelyik meghívja a függvényt
     * @param qty: az átadni kívánt vízmennyiség
     */
    public int seepWater(PipelineElement from, int qty) {
        return 0;
    }

    /**
     * A leszármazottak valósítják meg. Azt dönti el, hogy befogadja-e az
     * elemre lépni szándékozó játékost.
     * @param p: elemre lépni szándékozó játékos
     * @return befogadta-e a játékost
     */
    public boolean accept(Person p){
        return true;
    }

    /**
     * A timer azonos nevű metódusa hívja meg adott időközönként. A leszármazottak
     * felüldefiniálják.
     */
    public void tick(){
    }

    /**
     * A ciszternánál új cső felvételét valósítja meg. A
     * leszármazottak közül csak a WaterTank implementálja, hiszen a többi elemnél nem
     * lehetséges ilyen művelet végrehajtása. Visszatér a felvett elemmel.
     * @param p: a hívó játékos
     * @return : null, kivéve a WaterTanknél, ami felüldefiniálja
     */
    public Pump pickUpPump(Person p){
        return null;
    }

    /**
     * A cső lecsatlakoztatásának és elvitelének a megvalósítása. A
     * leszármazottak közül csakis kizárólag a Pump osztály implementálja nem üres
     * függvénytörzzsel. A Plumber osztály azonos nevű metódusa hívja meg. Visszatér a felvett
     * elemmel.
     * @param i: Hányadik csövet szeretné lecsatlakoztatni
     * @return null, kivéve a Pumpnál, ami felüldefiniálja
     */
    public Pipe pickUpPipeEnd(int i){
        return null;
    }

    /**
     * A teljes cső lecsatlakoztatásának és elvitelének a megvalósítása. A
     * leszármazottak közül csakis kizárólag a Pump osztály implementálja nem üres
     * függvénytörzzsel. A Plumber osztály azonos nevű metódusa hívja meg. Visszatér a felvett
     * elemmel.
     * @param i: Hányadik csövet szeretné lecsatlakoztatni
     * @return null, kivéve a Pumpnál, ami felüldefiniálja
     */
    public Pipe pickUpPipe(int i){
        return null;
    }

    /**
     *  Új pumpa lerakásához szükséges. A Pipe az egyetlen osztály, amelyik
     * megvalósítja, a többi leszármazott esetében üres a függvény törzse.
     * @return false, kivéve a Pipenál, ami felüldefiniálja
     */
    public boolean split(Pump p){
        return false;
    }

    /**
     * Pumpa esetén lecsatlakoztatja az adott elemről a paraméterként
     * átadott csövet. Ha ez sikeres volt akkor true-val tér vissza, egyébként false-al.
     * @param p: amelyik csövet szeretné a játékos lecsatlakoztatni
     * @return :false, kivéve a Pumpnál, ami felüldefiniálja
     */
    public boolean disconnect(Pipe p){
        return false;
    }

    /**
     * A cső egyik felvett végének a hálózatba
     * csatlakoztatásáért felelős. Csak a Pump valósítja meg ténylegesen, mivel csak arra lehet
     * csatlakoztatni csövet. A többi leszármazott osztály esetén üres a törzse.
     * @param pickedUpPipe: a játékos kezében lévő cső
     * @return :false, kivéve a Pumpnál, ami felüldefiniálja
     */
    public boolean connectPipeEnd(Pipe pickedUpPipe){
        return false;
    }

    /**
     * A PipelineElement random tagváltozójának settere
     * @param r az új random
     */
    public void setRandom(Random r){
        random=r;
    }

    /**
     * A PipelineElement random tagváltozójának gettere
     * @return random
     */
    public Random getRandom(){
        return this.random;
    }

    /**
     * Csak a cső osztály implementálja nem üres függvénytörzzsel.
     * A cső állapotát átírja csúszósra.
     * @return sikeres volt-e a művelet
     */
    public boolean makeSlippery(){
        return false;
    }

    /**
     * Csak a cső osztály implementálja nem üres függvénytörzzsel. A
     * cső állapotát átírja ragadósra.
     * @return sikeres volt-e a művelet
     */
    public boolean makeSticky(){
        return false;
    }

    /**
     * Csak a cső osztály implementálja nem üres függvénytörzzsel. A
     * cső isSticky változó értékével tér vissza.
     * @return isSticky változó értéke
     */
    public boolean getIsSticky(){
        return false;
    }

    /**
     * Csak a cső osztály implementálja nem üres függvénytörzzsel.
     * A cső isSlippery változó értékével tér vissza.
     * @return isSlippery változó értéke
     */
    public boolean getIsSlippery(){
        return false;
    }

    /**
     * Csak a cső osztály implementálja nem üres függvénytörzzsel. A
     * cső isSticky változó értékékét modósítja.
     */
    void setIsSticky(boolean sticky){
    }

    public int getIndex(PipelineElement pipe) {

        for(int i = 0; i < neighbors.size(); ++i) {

            PipelineElement element = neighbors.get(i);
            if(pipe.equals(element)) { return i; }
        }

        return -1;
    }


    /**
     * Setter az isPickedUp tagváltozóhoz.
     * @param pickedUp true/false
     */
    public void setIsPickedUp(boolean pickedUp) {
        isPickedUp = pickedUp;
    }


}

