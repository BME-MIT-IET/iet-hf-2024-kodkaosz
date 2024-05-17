package DrukmakoriSivatag;

import java.util.ArrayList;

/**
 * Periodikus időzítő. Ez időzíti a Tickable interfészt megvalósító osztályok (Pump, Pipe,
 * WaterTank, WaterSource, Person) periodikus működését.
 */
public class Timer {
    /**
     * Heterogén kollekció, azokat a pályaelemeket tárolja, amelyek
     * megvalósítják a Tickable interfészt.
     */
    private ArrayList<Tickable> tickables;

    /**
     * Timer osztály paraméternélküli konstruktora.
     */
    public Timer(){
        tickables = new ArrayList<>();
    }
    /**
     *  A tickables kollekció minden elemének meghívja a tick() függvényét.
     */
    public void tick(){
        for(int i=0;i< tickables.size();i++){
            tickables.get(i).tick();
        }
    }

    /**
     * Hozzáadja a paraméterként kapott újabb elemet a
     * tickables kollekcióhoz.
     * @param obj az új Tickable objektum
     */
    public void addTickable(Tickable obj){
        tickables.add(obj);
    }
}
