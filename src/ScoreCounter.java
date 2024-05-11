import javax.swing.*;
import java.awt.*;

/**
 * Tárolja és megjeleníti a képernyőn a szabotőrök és a szerelők pontjait.
 */
public class ScoreCounter extends JLabel {
    /**
     * Annak a csapatnak a pontszáma, amelyik kezelésére létre lett hozva.
     */
    private int score = 0;
    public ScoreCounter(String s){
        this.setFont(new Font("Serif", Font.PLAIN, 39));
        this.setText(s+Integer.toString(score));
    }

    /**
     * A score tagváltozónak megfelelően frissíti a Label-re kiírt számot.
     */
    public void update() {

        this.setText(Integer.toString(score));

    }

    /**
     * Setter a score adattaghoz.
     * @param newScore Az új pontszám.
     */
    public void setScore(int newScore) {
        score = newScore;
    }
}
