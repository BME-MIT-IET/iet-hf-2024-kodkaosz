import javax.swing.*;

public class PlumberMenu extends ContextMenu {

    public PlumberMenu(SpriteController controller) {

        super(controller);

        // letrehozza az egyes menuket
        JMenuItem fixItem = new JMenuItem("Fix");
        JMenuItem pickUpPumpItem = new JMenuItem("Pick Up Pump");
        JMenuItem addPumpItem = new JMenuItem("Add Pump");
        JMenuItem pickUpPipeEndItem = new JMenuItem("Pick Up Pipe End");
        JMenuItem pickUpPipeItem = new JMenuItem("Pick Up Pipe");
        JMenuItem connectPipeItem = new JMenuItem("Connect Pipe End");

        // menuk action commandjainak bealllitasa
        fixItem.setActionCommand("Fix");
        pickUpPumpItem.setActionCommand("Pick Up Pump");
        addPumpItem.setActionCommand("Add Pump");
        pickUpPipeEndItem.setActionCommand("Pick Up Pipe End");
        pickUpPipeItem.setActionCommand("Pick Up Pipe");
        connectPipeItem.setActionCommand("Connect Pipe End");

        // action listenerek letrehozasa
        fixItem.addActionListener(new MenuItemActionListener(this));
        pickUpPumpItem.addActionListener(new MenuItemActionListener(this));
        addPumpItem.addActionListener(new MenuItemActionListener(this));
        pickUpPipeEndItem.addActionListener(new MenuItemActionListener(this));
        pickUpPipeItem.addActionListener(new MenuItemActionListener(this));
        connectPipeItem.addActionListener(new MenuItemActionListener(this));

        // menuk hozzaadasa
        this.add(fixItem);
        this.add(pickUpPumpItem);
        this.add(addPumpItem);
        this.add(pickUpPipeEndItem);
        this.add(pickUpPipeItem);
        this.add(connectPipeItem);
    }
}
