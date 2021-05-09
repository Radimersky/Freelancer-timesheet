package ui;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.IdentityHashMap;
import java.util.Map;

public class TabContainer {

    private static final I18N I18N = new I18N(TabContainer.class);

    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final Map<Component, Tab<?>> tabs = new IdentityHashMap<>();

    public void addChangeListener(ChangeListener l) {
        tabbedPane.addChangeListener(l);
    }

    JComponent getComponent() {
        return tabbedPane;
    }

    void addTab(Tab<?> tab) {
        var component = tab.getComponent();
        tabbedPane.addTab(tab.getTitle(), component);
        tabs.put(component, tab);
    }

    Tab<?> getSelectedTab() {
        var selectedComponent = tabbedPane.getSelectedComponent();
        if (selectedComponent == null) {
            throw new IllegalStateException(I18N.getString("noSelected"));
        }
        var tab = tabs.get(selectedComponent);
        if (tab == null) {
            throw new AssertionError(I18N.getString("unknown"));
        }
        return tab;
    }
}
