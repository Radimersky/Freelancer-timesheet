package ui.actions;


import ui.MainWindow;

import javax.swing.*;
import java.net.URL;

final public class Icons {
    public static final Icon DELETE_ICON = createIcon("Crystal_Clear_action_button_cancel.png");
    public static final Icon EDIT_ICON = createIcon("Crystal_Clear_action_edit.png");
    public static final Icon ADD_ICON = createIcon("Crystal_Clear_action_edit_add.png");
    public static final Icon QUIT_ICON = createIcon("Crystal_Clear_action_exit.png");
    public static final Icon INVOICE_ICON = createIcon("action_invoice.png");

    private Icons() {
        throw new AssertionError("This class is not instantiable");
    }

    private static ImageIcon createIcon(String name) {
        URL url = MainWindow.class.getResource(name);
        if (url == null) {
            throw new RuntimeException("Icon " + name + " not found!");
        }
        return new ImageIcon(url);
    }

}
