import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Ez az osztály olvassa be a pályaleíró fájlokat és tárolja a pályán szereplő objektumokat.
 */
public class Proto {

    /**
     * Azt mondja meg, hogy a játék debug módban-e lett indítva.
     * Bizonyos parancsok csak akkor érhetőek el, ha ennek az értéke true.
     */
    private boolean isDebug = false;


    /**
     * Név - objektum párokban tárolja a játék során használt objektumokat.
     * A felhasználók név szerint tudják megadni a parancsoknak az objektumokat, ezért szükséges ilyen módon eltárolni.
     */
    private Map<String, Object> nameObjectMap = new HashMap<>();

    /**
     * Objektum-név párokban tárolja a játék során használt objektumokat.
     */
    private Map<Object, String> objectNameMap = new HashMap<>();
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
     * Fájl vége jelig, illetve exit parancsig olvassa a parancsokat és hajtja azokat végre.
     */
    public void playGame() {
        reset();
    }

    /**
     * Alaphelyzetbe állítja az objektumot, előkészül egy újabb teszteset futtatására
     */
    public void reset() {
        nameObjectMap.clear();
        objectNameMap.clear();
    }


    /**
     * Beolvassa a megadott fájlt.
     *
     * @param path beolvasandó fájl útvonala
     * @return sikeres-e a beolvasás
     */
    public boolean load(String path) {
        reset();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        ArrayList<String> params = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            String[] cmd;
            while ((line = br.readLine()) != null) {
                cmd = line.trim().split(" ");
                names.add(cmd[1]);
                types.add(cmd[0]);
                params.add(cmd.length > 2 ? cmd[2] : null);
            }
        } catch (IOException e) {
            return false;
        }
        MainWindow.getInstance().clearCanvas();
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
        return true;
    }

}
