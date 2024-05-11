import static java.lang.Math.random;

/**
 * Ez az osztály minden olyan eseményért felelős, ami véletlen következik be. Ilyen például a pumpa
 * elromlása és az új csövek generálása.
 */
public class Random {
    /**
     * Random osztály paraméternélküli konstruktora.
     */
    public Random(){

    }
    /**
     *  Egy random generált érték alapján eldönti, hogy elromoljon-e a pumpa, vagy sem, majd visszatér a döntéssel
     * @return true, ha elromoljon a pumpa, false, ha ne
     */
    public boolean decidePump(){
        if(Main.proto.getIsDebug()){
            return true;
        }
        int sz = (int) (random() * 100);
        if (sz % 2 == 0) {
            return true;
        }
        return false;
    }

    /**
     * Egy random érték alapján eldönti, hogy létrejöjjön-e új cső a
     * ciszternánál,majd visszatér a döntéssel.
     * @return true, ha jöjjön létre új cső, false, ha ne
     */
    public boolean decideNewPipe(){
        if(Main.proto.getIsDebug()){
            return false; // ne jöjjenek létre random csövek, amikor nem szamitunk rajuk
        }
        int sz= (int) (random() * 100);
        if(sz%2==0){
            return true;
        }
        return false;
    }

    /**
     * Egy random javítási időt generál egy csőnek és visszatér
     * vele. Ha egy csőnek a fixedTime értéke nem nulla, nem lehet kilyukasztani.
     * @return random javítási idő
     */
    public int decideFixedTime(){
        if(Main.proto.getIsDebug()==true){
            return 3;
        }
        java.util.Random random= new java.util.Random();
        return random.nextInt(5)+1;
    }

    /**
     * Egy random időt generál egy játékosnak, ha egy ragadós
     * csőre lép, majd visszatér vele. Ha egy játékosnak a stuckTime értéke nem nulla,
     * nem léphet.
     * @return random StuckTime
     */
    public int decideStuckTime(){
        if(Main.proto.getIsDebug()==true){
            return 3;
        }
        java.util.Random random= new java.util.Random();
        return random.nextInt(5)+1;
    }

    /**
     * Egy random 0 vagy 1 értéket generál, majd visszatér vele.
     * Ha 0, akkor a cső 0. indexre csúszik a játékos, egyébként az 1. indexre.
     * @return melyik indexű szomszédra lépjen a játékos
     */
    public int decideSlipping(){
        if(Main.proto.getIsDebug()==true){
            return 0;
        }
        int sz=(int)random()*100;
        return sz%2;
    }
}
