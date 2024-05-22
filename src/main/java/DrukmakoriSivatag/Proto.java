package main.java.DrukmakoriSivatag;

import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

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
     * Logger hibák logolására
     */
    Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Szabványos bemenet olvasására használt BufferedReader
     */
    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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
    private final Map<String, FunctionReference> commands = new HashMap<>();

    /**
     * Objektumok név - konstruktor párosait tartalmazó map.
     * A fájl beolvasásakor kell a megfelelő típusú objektumok létrehozásához.
     */
    private final Map<String, Supplier<Object>> constructors = new HashMap<>();

    /**
     * Objektumok név - setup függvény párosait tartalmazó map.
     * A fájl beolvasásakor kell a megfelelő típusú objektumok létrehozásához.
     */
    private final Map<String, BiConsumer<Object, ArrayList<String>>> setups = new HashMap<>();

    /**
     * Konstruktor Mappek inicializálására
     */
    public Proto() {
        commands.put("load", this::load);
        commands.put("save", x -> save());
        commands.put("state", this::state);
        commands.put("endgame", this::endGame);
        commands.put("move", this::move);
        commands.put("damage", this::damage);
        commands.put("repair", this::repair);
        commands.put("pickUp", this::pickUp);
        commands.put("pickUpPipeEnd", this::pickUpPipeEnd);
        commands.put("connectPipeEnd", this::connectPipeEnd);
        commands.put("addPump", this::addPump);
        commands.put("redirect", this::redirect);
        commands.put("makeSlippery", this::makeSlippery);
        commands.put("makeSticky", this::makeSticky);
        commands.put("plumberPoints", x -> plumberPoints());
        commands.put("saboteurPoints", x -> saboteurPoints());
        commands.put("dbgTick", this::dbgTick);
        commands.put("dbgTickAll", x -> dbgTickAll());
        commands.put("dbgCreatePipe", this::dbgCreatePipe);
        commands.put("dbgCreatePump", this::dbgCreatePump);
        commands.put("dbgDamage", this::dbgDamage);
        commands.put("dbgSetLeakable", this::dbgSetLeakable);
        commands.put("exit", this::exit);

        constructors.put("Desert", Desert::new);
        constructors.put("Pipe", Pipe::new);
        constructors.put("Plumber", Plumber::new);
        constructors.put("Pump", Pump::new);
        constructors.put("PumpTank", PumpTank::new);
        constructors.put("Random", Random::new);
        constructors.put("Saboteur", Saboteur::new);
        constructors.put("Timer", Timer::new);
        constructors.put("WaterSource", WaterSource::new);
        constructors.put("WaterTank", WaterTank::new);

        setups.put("Desert", Desert::setup);
        setups.put("Pipe", Pipe::setup);
        setups.put("Plumber", Plumber::setup);
        setups.put("Pump", Pump::setup);
        setups.put("PumpTank", PumpTank::setup);
        setups.put("Saboteur", Saboteur::setup);
        setups.put("WaterSource", WaterSource::setup);
        setups.put("WaterTank", WaterTank::setup);
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
            while(ret !=null && ret.isEmpty()){
                ret = br.readLine();
            }
        } catch (IOException e) {
            try {
                br.close();
            } catch (IOException ignored) {
                logger.info("BufferedReader bezárása sikertelen.");

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
        while (cmd != null && !cmd.get(0).equals("load") && !cmd.get(0).equals("exit")) {
            cmd = parseCommand(readCommand());
        }
        while (cmd != null) {
            try {
                commands.get(cmd.get(0)).runFunction(cmd.size() > 1 ? new ArrayList<>(cmd.subList(1, cmd.size())) : null);
                if(isOver) break;
            } catch (Exception e) {
                System.out.println("Hibás parancs");
            }
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

    private void exit(ArrayList<String> x) {
        try {
            br.close();
        } catch (Exception e){
            logger.info("Reader bezárása sikertelen");
        }
        System.exit(0);
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
        try (BufferedReader bufferedReader = new BufferedReader(
                new FileReader("src/main/resources/DrukmakoriSivatag/" + options.get(0)))) {
            String line;
            String[] cmd;
            while ((line = bufferedReader.readLine()) != null) {
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
                param = new ArrayList<>(Arrays.asList(params.get(i).split(";")));
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
     */
    public void save() {
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
     */
    public void endGame(List<String> params) {
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
     */
    public void plumberPoints() {
        System.out.println("A szerelők pontszáma: " + ((Game) getByName("g")).getFinishedWater() + ".");
    }

    /**
     * Kiírja a szabotőrök aktuális pontszámát.
     */
    public void saboteurPoints() {
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
     */
    public void dbgTickAll() {
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
}
