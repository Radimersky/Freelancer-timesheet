package ui;

import data.ClientDao;
import data.TimeEntryDao;
import data.WorkTypeDao;
import ui.actions.AddAction;
import ui.actions.CreateInvoiceAction;
import ui.actions.DeleteAction;
import ui.actions.EditAction;
import ui.actions.QuitAction;
import ui.model.ClientModel;
import ui.model.TimeEntryModel;
import ui.model.WorkTypeModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.util.List;

public class MainWindow {

    private static final I18N I18N = new I18N(MainWindow.class);
    private final JFrame frame;
    private final TabContainer tabContainer = new TabContainer();
    private final Action addAction = new AddAction(tabContainer::getSelectedTab);
    private final Action deleteAction = new DeleteAction(tabContainer::getSelectedTab);
    private final Action editAction = new EditAction(tabContainer::getSelectedTab);
    private final Action createInvoiceAction;

    private MainWindow(TimeEntryDao timeEntryDao, ClientDao clientDao, WorkTypeDao workTypeDao) {
        createInvoiceAction = new CreateInvoiceAction(clientDao, tabContainer::getSelectedTab);
        frame = createFrame();
        frame.setJMenuBar(createMenuBar());
        addTabs(timeEntryDao, clientDao, workTypeDao);
        tabContainer.addChangeListener(e -> updateActions());
        updateActions();
        frame.add(tabContainer.getComponent(), BorderLayout.CENTER);
        frame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.pack();
    }

    private void addTabs(TimeEntryDao timeEntryDao, ClientDao clientDao, WorkTypeDao workTypeDao){
        var timeEntryModel = new TimeEntryModel(timeEntryDao);
        var clientModel = new ClientModel(clientDao);
        var workTypeModel = new WorkTypeModel(workTypeDao);
        clientModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                timeEntryModel.load();
            }
        });

        workTypeModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                timeEntryModel.load();
            }
        });
        var actions = List.of(addAction, deleteAction, editAction);
        addTab(new TimeEntryTab(timeEntryModel, actions, clientDao, workTypeDao));
        addTab(new ClientTab(clientModel, actions));
        addTab(new WorkTypeTab(workTypeModel, actions));
    }

    public static void run(TimeEntryDao timeEntryDao, ClientDao clientDao, WorkTypeDao workTypeDao) {
        try {
            new MainWindow(timeEntryDao, clientDao, workTypeDao).show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Object[] options = {
                    new JButton(new QuitAction())
            };
            JOptionPane.showOptionDialog(null,
                    I18N.getString("failedDialogText"), I18N.getString("failedDialogTitle"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
                    null, options, options[0]);
        }
    }

    private void addTab(Tab<?> tab) {
        tab.addListSelectionListener(e -> updateActions());
        tabContainer.addTab(tab);
    }

    private void show() {
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        var frame = new JFrame(I18N.getString("title"));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(250, 170);
        return frame;
    }

    private JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();
        var editMenu = new JMenu(I18N.getString("editMenu"));
        editMenu.setMnemonic('e');
        editMenu.add(addAction);
        editMenu.add(deleteAction);
        editMenu.add(editAction);
        menuBar.add(editMenu);
        return menuBar;
    }

    private JToolBar createToolbar() {
        var toolbar = new JToolBar();
        toolbar.add(new QuitAction());
        toolbar.addSeparator();
        toolbar.add(addAction);
        toolbar.add(deleteAction);
        toolbar.add(editAction);
        toolbar.add(createInvoiceAction);
        return toolbar;
    }

    private void updateActions() {
        int selectedRowsCount = tabContainer.getSelectedTab().getSelectedRowsCount();
        editAction.setEnabled(selectedRowsCount == 1);
        deleteAction.setEnabled(selectedRowsCount >= 1);
        createInvoiceAction.setEnabled(selectedRowsCount >= 1 && tabContainer.getSelectedTab() instanceof TimeEntryTab);
    }
}
