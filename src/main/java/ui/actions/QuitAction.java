package ui.actions;

import ui.I18N;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class QuitAction extends AbstractAction {

    private static final ui.I18N I18N = new I18N(QuitAction.class);

    public QuitAction() {
        super(I18N.getString("name"), Icons.QUIT_ICON);
        putValue(SHORT_DESCRIPTION, I18N.getString("description"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
