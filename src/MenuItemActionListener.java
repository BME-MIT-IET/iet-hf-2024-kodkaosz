import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class MenuItemActionListener implements ActionListener {

    private ContextMenu contextMenu;

    /**
     * Milyen parancsokra kell megjeleniteni az inputot
     */
    private final String[] inputCommands = new String[] {

        "Move", "Redirect", "Pick Up Pipe", "Pick Up Pipe End", "Connect Pipe End"
    };

    /**
     * Konstruktor ami var egy context menut
     * @param contextMenu
     */
    public MenuItemActionListener(ContextMenu contextMenu) {

        this.contextMenu = contextMenu;
    }

    /**
     * actionPerformed implementalasa
     * @param e
     */
    public void actionPerformed(ActionEvent e) {

        contextMenu.notifyControllerChoice(e.getActionCommand());
        contextMenu.setChoice(e.getActionCommand());
        if(Arrays.asList(inputCommands).contains(e.getActionCommand())) {

            contextMenu.showInputPopup();
        }
        else {

            contextMenu.setVisible(false);
        }
    }
}
