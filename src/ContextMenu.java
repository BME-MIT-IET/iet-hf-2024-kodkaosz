import javax.swing.*;
import java.util.ArrayList;

public abstract class ContextMenu extends JPopupMenu {

    protected String chosenMenu;
    protected ArrayList<String> currentInput;
    protected JPopupMenu inputPopup;

    private SpriteController controller;

    private int x;
    private int y;

    private Integer[] comboVals = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    private JComboBox comboBox1;
    private JComboBox comboBox2;

    /**
     * Default konstruktor
     */
    public ContextMenu(SpriteController controller) {

        this.controller = controller;

        // egyes menuk letrehozasa
        JMenuItem moveItem = new JMenuItem("Move");
        JMenuItem damageItem = new JMenuItem("Damage");
        JMenuItem redirectItem = new JMenuItem("Redirect");
        JMenuItem makeStickyItem = new JMenuItem("Make Sticky");

        // menuk action commandjainak beallitasa
        moveItem.setActionCommand("Move");
        damageItem.setActionCommand("Damage");
        redirectItem.setActionCommand("Redirect");
        makeStickyItem.setActionCommand("Make Sticky");

        // action listenerek letrehozasa
        moveItem.addActionListener(new MenuItemActionListener(this));
        damageItem.addActionListener(new MenuItemActionListener(this));
        redirectItem.addActionListener(new MenuItemActionListener(this));
        makeStickyItem.addActionListener(new MenuItemActionListener(this));

        // menuk hozzaadasa
        this.add(moveItem);
        this.add(damageItem);
        this.add(redirectItem);
        this.add(makeStickyItem);

        // input popup letrehozasa
        inputPopup = new JPopupMenu();
        final JTextField inputField = new JTextField("Input", 10);
        comboBox1 = new JComboBox(comboVals);
        comboBox2 = new JComboBox(comboVals);
        JButton confirmButton = new JButton("OK");
        inputField.setEnabled(true);
        inputField.setEditable(true);
        confirmButton.setActionCommand("OK");
        confirmButton.setFocusable(false);
        confirmButton.addActionListener(new InputActionListener(this, comboBox1, comboBox2));

        inputPopup.add(comboBox1);
        inputPopup.add(comboBox2);
        inputPopup.add(confirmButton);
    }

    public void notifyControllerChoice(String choice) {

        controller.gotChoice(choice);
    }

    public void notifyControllerInput(ArrayList<String> input) {

        controller.gotInput(input);
    }

    public void showInputPopup() {

        if(!chosenMenu.equalsIgnoreCase("Redirect")) {

            comboBox2.setVisible(false);
        }

        inputPopup.setLocation(x, y);
        inputPopup.setVisible(true);
    }

    /**
     * A context menut megjeleniti
     */
    public void showInput(int x, int y) {

        this.x = x;
        this.y = y;

        this.setLocation(x, y);
        this.setVisible(true);
    }

    /**
     * A context menut bezarja
     */
    public void hidePopup() {

        inputPopup.setVisible(false);
        this.setVisible(false);
    }

    /**
     * Choice getter
     * @return
     */
    public String getChoice() {

        return chosenMenu;
    }

    /**
     * Input getter
     * @return input mint lista
     */
    public ArrayList<String> getCurrentInput() {

        return currentInput;
    }

    /**
     * choice setter
     * @param selectedChoice
     */
    public void setChoice(String selectedChoice) {

        chosenMenu = selectedChoice;
    }

    /**
     * input setter
     * @param currentInput
     */
    public void setCurrentInput(ArrayList<String> currentInput) {

        this.currentInput = currentInput;
    }
}
