import javax.swing.*;

public class SaboteurMenu extends ContextMenu {

    public SaboteurMenu(SpriteController controller) {

        super(controller);

        // menu beallitasa
        JMenuItem makeSlipperyItem = new JMenuItem("Make Slippery");
        makeSlipperyItem.setActionCommand("Make Slippery");
        makeSlipperyItem.addActionListener(new MenuItemActionListener(this));
        this.add(makeSlipperyItem);
    }
}
