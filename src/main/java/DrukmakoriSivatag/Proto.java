package DrukmakoriSivatag;

import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Ez az osztály felelős a program megfelelő működtetéséért.
 * Kezeli a parancsokat, amelyeket bekér a felhasználóktól, és a képernyőre kiíratásokat is ez az osztály végzi el.
 * Eltárolja az összes objektumot, hozzájuk rendelve azt a nevet, amellyel a játékosok hivatkozhatnak rá.
 * A játékmenet köreit is kezeli.
 */
public class Proto {

    /**
     * Parancsok hivatkozásához szükséges interface
     */
    @FunctionalInterface
    private interface FunctionReference {
        /**
         * Futtatja az adott példányhoz tartozó függvényt
         */
        void runFunction(ArrayList<String> options);
    }

    /**
     * Szabványos bemenet olvasására használt BufferedReader
     */
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Azt mondja meg, hogy a játék debug módban-e lett indítva.
     * Bizonyos parancsok csak akkor érhetőek el, ha ennek az értéke true.
     */
    private boolean isDebug = false;

    /**
     * Azt mondja meg, hogy vége van-e az adott játéknak.
     */
    private boolean isOver = false;

    /**
     * Név - objektum párokban tárolja a játék során használt objektumokat.
     * A felhasználók név szerint tudják megadni a parancsoknak az objektumokat, ezért szükséges ilyen módon eltárolni.
     */
    private final Map<String, Object> nameObjectMap = new HashMap<>();

    /**
     * Objektum-név párokban tárolja a játék során használt objektumokat.
     */
    private final Map<Object, String> objectNameMap = new HashMap<>();

    /**
     * A parancsokat tartalmazó map.
     * Megfelelő bemenet esetén innen kerül kiválasztása, hogy melyik fusson le.
     */
    private final Map<String, FunctionReference> commands = new HashMap<String, FunctionReference>() {{
        put("load", x -> load(x));
        put("save", x -> save(x));
        put("state", x -> state(x));
        put("exit", x -> exit(x));
        put("move", x -> move(x));
        put("damage", x -> damage(x));
        put("repair", x -> repair(x));
        put("pickUp", x -> pickUp(x));
        put("pickUpPipeEnd", x -> pickUpPipeEnd(x));
        put("connectPipeEnd", x -> connectPipeEnd(x));
        put("addPump", x -> addPump(x));
        put("redirect", x -> redirect(x));
        put("makeSlippery", x -> makeSlippery(x));
        put("makeSticky", x -> makeSticky(x));
        put("plumberPoints", x -> plumberPoints(x));
        put("saboteurPoints", x -> saboteurPoints(x));
        put("dbgTick", x -> dbgTick(x));
        put("dbgTickAll", x -> dbgTickAll(x));
        put("dbgCreatePipe", x -> dbgCreatePipe(x));
        put("dbgCreatePump", x -> dbgCreatePump(x));
        put("dbgDamage", x -> dbgDamage(x));
        put("dbgSetLeakable", x -> dbgSetLeakable(x));
    }};

    /**
     * Objektumok név - konstruktor párosait tartalmazó map.
     * A fájl beolvasásakor kell a megfelelő típusú objektumok létrehozásához.
     */
    private final Map<String, Supplier<Object>> constructors = new HashMap<String, Supplier<Object>>() {{
        put("Desert", Desert::new);
        put("Pipe", Pipe::new);
        put("Plumber", Plumber::new);
        put("Pump", Pump::new);
        put("PumpTank", PumpTank::new);
        put("Random", Random::new);
        put("Saboteur", Saboteur::new);
        put("Timer", Timer::new);
        put("WaterSource", WaterSource::new);
        put("WaterTank", WaterTank::new);
    }};

    /**
     * Objektumok név - setup függvény párosait tartalmazó map.
     * A fájl beolvasásakor kell a megfelelő típusú objektumok létrehozásához.
     */
    private final Map<String, BiConsumer<Object, ArrayList<String>>> setups = new HashMap<String, BiConsumer<Object, ArrayList<String>>>() {{
        put("Desert", Desert::setup);
        put("Pipe", Pipe::setup);
        put("Plumber", Plumber::setup);
        put("Pump", Pump::setup);
        put("PumpTank", PumpTank::setup);
        put("Saboteur", Saboteur::setup);
        put("WaterSource", WaterSource::setup);
        put("WaterTank", WaterTank::setup);
    }};

    /**
     * Konstruktor
     */
    public Proto() {
    }

    /**
     * Debug módban fut-e a játék
     *
     * @return isDebug értéke
     */
    public boolean getIsDebug() {
        return isDebug;
    }

    /**
     * Hozzáadja az átadott objektumot, a megadott névvel a központi tárolóhoz
     *
     * @param name   objektum neve, amivel hivatkozni lehet rá
     * @param object tárolandó objektum
     */
    public void addObject(String name, Object object) {
        nameObjectMap.put(name, object);
        objectNameMap.put(object, name);
    }

    /**
     * Visszatér a keresett nevű objektummal, ha az létezik
     *
     * @param name keresett objektum neve
     * @return a keresett objektum, vagy null, ha nem található az objektum
     */
    public Object getByName(String name) {
        return nameObjectMap.get(name);
    }

    /**
     * Visszatér a megadott objektum nevével, ha az benne van a tárolóban.
     *
     * @param object az objektum, aminek a nevét keressük
     * @return az objektumm neve, vagy null, ha nem található az objektum
     */
    public String getByObject(Object object) {
        return objectNameMap.get(object);
    }

    /**
     * A szabványos bemenetről beolvas egy parancsot. Egyelőre még nem dolgozza fel, hanem a teljes paranccsal visszatér.
     *
     * @return beolvasott parancs
     */
    private String readCommand() {
        String ret;
        try {
            ret = br.readLine();
        } catch (IOException e) {
            try {
                br.close();
            } catch (IOException ignored) {
            }
            throw new RuntimeException(e);
        }
        return ret != null ? ret.trim() : null;
    }

    /**
     * Létrehoz egy String típusú elemekből álló ArrayList típusú listát.
     * A paraméterként megadott stringet (amely a felhasználó által megadott parancs) szóközök mentén feldarabolja,
     * minden darabot ebbe az ArrayListbe rakja bele, majd visszatér vele.
     *
     * @param cmd feladrabolandó parancs string
     * @return lista a feldarabolt bemenettel
     */
    private ArrayList<String> parseCommand(String cmd) {
        return cmd != null ? new ArrayList<>(Arrays.asList(cmd.split(" "))) : null;
    }

    /**
     * Fájl vége jelig, illetve exit parancsig olvassa a parancsokat és hajtja azokat végre.
     */
    public void PlayGame() {
        reset();
        ArrayList<String> cmd = parseCommand(readCommand());
        while (cmd != null && !cmd.get(0).equals("load")) {
            cmd = parseCommand(readCommand());
        }
        while (cmd != null && !isOver) {
            commands.get(cmd.get(0)).runFunction(cmd.size() > 1 ? new ArrayList<String>(cmd.subList(1, cmd.size())) : null);
            cmd = parseCommand(readCommand());
        }
        if (cmd == null) {
            System.exit(0);
        }
    }

    /**
     * Alaphelyzetbe állítja az objektumot, előkészül egy újabb teszteset futtatására
     */
    public void reset() {
        nameObjectMap.clear();
        objectNameMap.clear();
        isOver = false;
    }


    // parancsokat megvalósító függvények

    /**
     * Beolvassa a megadott fájlt.
     *
     * @param options beolvasandó fájl, futtatás módja (debug/normál)
     */
    public void load(ArrayList<String> options) {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        ArrayList<String> params = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(options.get(0)))) {
            String line;
            String[] cmd;
            while ((line = br.readLine()) != null) {
                cmd = line.trim().split(" ");
                names.add(cmd[1]);
                types.add(cmd[0]);
                params.add(cmd.length > 2 ? cmd[2] : null);
            }
        } catch (IOException e) {
            System.out.println("Pálya betöltése sikertelen.");
            return;
        }
        reset();
        Game game = new Game();
        addObject("g", game);
        // default konstruktorral hozza létre, és tárolja el az objektumokat
        for (int i = 0; i < names.size(); ++i) {
            Object obj = constructors.get(types.get(i)).get();
            addObject(names.get(i), obj);
        }
        // a létrehozott objektumoknak beállítja a paramétereit
        for (int i = 0; i < names.size(); ++i) {
            BiConsumer<Object, ArrayList<String>> setup = setups.get(types.get(i));
            ArrayList<String> param = null;
            if (params.get(i) != null) {
                param = new ArrayList<String>(Arrays.asList(params.get(i).split(";")));
                for (int j = param.size(); j > 0; --j) {
                    param.add(param.get(0).split(":")[1]);
                    param.remove(0);
                }
            }
            setup.accept(getByName(names.get(i)), param);
        }
        isDebug = options.get(1).equalsIgnoreCase("debug");
        System.out.println("Pálya betöltése sikeres.");
    }

    /**
     * Kiírja minden objektum aktuális állapotát a kimenetre
     *
     * @param options jelen esetben nem foglalkozunk vele, az interface miatt kell
     */
    public void save(ArrayList<String> options) {
        for (Object o : nameObjectMap.values()) {
            if (!getByObject(o).equals("g")) {   // a Game adatait nem írjuk ki
                System.out.println(o.toString());
            }
        }
    }

    /**
     * Kiírja a megadott objektum aktuális állapotát a kimenetre
     *
     * @param options kiírandó objektum neve
     */
    public void state(ArrayList<String> options) {
        System.out.println(nameObjectMap.get(options.get(0)));
    }

    /**
     * Kilép az aktuális játékból
     *
     * @param options jelen esetben nem foglalkozunk vele, az interface miatt kell
     */
    public void exit(ArrayList<String> options) {
        isOver = true;
        System.out.println("Játék megszakítva.");
    }

    /**
     * Megadott játékossal megpróbálunk lépni a megadott irányba.
     *
     * @param options léptetendő játékos, léptetés iránya
     */
    public void move(ArrayList<String> options) {
        Person player = (Person) getByName(options.get(0));
        if (((Game) getByName("g")).isCurrentPlayer(player)) {
            player.move(Integer.parseInt(options.get(1)));
            System.out.println("Új pozíció: " + getByObject(player.getPipelineElement()) + ".");
        } else {
            System.out.println("A játékos körének vége.");
        }
    }

    /**
     * A megadott játékos megpróbálja kilyukasztani az elemet, amin áll. Ha nem csövön áll, nem történik semmi.
     *
     * @param options lyukasztani próbáló játékos
     */
    public void damage(ArrayList<String> options) {
        Person player = (Person) getByName(options.get(0));
        Game game = (Game) getByName("g");
        if (game.isCurrentPlayer(player)) {
            System.out.println("Rongálás " + (player.damagePipe() ? "sikeres." : "sikertelen."));
            game.rotatePlayers();
        } else {
            System.out.println("A játékos körének vége.");
        }
    }

    /**
     * A megadott játékos megpróbálja megjavítani az elemet, amin áll.
     *
     * @param options javító játékos
     */
    public void repair(ArrayList<String> options) {
        Person player = (Person) getByName(options.get(0));
        Game game = (Game) getByName("g");
        if (game.isCurrentPlayer(player)) {
            System.out.println("Javítás " + (player.fix() ? "sikeres." : "sikertelen."));
            game.rotatePlayers();
        } else {
            System.out.println("A játékos körének vége.");
        }
    }

    /**
     * Az adott játékos megpróbál felvenni megadott típusú elemet.
     *
     * @param options felvevendő elem típusa, játékos
     */
    public void pickUp(ArrayList<String> options) {
        Person player = (Person) getByName(options.get(1));
        Game game = (Game) getByName("g");
        if (game.isCurrentPlayer(player)) {
            boolean success;
            if (options.get(0).equalsIgnoreCase("pipe")) {
                success = player.pickUpPipe(0);
            } else {
                success = player.pickUpPump();
            }
            System.out.println("Elem felvétele " + (success ? "megtörtént." : "sikertelen."));
            game.rotatePlayers();
        } else {
            System.out.println("A játékos körének vége.");
        }
    }

    /**
     * Az adott játékos megpróbálja felvenni az adott indexű csővéget.
     *
     * @param options játékos, felvevendő csővég indexe
     */
    public void pickUpPipeEnd(ArrayList<String> options) {
        Person player = (Person) getByName(options.get(0));
        Game game = (Game) getByName("g");
        if (game.isCurrentPlayer(player)) {
            System.out.println("Csővég felvétele " + (player.pickUpPipeEnd(Integer.parseInt(options.get(1))) ? "sikeres." : "sikertelen."));
            game.rotatePlayers();
        } else {
            System.out.println("A játékos körének vége.");
        }
    }

    /**
     * A megadott játékos csatlakoztatja (valamelyik) nála lévő csővéget az elemhez, amin áll, ha az engedi.
     *
     * @param options kísérletet végző játékos
     */
    public void connectPipeEnd(ArrayList<String> options) {
        Person player = (Person) getByName(options.get(0));
        Game game = (Game) getByName("g");
        if (game.isCurrentPlayer(player)) {
            System.out.println("Csatlakoztatás " + (player.connectPipeEnd() ? "sikeres." : "sikertelen."));
            game.rotatePlayers();
        } else {
            System.out.println("A játékos körének vége.");
        }
    }

    /**
     * A megadott játékos megpróbálja lerakni a nála lévő pumpát (már ha van nála) az elemre, amin áll.
     *
     * @param options játékos aki megpróbálja lerakni a pumpát
     */
    public void addPump(ArrayList<String> options) {
        Person player = (Person) getByName(options.get(0));
        Game game = (Game) getByName("g");
        if (game.isCurrentPlayer(player)) {
            System.out.println("Pumpa lerakása " + (player.addPump() ? "sikeres." : "sikertelen."));
            game.rotatePlayers();
        } else {
            System.out.println("A játékos körének vége.");
        }

    }

    /**
     * A megadott játékos megpróbálja átirányítani a pumpát, amin áll, ha valóban pumpán áll.
     *
     * @param options player, from, to
     */
    public void redirect(ArrayList<String> options) {
        Person player = (Person) getByName(options.get(0));
        Game game = (Game) getByName("g");
        PipelineElement element = player.getPipelineElement();
        int from = element.getIndex((PipelineElement) getByName(options.get(1)));
        int to = element.getIndex((PipelineElement) getByName(options.get(2)));
        if (game.isCurrentPlayer(player)) {
            System.out.println("Átirányítás " + (player.redirectPump(from, to) ? "sikeres." : "sikertelen."));
            game.rotatePlayers();
        } else {
            System.out.println("A játékos körének vége.");
        }
    }

    /**
     * A megadott játékos megpróbálja csúszóssá tenni az elemet, amin áll.
     *
     * @param options a kísérletet végző játékos
     */
    public void makeSlippery(ArrayList<String> options) {
        Person player = (Person) getByName(options.get(0));
        Game game = (Game) getByName("g");
        if (game.isCurrentPlayer(player)) {
            System.out.println("Csúszóssá tevés  " + (player.makeSlippery() ? "sikeres." : "sikertelen."));
            game.rotatePlayers();
        } else {
            System.out.println("A játékos körének vége.");
        }
    }

    /**
     * A megadott játékos megpróbálja ragadóssá tenni az elemet, amin áll.
     *
     * @param options a kísérletet végző játékos
     */
    public void makeSticky(ArrayList<String> options) {
        Person player = (Person) getByName(options.get(0));
        Game game = (Game) getByName("g");
        if (game.isCurrentPlayer(player)) {
            System.out.println("Ragadóssá tevés " + (player.makeSticky() ? "sikeres." : "sikertelen."));
            game.rotatePlayers();
        } else {
            System.out.println("A játékos körének vége.");
        }
    }

    /**
     * Kiírja a szerelők aktuális pontszámát.
     *
     * @param options jelen esetben nem foglalkozunk vele, az interface miatt kell
     */
    public void plumberPoints(ArrayList<String> options) {
        System.out.println("A szerelők pontszáma: " + ((Game) getByName("g")).getFinishedWater() + ".");
    }

    /**
     * Kiírja a szabotőrök aktuális pontszámát.
     *
     * @param options jelen esetben nem foglalkozunk vele, az interface miatt kell
     */
    public void saboteurPoints(ArrayList<String> options) {
        System.out.println("A szabotőrök pontszáma: " + ((Game) getByName("g")).getLostWater() + ".");
    }

    /**
     * A megadott objektumon meghívja a tick() függvényt.
     *
     * @param options jelen esetben nem foglalkozunk vele, az interface miatt kell
     */
    public void dbgTick(ArrayList<String> options) {
        ((Tickable) getByName(options.get(0))).tick();
        System.out.println("Tick.");
    }

    /**
     * Minden Tickable interfészt megvalósító objektumon meghívja a tick() függvényt.
     *
     * @param options jelen esetben nem foglalkozunk vele, az interface miatt kell
     */
    public void dbgTickAll(ArrayList<String> options) {
        ((Game) getByName("g")).tickAll();
        System.out.println("TickAll.");
    }

    /**
     * Új csövet hoz létre a megadott ciszternán.
     *
     * @param options ciszterna neve, amin létre akarjuk hozni a csövet
     */
    public void dbgCreatePipe(ArrayList<String> options) {
        ((WaterTank) getByName(options.get(0))).createPipe();
        System.out.println("Cső létrehozva.");
    }

    /**
     * Új pumpát hoz létre a megadott ciszternán.
     *
     * @param options ciszterna neve, amin a pumpát akarjuk létrehozni
     */
    public void dbgCreatePump(ArrayList<String> options) {
        ((WaterTank) getByName(options.get(0))).createPump();
        System.out.println("Pumpa létrehozva.");
    }

    /**
     * Elrontja a megadott elemet.
     * Abban különbözik a damage utasítástól, hogy itt akkor is elronthatunk egy elemet,
     * ha nem áll rajta senki, illetve, ha az adott elem Pumpa.
     *
     * @param options elrontandó elem típusa, neve
     */
    public void dbgDamage(ArrayList<String> options) {
        Object element = getByName(options.get(1));
        if (options.get(0).equalsIgnoreCase("Pipe")) {
            ((Pipe) element).setIsDamaged(true);
        } else if (options.get(0).equalsIgnoreCase("Pump")) {
            ((Pump) element).setIsDamaged(true);
        }
        System.out.println("Elem elromolva.");
    }

    /**
     * Beállítja, hogy az adott cső meddig ne lehessen kilyukasztható. 0 esetén azonnal kilyukaszthatóvá válik a cső.
     *
     * @param options állítandó cső, meddig legyen kilyukaszthatatlan a cső
     */
    public void dbgSetLeakable(ArrayList<String> options) { //setFixedTime() a fgv, ha ugyanarra gondolunk
        ((Pipe) getByName(options.get(0))).setFixedTime(Integer.parseInt(options.get(1)));
        System.out.println("A cső nem lyukasztható ki ennyi ideig: " + options.get(1) + ".");
    }

    /**
     * Bezárja az inputstreamet, ha meghívódik a destruktor
     *
     * @throws IOException ??
     */
    @Override
    protected void finalize() throws IOException {
        br.close();
    }
}
