/**
 * Interfész, az összes aktív elem (forrás, ciszterna, pumpa) megvalósítja, ez felelős a periodikusan történő eseményeikért.
 */
public interface Tickable {
    /**
     * Az interfészt megvalósító osztályok ebben a függvényben valósítják meg a periodikus működésüket.
     */
    public void tick();
}
