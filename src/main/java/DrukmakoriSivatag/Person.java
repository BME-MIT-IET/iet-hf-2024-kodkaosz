package main.java.DrukmakoriSivatag;

import java.util.ArrayList;

/**
 * A játékos szabotőr vagy vízvezetékszerelő mivoltától független funkcióit foglalja magába, ennek a
 * két osztálynak a közös őse, absztrakt osztály. Felelős a játékos mozgatásáért és a pumpa elállításáért.
 */
public abstract class Person implements Tickable {
    /**
     * A csőhálózatnak azon eleme, amelyiken a játékos éppen áll.
     * Ennek a megfelelő metódusa kerül meghívásra, ha a játékos valamilyen műveletet hajt végre
     * az elemen.
     */
    protected PipelineElement element;
    /**
     * Azt számlálja, hogy még meddig van az elemre ragadva a játékos.
     */
    protected int stuckCounter;

    /**
     * Az osztály konstruktora.
     */
    protected Person() {
        stuckCounter = 0;
    }

    /**
     * Statikus függvény.
     * A paraméterként kapott objektum állapotát beállítja a megadott ArrayListben szereplő értékeknek megfelelően.
     *
     * @param obj    Az a Person objektum, amelynek az állapotát be szeretnénk állítani.
     * @param params A parancsban megadott paraméterek.
     */
    static void setup(Object obj, ArrayList<String> params) {
        ((Person) obj).setStuckCounter(Integer.parseInt(params.get(0)));
        ((Person) obj).setPipelineElement((PipelineElement) Main.proto.getByName(params.get(1)));
        ((Person) obj).getPipelineElement().accept((Person) obj);
        ((Game) Main.proto.getByName("g")).addPlayer((Person) obj);
        ((Game) Main.proto.getByName("g")).addTickable((Person) obj);
    }

    /**
     * Minden leszármazott implementálja, ezért lehet abstract
     *
     * @return Az objektum aktuális állapotának a leírásával.
     */
    public abstract String toString();

    /**
     * Az element tagváltozóhoz tartozó getter függvény.
     *
     * @return Az elem amin áll a játékos.
     */
    public PipelineElement getPipelineElement() {
        return element;
    }

    /**
     * Az element tagváltozóhoz tartozó setter függvény.
     *
     * @param e Az elem amin áll a játékos.
     */
    public void setPipelineElement(PipelineElement e) {
        element = e;
    }

    /**
     * A stuckCounter tagváltozóhoz tartozó getter függvény.
     *
     * @return Az ragadósság számlálójának értéke.
     */
    public int getStuckCounter() {
        return stuckCounter;
    }

    /**
     * Az stuckCounter tagváltozóhoz tartozó setter függvény.
     *
     * @param s Az ragadósság számlálójának új értéke.
     */
    public void setStuckCounter(int s) {
        stuckCounter = s;
    }

    /**
     * Először
     * lekérdezi az element tagváltozó index paraméterrel megegyező indexű szomszédját. Erre az
     * elemre szeretne lépni a játékos. A függvény törzs maradék része egy if függvénybe van zárva,
     * amelynek a logikai feltétele az előbb kapott szomszéd elem accept(Person p) metódusából
     * visszatérő bool értéke. Ha a következő mező befogadja a játékost, vagyis az accept true-val
     * tér vissza, akkor a remove(Person p) függvénnyel az előző elem eltávolítja a játékost, a
     * Person objektum element attribútumának pedig beállításra kerül a következő mező.
     *
     * @param index Az aktuális elem azon szomszédjának az indexe, ahányadik eleme a neighbors tömbnek a szomszéd amelyre lépni akar.
     */
    public void move(int index) {
        if (stuckCounter == 0) {
            PipelineElement prevElement = element;
            PipelineElement newElement = element.getNeighborElement(index);
            if (newElement.accept(this)) {
                if (newElement.getIsSlippery()) {
                    PipelineElement temp = newElement.getNeighborElement(newElement.getRandom().decideSlipping());
                    newElement.setIsSticky(false);
                    newElement = temp;
                    newElement.accept(this);
                }
                if (newElement.getIsSticky()) {
                    stuckCounter = newElement.getRandom().decideStuckTime();
                }
                setPipelineElement(newElement);
                prevElement.remove(this);
            }
        }
    }

    /**
     * Meghívja az element makeSticky nevű függvényét. Ha egy
     * csövön van játékos, ragadóssá teszi a csövet a következő játékosnak, egyébként nincs
     * hatással az objektum állapotára a függvény.
     */
    public boolean makeSticky() {
        return element.makeSticky();
    }

    /**
     * Meghívja az element redirect nevű
     * metódusát. Csak akkor van hatással a játékra, hogyha az element Pump, mivel csak a többi
     * esetén üres a függvény törzse. Mindkét paraméter esetében megvizsgálja, hogy az ahhoz az
     * indexű elem nem null-e, és csak akkor fut tovább, ha egyik se az, hiszen csak akkor lehet
     * kimenetnek és bemenetnek beállítani. Emellett egy harmadik logikai feltétel, hogy a két cső
     * ne egyezzen meg egymással. Ezután már csak két függvényhívás következik, amelyekkel a
     * ki- és bemenet kerül beállításra: setFromPipe(first), setToPipe(second).
     *
     * @param firstIdx  A bemeneti cső indexe.
     * @param secondIdx A kimeneti cső indexe.
     */
    public boolean redirectPump(int firstIdx, int secondIdx) {
        return element.redirect(firstIdx, secondIdx);
    }

    /**
     * A Tickable interface által kapott metódus megvalósítása. Amennyiben a
     * stuckCounter attribútum értéke meghaladja a nullát, akkor annak az értékét csökkenti
     * eggyel. Ha stuckCounter nulla, akkor pedig az element tagváltozónak meghívja a
     * setIsSticky függvényét false paraméterrel, ezzel beállítva, hogy már nem ragadós a
     * cső.
     */
    @Override
    public void tick() {
        if (stuckCounter > 0) {
            stuckCounter--;
        } else if (stuckCounter == 0) {
            element.setIsSticky(false);
        }
    }

    /**
     * Meghívja az element attribútum damageElementSaboteur() függvényét, ami
     * az element isDamaged logikai tagváltozójának az értékét true-val teszi egyenlővé.
     */
    public boolean damagePipe() {
        return element.damageElement();
    }

    /**
     * Az aktuális elem megjavításáért felelős, csak a Plumber osztály valósítja meg nem üres törzzsel.
     */
    public boolean fix() {
        return false;
    }

    /**
     * Játékosnál lévő pumpa elhelyezése a hálózatban. A Plumber osztály definiálja felül.
     *
     * @return Sikeres volt-e a művelet.
     */
    public boolean addPump() {
        return false;
    }

    /**
     * A cső egyik felvett végének a hálózatba csatlakoztatásáért felelős. A Plumber felüldefiniálja.
     *
     * @return Sikeres volt-e a művelet.
     */
    public boolean connectPipeEnd() {
        return false;
    }

    /**
     * Leszármazotthoz tartozó setter. Plumber valósítja meg.
     */
    public void setPickedUpPump(Pump p) {
    }

    /**
     * Leszármazotthoz tartozó függvény. Plumber valósítja meg.
     *
     * @param idx A felvenni szánt cső indexe.
     * @return Sikeres volt-e a művelet.
     */
    public boolean pickUpPipe(int idx) {
        return false;
    }

    /**
     * Leszármazotthoz tartozó függvény. Plumber valósítja meg.
     *
     * @param idx A felvenni szánt cső indexe.
     * @return Sikeres volt-e a művelet.
     */
    public boolean pickUpPipeEnd(int idx) {
        return false;
    }

    /**
     * Leszármazotthoz tartozó függvény. Plumber valósítja meg.
     *
     * @return Sikeres volt-e a művelet.
     */
    public boolean pickUpPump() {
        return false;
    }

    /**
     * Leszármazotthoz tartozó függvény. Saboteur valósítja meg.
     *
     * @return Sikeres volt-e a művelet.
     */
    public boolean makeSlippery() {
        return false;
    }
}
