import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class InputActionListener implements ActionListener {

    private ContextMenu contextMenu;
    //private JTextField textField;
    private JComboBox comboBox1;
    private JComboBox comboBox2;

    /**
     * Konstruktor ami var egy context menut es egy textfield-ot
     * A textfield fogja tartalmazzni az inputot
     * @param contextMenu
     * @param
     */
    //public InputActionListener(ContextMenu contextMenu, JTextField textField) {
    public InputActionListener(ContextMenu contextMenu, JComboBox comboBox1, JComboBox comboBox2) {

        this.contextMenu = contextMenu;
        this.comboBox1 = comboBox1;
        this.comboBox2 = comboBox2;
    }

    /**
     * actionPerformed implementalasa
     * @param e
     */
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand() == "OK") {

            //contextMenu.notifyControllerInput(new ArrayList<String>(Arrays.asList(textField.getText().split(" "))));
            //contextMenu.setCurrentInput(new ArrayList<String>(Arrays.asList(textField.getText().split(" "))));
            if(contextMenu.getChoice() != "Redirect") {

                ArrayList<String> list = new ArrayList<String>();
                list.add(Integer.toString(comboBox1.getSelectedIndex()));
                contextMenu.notifyControllerInput(list);
                contextMenu.setCurrentInput(list);
            }
            else {

                ArrayList<String> list = new ArrayList<String>();
                System.out.println(comboBox1.getSelectedIndex());
                list.add(Integer.toString(comboBox1.getSelectedIndex()));
                list.add(Integer.toString(comboBox2.getSelectedIndex()));
                contextMenu.notifyControllerInput(list);
                contextMenu.setCurrentInput(list);
            }
            contextMenu.hidePopup();
        }
    }
}
