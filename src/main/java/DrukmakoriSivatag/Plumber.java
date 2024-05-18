package main.java.DrukmakoriSivatag;

import java.util.ArrayList;
import java.util.Arrays;

public class Plumber extends Person{
    /**
     * A felvett pumpa.
     */
    public Pump pickedUpPump;
    /**
     * A felvett csővég.
     */
    public Pipe pickedUpPipe;

    /**
     *
     * Statikus függvény.
     * A paraméterként kapott objektum állapotát beállítja a megadott ArrayListben szereplő értékeknek megfelelően.
     *
     * @param obj Az a Person objektum, amelynek az állapotát be szeretnénk állítani.
     * @param params A parancsban megadott paraméterek.
     */

    static void setup(Object obj, ArrayList<String> params){
        String[] parentParams = new String[2];
        parentParams[0] = params.get(0);
        parentParams[1] = params.get(3);

        Person.setup(obj, new ArrayList<>(Arrays.asList(parentParams)));

        ((Plumber)obj).setPickedUpPipe((Pipe)Main.proto.getByName(params.get(1)));
        ((Plumber)obj).setPickedUpPump((Pump)Main.proto.getByName(params.get(2)));
    }

    /**
     * Visszatér az objektum aktuális állapotának a leírásával.
     * @return Az objektum állapota egy Stringbe leírva.
     */
    @Override
    public String toString(){
        String output = "Plumber " + Main.proto.getByObject(this) + " stuckCounter:"+ stuckCounter
                + ";pickedUpPipe:" + Main.proto.getByObject(pickedUpPipe)
                + ";pickedUpPump:" + Main.proto.getByObject(pickedUpPump)
                + ";element:" + Main.proto.getByObject(element);
        return output;
    }

    /**
     * A felvett pumpához tartozó getter függvény.
     * @return A felvett pumpa.
     */
    public Pump getPickedUpPump(){
        return pickedUpPump;

    }
    /**
     * A felvett pumpához tartozó setter függvény.
     * @param p A felvett pumpa.
     */
    @Override
    public void setPickedUpPump(Pump p){
        pickedUpPump = p;
    }
    /**
     * A felvett csővéghez tartozó getter függvény.
     * @return A felvett csővég.
     */
    public Pipe getPickedUpPipe(){
        return pickedUpPipe;

    }

    /**
     * A felvett csővéghez tartozó setter függvény.
     * @param p A felvett csővég.
     */
    public void setPickedUpPipe(Pipe p){
        pickedUpPipe = p;
    }

    /**
     * Megjavítja az elemet amin épp áll. Ehhez meghívja a Person osztályból örökölt
     * element adattag fix() függvényét, ami abban az esetben, ha az element Pump vagy Pipe,
     * akkor az isDamaged bool típusú tagváltozóját false-ra állítja.
     */
    @Override
    public boolean fix(){
        return element.fix();
    }

    /**
     * Ezzel a függvénnyel vesz fel egy lerakható pumpát a ciszternától a
     * játékos. Csak akkor kerül végrehajtásra a függvénytörzs, hogyha a pickedUpPump attribútum
     * null, vagyis nincs nála pumpa. Ekkor meghívja az element tagváltozó azonos nevű
     * függvényét. Csak akkor fog történni bármi, hogyha WaterTank elemen áll, mivel egyedül az
     * valósítja meg ezt a függvényt. Ez a metódus létrehoz egy új pumpát, majd ezt az újonnan
     * létrejött Pump-ot átadja a Plumber setPickedUpPump(Pump p) metódusának, így most a
     * szerelő birtokában van már a pumpa.
     *
     * @return Sikeres volt-e a művelet.
     *
     */
    @Override
    public boolean pickUpPump(){
        boolean success = false;
        if(pickedUpPump==null) {
            Pump temp = element.pickUpPump(this);
            setPickedUpPump(temp);
            success = true;
        }
        return success;
    }

    /**
     * A metódus törzsének teljes egésze egy If függvényben helyezkedik el. Ez
     * az If azt ellenőrzi, hogy a pickedUpPump attribútum referenciája nem null-e. Ha null, akkor a
     * játékosnál nincs pumpa, szóval nincs mit leraknia és nem történik semmi. Ellenkező esetben
     * meghívja az element attribútum Split(Pump p) metódusát. Ezt csak a Pipe implementálja nem
     * üres függvénytörzzsel, ezért, ha más fajta elemen áll a játékos, akkor nem tud lerakni pumpát.
     *
     * @return  Sikeres volt-e a művelet.
     */
    @Override
    public boolean addPump(){
        boolean success = false;
        if(pickedUpPump!=null) {
            success = element.split(pickedUpPump);
            if(success) {
                pickedUpPump.setIsPickedUp(false);
                setPickedUpPump(null);
            }
        }
        return success;
    }

    /**
     *
     * Az új pumpa felvételéhez hasonlóan itt is megvizsgálja, hogy
     * nincs-e lecsatlakoztatott végű cső már a játékosnál. Ha nincs nála, akkor meghívja az element
     * tagváltozó azonos nevű függvényét. Ezt csak a pumpa implementálja, hiszen csak arról lehet
     * csövet lecsatlakoztatni. Ez a függvény lekérdezi, hogy a szomszédokat tartalmazó i indexű
     * eleme melyik cső. Ezután az element disconnect függvényének ezt átadja paraméterként,
     * majd egy logikai változó formájában visszakapva azt, hogy sikeresen le lett-e csatlakozva. A
     * függvénytörzs maradék része ennek a bool-nak csak true értéke esetén kerül végrehajtásra. Itt
     * már csak annyi történik, hogy a Plumber objektum pickedUpPipe attribútumát egyenlővé
     * teszi azzal a csővel, amit lecsatolt.
     *
     * @param idx   A lecsatlakoztatni kívánt cső indexe.
     *@return Sikeres volt-e a művelet.
     *
     */
    @Override
    public boolean pickUpPipeEnd(int idx){
        boolean success = false;
        if(pickedUpPipe==null){
            setPickedUpPipe(element.pickUpPipeEnd(idx));
            if(pickedUpPipe != null){
                success = true;
            }
        }
        return success;
    }

    /**
     *
     * Egy teljes cső lecsatlakoztatásáért felelős. Az új pumpa felvételéhez hasonlóan itt is megvizsgálja,
     * hogy nincs-e lecsatlakoztatott végű cső már a játékosnál. Ha nincs nála, akkor
     * meghívja az element tagváltozó azonos nevű függvényét. Ezt csak a pumpa
     * implementálja, hiszen csak arról lehet csövet lecsatlakoztatni teljesen.
     *
     * @param idx   A lecsatlakoztatni kívánt cső indexe.
     * @return Sikeres volt-e a művelet.
     */
    @Override
    public boolean pickUpPipe(int idx){
        boolean success = false;
        if(pickedUpPipe==null){
            setPickedUpPipe(element.pickUpPipe(idx));
            if(pickedUpPipe != null){
                success = true;
            }
        }
        return success;
    }

    /**
     * A cső egyik felvett végének a hálózatba csatlakoztatásáért felelős.
     * Azonos logikával működik, mint az addPump függvény. Elöszőr megnézi, hogy van-e cső,
     * amit le lehet rakni, és ha van, csak akkor csinál bármit is. Az element attribútumon meghívja
     * a vele analóg nevű metódust. Ez megpróbálja hozzáadni a neigbors tömbhöz az addNeighbor
     * függvény segítségével a pickedUpPipe-ot. Ha ez sikerült akkor az addNeighbor visszatér
     * true-val, ellenkező esetben false-al. Ha true-val tért vissza, akkor a pickedUpPipe tagváltozó
     * értékét null-ra állítja.
     *
     * @return Sikeres volt-e a művelet.
     *
     */
    @Override
    public boolean connectPipeEnd(){
        boolean success = false;
        if(pickedUpPipe!=null){
            success = element.connectPipeEnd(pickedUpPipe);
            if(success)
                setPickedUpPipe(null);
        }
        return success;
    }
}
