package main.java.DrukmakoriSivatag;

import java.util.ArrayList;

/**
 * A játék irányításáért felelős osztály. Ebbe beleértjük a játék indítását és az inicializálását, illetve a pontszámok nyilvántartását. Csak egyszer példányosítható, hiszen egyszerre csak egy játékot lehet játszani. Az összes osztály ismeri.
 */
public class Game {
    /**
     * A sivatag, ez minden szabad végű cső szomszédja.
     */
    private Desert desert;
    /**
     * Az időzítő, amely ennek a játénak az ütemezését végzi el.
     */
    private Timer timer = new Timer();
    /**
     * A szerelők pontszáma.
     */
    private int finishedWater;
    /**
     * A szabotőrök pontszáma.
     */
    private int lostWater;
    /**
     * A játékosokat tároló lista.
     */
    private ArrayList<Person> players;

    /**
     * A Game paraméter nélküli konstruktora.
     */
    public Game() {
        desert = new Desert();
        finishedWater = 0;
        lostWater = 0;
        players = new ArrayList<>();
    }

    /**
     * Hozzáad egy új Tickable interfészt megvalósító objektumot a Timer tickables kollekciójához.
     * @param obj az új objektum
     */
    public void addTickable(Tickable obj) {
        timer.addTickable(obj);
    }

    /**
     * Hozzáad egy új játékost a játékosok listájához.
     * @param p az új játékos
     */
    public void addPlayer(Person p) {
        players.add(p);
    }

    /**
     * Visszaadja, hogy a paraméterben kapott játékosnak véget ért-e már a köre.
     * @param p a vizsgálni kívánt játékos
     * @return true, ha a játékosnak nem ért még véget a köre
     */
    public boolean isCurrentPlayer(Person p) {
        if (Main.proto.getIsDebug()) return true;
        return p == players.get(0);
    }

    /**
     * Meghívja a Timer tick() függvényt.
     */
    public void tickAll() {
        timer.tick();
    }

    /**
     * A paraméterként kapott mennyiséget hozzáadja a lostWater attribútumhoz.
     * @param qty a mennyiség, amennyit hozzá szeretnénk adni
     */
    public void addLostWater(int qty) {
        setLostWater(lostWater + qty);
    }

    /**
     * A paraméterként kapott értéket hozzáadja a finishedWater tagváltozóhoz.
     * @param qty a mennyiség, amennyit hozzá szeretnénk adni
     */
    public void addFinishedWater(int qty) {
        setFinishedWater(finishedWater + qty);
    }

    /**
     * A paraméterként kapott értéket levonja a finishedWater tagváltozóból. 0 alá nem csökkentheti.
     * @param qty a mennyiség, amennyit le szeretnénk vonni
     */
    public void removeFinishedWater(int qty) {
        setFinishedWater(Math.max(finishedWater - qty, 0));
    }

    /**
     * Eltávolítja a players tömb első elemét, majd hozzáadja a végére.
     */
    public void rotatePlayers() {
        Person temp = players.get(0);
        players.remove(0);
        players.add(temp);
    }

    /**
     * Getter a Desert attribútumhoz.
     * @return Desert
     */
    public Desert getDesert() {
        return desert;
    }

    /**
     * Setter a Desert attribútumhoz.
     * @param d beállítani kívánt Desert referencia
     */
    public void setDesert(Desert d){
        desert = d;
    }

    /**
     * Setter a finishedWater tagváltozóhoz.
     * @param finishedWater beállítani kívánt érték
     */
    public void setFinishedWater(int finishedWater) {
        this.finishedWater = finishedWater;
    }

    /**
     * setter a lostWater tagváltozóhoz.
     * @param lostWater beállítani kívánt érték
     */
    public void setLostWater(int lostWater) {
        this.lostWater = lostWater;
    }

    /**
     * getter a finishedWater tagváltozóhoz.
     * @return finishedWater
     */
    public int getFinishedWater() {
        return finishedWater;
    }

    /**
     * Getter a lostWater tagváltozóhoz.
     * @return lostWater
     */
    public int getLostWater() {
        return lostWater;
    }
}
